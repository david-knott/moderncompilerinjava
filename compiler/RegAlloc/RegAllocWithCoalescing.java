package RegAlloc;

import java.util.Hashtable;

import Assem.Instr;
import Assem.InstrList;
import Assem.MOVE;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Frame.Access;
import Frame.Frame;
import Graph.NodeList;
import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;

/**
 * RegAllocWithCoalescing class manages the spilling.
 */
public class RegAllocWithCoalescing implements TempMap {
    public InstrList instrList;
    public Frame frame;
    //private TempList spillTemps;

    private InstructionWorkList workListMoves;
    private InstructionWorkList activeMoves;
    private InstructionWorkList frozenMoves;
    private InstructionWorkList constrainedMoves;
    private InstructionWorkList coalescedMoves;

    private Hashtable<Temp, InstructionWorkList> moveList = new Hashtable<Temp, InstructionWorkList>();
    private Hashtable<Temp, TempWorkList> adjList = new Hashtable<Temp, TempWorkList>();

    private TempWorkList spilledNodes; //nodes marked for spilling.
    private TempWorkList colouredNodes; //nodes that have been coloured.
    private TempWorkList precoloured; //can be created from our list of registers

    private Hashtable<Temp, Temp> colour = new Hashtable<Temp, Temp>();

    private TempList initial;
    private Hashtable<Temp, Integer> degree = new Hashtable<Temp, Integer>();
    private int K;

    private TempWorkList spillWorkList;
    private TempWorkList freezeWorkList;
    private TempWorkList simplifyWorkList;
    private TempWorkList coalescedNodes;
    private TempWorkList stack;
    private Hashtable<Temp, Temp> alias = new Hashtable<Temp, Temp>();

    private FlowGraph flowGraph;
    private Liveness liveness;

    private TempWorkList liveOut(Instr head) {
        return liveness.liveOut(this.flowGraph.node(head));
    }

    private void liveness() {
        flowGraph = new AssemFlowGraph(this.instrList);
        liveness = new Liveness(flowGraph);
    }

    private TempWorkList use(Instr instr) {
        TempWorkList nodeWorkList = null;
        for(TempList tempList = instr.use(); tempList != null; tempList = tempList.tail) {
            nodeWorkList = TempWorkList.append(nodeWorkList, tempList.head);
        }
        return nodeWorkList;
    }

    private TempWorkList def(Instr instr) {
        TempWorkList nodeWorkList = null;
        for(TempList tempList = instr.def(); tempList != null; tempList = tempList.tail) {
            nodeWorkList = TempWorkList.append(nodeWorkList, tempList.head);
        }
        return nodeWorkList;
    }

    private void build() {
        for(InstrList instrList = InstrList.reverse(this.instrList); instrList != null; instrList = instrList.tail) {
            TempWorkList live = liveOut(instrList.head);
            if(instrList.head instanceof MOVE) {
                live = TempWorkList.andOr(live, use(instrList.head));
                for(TempWorkList n = TempWorkList.or(def(instrList.head), use(instrList.head)); n != null; n = n.next) {
                    this.moveList.put(n.me, InstructionWorkList.or(this.moveList.get(n.me), instrList.head));
                }
                this.workListMoves = InstructionWorkList.or(this.workListMoves, instrList.head);
            }
            live = TempWorkList.or(live, def(instrList.head));
            for(var d = def(instrList.head); d != null; d = d.next) {
                for(var l = live; l != null; l = l.next) {
                    this.addEdge(l.me, d.me);
                }
            }
        }
    }

    /**
     * Add temps and their related nodes into the correct worklist.
     * If the degree is greater or equal to K, we add to the spill work list
     * If the node is move related, add it to the freeze work list.
     * Otherwise add it to the simplify work list
     */
    private void makeWorklist() {
        for (TempList tempList = initial; tempList != null; tempList = tempList.tail) {
            Temp temp = tempList.head;
            if (degree.get(temp) >= K) {
                this.spillWorkList = TempWorkList.or(this.spillWorkList, new TempWorkList(temp));
            } else if (moveRelated(temp)) {
                this.freezeWorkList = TempWorkList.or(this.freezeWorkList, new TempWorkList(temp));
            } else {
                this.simplifyWorkList = TempWorkList.or(this.simplifyWorkList, new TempWorkList(temp));
            }
        }
    }

    private InstructionWorkList nodeMoves(Temp temp) {
        return InstructionWorkList.and(this.moveList.get(temp),
                InstructionWorkList.or(this.activeMoves, this.workListMoves));
    }

    private boolean moveRelated(Temp node) {
        return nodeMoves(node) == null;
    }

    /**
     * Add nodes to the select stack and decrement their adjacent nodes degrees.
     */
    private void simplify() {
        for (TempWorkList nodeWorkList = this.simplifyWorkList; nodeWorkList != null; nodeWorkList = nodeWorkList.next) {
            //stack.push(nodeWorkList.me);
            this.stack = TempWorkList.append(this.stack, nodeWorkList.me);
            for (TempWorkList adjacent = this.adjacent(nodeWorkList.me); adjacent != null; adjacent = adjacent.next) {
                this.decrementDegree(adjacent.me);
            }
        }
    }

    /**
     * Decrement node head's degree by one. If this node moves from > K to K
     * we enable moves for this node and its adjacent nodes. We then remove this
     * node from the spill work list.
     * If the node is move related, we move it to the freeze worklist, if not,
     * move it to the simplify worklist.
     * This method can be called from either the simplify method or the combine method.
     * @param head
     */
    private void decrementDegree(Temp head) {
        int d = this.degree.get(head);
        d = d - 1;
        if (d == this.K) {
            this.enableMoves(TempWorkList.or(new TempWorkList(head), this.adjacent(head)));
            this.spillWorkList = TempWorkList.andOr(this.spillWorkList, new TempWorkList(head));
            if (this.moveRelated(head)) {
                this.freezeWorkList = TempWorkList.or(this.freezeWorkList, new TempWorkList(head));
            } else {
                this.simplifyWorkList = TempWorkList.or(this.simplifyWorkList, new TempWorkList(head));
            }
        }
    }

    private void enableMoves(TempWorkList or) {
        for (; or != null; or = or.next) {
            for (InstructionWorkList instructionWorkList = this
                    .nodeMoves(or.me); instructionWorkList != null; instructionWorkList = instructionWorkList.next) {
                if (InstructionWorkList.contains(this.activeMoves, instructionWorkList.me)) {
                    this.activeMoves = InstructionWorkList.andOr(this.activeMoves, new InstructionWorkList(instructionWorkList.me));
                    this.workListMoves = InstructionWorkList.andOr(this.workListMoves, new InstructionWorkList(instructionWorkList.me));
                }
            }
        }
    }

    /**
     * Returns the alias for a coalesced node.
     * @param node
     * @return this nodes alias.
     */
    private Temp getAlias(Temp node) {
        if (TempWorkList.contains(this.coalescedNodes, node)) {
            return this.getAlias(this.alias.get(node));
        }
        return node;
    }

    private void coalesce() {
        Instr instr = this.workListMoves.me;
        Temp x = this.getAlias(instr.use().head);
        Temp y = this.getAlias(instr.def().head);
        Temp u, v;
        if (TempList.contains(this.frame.registers(), y)) {
            u = y;
            v = x;
        } else {
            u = x;
            v = y;
        }
        InstructionWorkList m = new InstructionWorkList(instr);
        this.workListMoves = InstructionWorkList.andOr(this.workListMoves, m);
        if (u == v) {
            this.coalescedMoves = InstructionWorkList.or(this.coalescedMoves, m);
            this.addWorkList(u);
        } else if (TempList.contains(this.frame.registers(), v) || this.inAdjacentSet(u, v)) {
            this.constrainedMoves = InstructionWorkList.or(this.constrainedMoves, m);
            this.addWorkList(u);
            this.addWorkList(v);
        } else if (TempList.contains(this.frame.registers(), u) && this.checkOk(v, u)
                || (!TempList.contains(this.frame.registers(), u)
                        && this.conservative(TempWorkList.or(this.adjacent(u), this.adjacent(v))))) {
            this.coalescedMoves = InstructionWorkList.or(this.coalescedMoves, m);
            this.combine(u, v);
            this.addWorkList(u);
        } else {
            this.activeMoves = InstructionWorkList.or(this.activeMoves, m);
        }
    }

    private boolean checkOk(Temp v, Temp u) {
        for (var t = this.adjacent(v); t != null; t = t.next) {
            if (!okay(t.me, u)) {
                return false;
            }
        }
        return true;
    }

    private boolean okay(Temp t, Temp r) {
        return degree.get(t) < K || TempWorkList.contains(this.precoloured, t) || this.inAdjacentSet(t, r);
    }

    private void combine(Temp u, Temp v) {
        if (TempWorkList.contains(this.freezeWorkList, v)) {
            this.freezeWorkList = TempWorkList.andOr(this.freezeWorkList, new TempWorkList(v));
        } else {
            this.spillWorkList = TempWorkList.andOr(this.spillWorkList, new TempWorkList(v));
        }
        this.coalescedNodes = TempWorkList.or(this.coalescedNodes, new TempWorkList(v));
        this.alias.put(v, u);
        var uwl = this.moveList.get(u);
        var vwl = this.moveList.get(v);
        this.moveList.put(u, InstructionWorkList.or(uwl, vwl));
        for (TempWorkList t = this.adjacent(v); t != null; t = t.next) {
            this.addEdge(t.me, u);
            this.decrementDegree(t.me);
        }
        if (degree.get(u) >= this.K && TempWorkList.contains(this.freezeWorkList, u)) {
            this.freezeWorkList = TempWorkList.andOr(this.freezeWorkList, new TempWorkList(u));
            this.spillWorkList = TempWorkList.andOr(this.spillWorkList, new TempWorkList(u));
        }
    }

    private void freeze() {
        Temp u = this.freezeWorkList.me;
        this.freezeWorkList = TempWorkList.andOr(this.freezeWorkList, new TempWorkList(u));
        this.simplifyWorkList = TempWorkList.or(this.simplifyWorkList, new TempWorkList(u));
        this.freezeMoves(u);
    }

    private void freezeMoves(Temp u) {
        for (var m = this.nodeMoves(u); m != null; m = m.next) {
            Temp x = null;
            Temp y = null;
            Temp v = null;
            if (this.getAlias(y) == this.getAlias(u)) {
                v = this.getAlias(x);
            } else {
                v = this.getAlias(y);
            }
            this.activeMoves = InstructionWorkList.andOr(this.activeMoves, new InstructionWorkList(m.me));
            this.frozenMoves = InstructionWorkList.or(this.frozenMoves, new InstructionWorkList(m.me));
            if (this.nodeMoves(v) == null && this.degree.get(v) < this.K) {
                this.freezeWorkList = TempWorkList.andOr(this.freezeWorkList, new TempWorkList(v));
                this.simplifyWorkList = TempWorkList.or(this.simplifyWorkList, new TempWorkList(v));
            }
        }
    }

    /**
     * Selects the node to spill from the spill worklist.
     * @return a Temp
     */
    private Temp nodeToSpill() {
        Temp m = this.spillWorkList.me;
        return m;
    }

    private void selectSpill() {
        Temp m = this.nodeToSpill();
        this.spillWorkList = TempWorkList.andOr(this.spillWorkList, new TempWorkList(m));
        this.simplifyWorkList = TempWorkList.or(this.simplifyWorkList, new TempWorkList(m));
        this.freezeMoves(m);
    }

    private void addEdge(Temp u, Temp v) {
        if(this.inAdjacentSet(u, v) && u != v) {
            this.addAdjacentSet(u, v);
            this.addAdjacentSet(v, u);
        }
        if(!TempWorkList.contains(this.precoloured, u)) {
            this.adjList.put(u, TempWorkList.or(this.adjList.get(u), new TempWorkList(v)));
            this.degree.put(u, this.degree.getOrDefault(u, 0));
        }
        if(!TempWorkList.contains(this.precoloured, v)) {
            this.adjList.put(v, TempWorkList.or(this.adjList.get(v), new TempWorkList(u)));
            this.degree.put(v, this.degree.getOrDefault(v, 0));
        }
    }

    private boolean conservative(TempWorkList nodeWorkList) {
        int k = 0;
        for (; nodeWorkList != null; nodeWorkList = nodeWorkList.next) {
            if (degree.get(nodeWorkList.me) >= K) {
                k++;
            }
        }
        return k < K;
    }

    private boolean inAdjacentSet(Temp u, Temp v) {
        throw new Error("Not impleented");
    }

    private void addAdjacentSet(Temp u, Temp v) {
        throw new Error("Not impleented");
    }

    private void addWorkList(Temp u) {
        if(!TempWorkList.contains(this.precoloured, u)
        && !this.moveRelated(u)
        && this.degree.get(u) < this.K) {
            this.freezeWorkList = TempWorkList.andOr(this.freezeWorkList, new TempWorkList(u));
            this.simplifyWorkList = TempWorkList.or(this.simplifyWorkList, new TempWorkList(u));
        }
    }

    private TempWorkList adjacent(Temp me) {
        return TempWorkList.andOr(this.adjList.get(me), TempWorkList.or(this.stack, this.coalescedNodes));
    }

    private void assignColours() {
        while (this.stack != null) {
            Temp n = TempWorkList.last(this.stack);
            this.stack = TempWorkList.andOr(this.stack, new TempWorkList(n));
            TempList okColours = this.frame.registers();
            for (var w = this.adjList.get(n); w != null; w = w.next) {
                if (TempWorkList.contains(TempWorkList.or(this.colouredNodes, this.precoloured), this.getAlias(w.me))) {
                    okColours = TempList.andNot(okColours, new TempList(this.colour.get(this.getAlias(w.me))));
                }
            }
            if (okColours == null) {
                this.spilledNodes = TempWorkList.or(this.spilledNodes, n);
            } else {
                this.colouredNodes = TempWorkList.or(this.colouredNodes, n);
                var c = okColours.head;
                this.colour.put(n, c);
            }
        }
        for(var n = this.coalescedNodes; n != null; n = n.next) {
            this.colour.put(n.me, this.colour.get(this.getAlias(n.me)));
        }
    }

    private void rewrite() {
        TempWorkList newTemps = null;
        for(;this.spilledNodes != null; this.spilledNodes = this.spilledNodes.next) {
            newTemps = TempWorkList.or(newTemps, this.rewrite(new TempList(this.spilledNodes.me)));
        }
        this.spilledNodes = null;
        this.initial = null;
        this.colouredNodes = null;
        this.coalescedNodes = null;
    }

    private TempWorkList rewrite(TempList spills) {
        // this.spilledNodes
        // TempList spills = this.selectSpill();
        InstrList newList = null;
        TempWorkList newTemps = null;
        Hashtable<Temp, Access> accessHash = new Hashtable<Temp, Access>();
        for (; instrList != null; instrList = instrList.tail) {
            TempList spilledDefs = TempList.and(instrList.head.def(), spills);
            TempList spilledUses = TempList.and(instrList.head.use(), spills);
            if (TempList.or(spilledDefs, spilledUses) == null) {
                newList = InstrList.append(newList, instrList.head);
                continue;
            }
            for (; spilledUses != null; spilledUses = spilledUses.tail) {
                Access access = accessHash.get(spilledUses.head);
                Temp spillTemp = Temp.create();
                //TODO: temps do not have a corresponding node in the graph at this point.
                //newTemps = NodeWorkList.append(newTemps, spillTemp);
                //this.spillTemps = TempList.append(this.spillTemps, spillTemp);
                InstrList memoryToTemp = frame.memoryToTemp(spilledUses.head, spillTemp, access);
                newList = InstrList.append(newList, memoryToTemp);
            }
            newList = InstrList.append(newList, instrList.head);
            for (; spilledDefs != null; spilledDefs = spilledDefs.tail) {
                Temp spillTemp = Temp.create();
                //newTemps = NodeWorkList.append(newTemps, spillTemp);
                Access access = this.frame.allocLocal(false);
                accessHash.put(spilledDefs.head, access);
                //this.spillTemps = TempList.append(this.spillTemps, spillTemp);
                InstrList tempToMemory = frame.tempToMemory(spilledDefs.head, spillTemp, access);
                newList = InstrList.append(newList, tempToMemory);
            }
        }
        this.instrList = newList;
        return newTemps;
    }

    private void main() {
        this.liveness();
        this.build();
        this.makeWorklist();
        do {
            if (this.simplifyWorkList != null) {
                this.simplify();
            }
            if (this.workListMoves != null) {
                this.coalesce();
            }
            if (this.freezeWorkList != null) {
                this.freeze();
            }
            if (this.spillWorkList != null) {
                this.selectSpill();
            }
        } while (this.simplifyWorkList != null || this.workListMoves != null || this.freezeWorkList != null
                || this.spillWorkList != null);
        this.assignColours();
        if (this.spilledNodes != null) {
            this.rewrite();
            this.main();
        }
    }

    public RegAllocWithCoalescing(Frame frame, InstrList instrList) {
        this.instrList = instrList;
        this.frame = frame;
        this.K = this.frame.registers().size();
        this.initial = null;
        this.main();
    }

    @Override
    public String tempMap(Temp t) {
        return null;
    }
}