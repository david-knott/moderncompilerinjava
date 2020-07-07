package RegAlloc;

import java.util.Hashtable;

import Assem.Instr;
import Assem.InstrList;
import Assem.MOVE;
import Codegen.Assert;
import Core.Component;
import Core.LL;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Frame.Access;
import Frame.Frame;
import Graph.Node;
import Intel.IntelFrame;
import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;

/**
 * RegAllocWithCoalescing class manages the spilling.
 */
public class RegAllocCoalesce extends Component implements TempMap {
    public InstrList instrList;
    public Frame frame;
    private int K;
    // move related worklists
    private Hashtable<Temp, LL<Temp>> adjList = new Hashtable<Temp, LL<Temp>>();
    private Hashtable<Temp, LL<Temp>> addSet = new Hashtable<Temp, LL<Temp>>();
    private LL<Temp> spilledNodes; // nodes marked for spilling.
    private LL<Temp> colouredNodes; // nodes that have been coloured.
    private LL<Temp> precoloured; // can be created from our list of registers
    private Hashtable<Temp, Temp> colour = new Hashtable<Temp, Temp>();
    private LL<Temp> initial;
    private Hashtable<Temp, Integer> degree = new Hashtable<Temp, Integer>();
    private LL<Temp> spillWorkList;
    private LL<Temp> simplifyWorkList;
    private LL<Temp> stack;
	private LL<Temp> spillTemps;
	private LL<Temp> coalescedNodes;
    private FlowGraph flowGraph;
    private Liveness liveness;
    private Hashtable<Temp, Integer> useCount = new Hashtable<Temp, Integer>();
    private Hashtable<Temp, Integer> defCount = new Hashtable<Temp, Integer>();

    private LL<Instr> workListMoves;
    private LL<Instr> activeMoves;
    private LL<Instr> frozenMoves;
    private LL<Instr> coalescedMoves;
    private LL<Instr> constrainedMoves;
    private LL<Temp> freezeWorkList;
    private Hashtable<Temp, LL<Instr>> moveList = new Hashtable<Temp, LL<Instr>>();

    private Hashtable<Temp, Temp> alias = new Hashtable<Temp, Temp>();

    private void updateUseAndDefCounts() {
        useCount = new Hashtable<Temp, Integer>();
        defCount = new Hashtable<Temp, Integer>();
        for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
            Node node = nodes.head;
            for (TempList defs = flowGraph.def(node); defs != null; defs = defs.tail) {
                Temp defNode = defs.head;
                useCount.put(defNode, defCount.getOrDefault(node, 1) + 1);
            }
            for (TempList uses = flowGraph.use(node); uses != null; uses = uses.tail) {
                Temp useNode = uses.head;
                useCount.put(useNode, defCount.getOrDefault(node, 1) + 1);
            }
        }
    }

    private LL<Temp> liveOut(Instr head) {
        return liveness.liveOut(this.flowGraph.node(head));
    }

    private void liveness() {
        flowGraph = new AssemFlowGraph(this.instrList);
        liveness = new Liveness(flowGraph);
    }

    private LL<Temp> use(Instr instr) {
        LL<Temp> nodeWorkList = null;
        for (TempList tempList = instr.use(); tempList != null; tempList = tempList.tail) {
            nodeWorkList = LL.<Temp>insertOrdered(nodeWorkList, tempList.head);
        }
        return nodeWorkList;
    }

    private LL<Temp> def(Instr instr) {
        LL<Temp> nodeWorkList = null;
        for (TempList tempList = instr.def(); tempList != null; tempList = tempList.tail) {
            nodeWorkList = LL.<Temp>insertOrdered(nodeWorkList, tempList.head);
        }
        return nodeWorkList;
    }

    /**
     * Builds the interference graph and move list.
     */
    private void build() {
        for (InstrList il = this.instrList; il != null; il = il.tail) {
            LL<Temp> live = liveOut(il.head);
            var uses = use(il.head);
            var defs = def(il.head);
            if (il.head instanceof MOVE) {
                this.workListMoves = LL.<Instr>insertOrdered(this.workListMoves, il.head);
                for(LL<Temp> n = LL.<Temp>or(uses, defs); n != null; n = n.tail) {
                    this.moveList.put(n.head, LL.<Instr>insertOrdered(this.moveList.getOrDefault(n.head, null), il.head)); 
                }
                for (var l = live; l != null; l = l.tail) {
                    if (uses.head != l.head) {
                        this.addEdge(defs.head, l.head);
                    }
                }
            } else {
                for (; defs != null; defs = defs.tail) {
                    for (var l = live; l != null; l = l.tail) {
                        this.addEdge(defs.head, l.head);
                    }
                }
            }
        }
    }

    /**
     * Add temps and their related nodes into the correct worklist. If the degree is
     * greater or equal to K, we add to the spill work list If the node is move
     * related, add it to the freeze work list. Otherwise add it to the simplify
     * work list
     */
    private void makeWorklist() {
        while(initial != null) {
            Temp temp = initial.head;
            initial = LL.<Temp>andNot(initial, new LL<Temp>(temp));
            if (this.degree.get(temp) >= K) {
                this.addSpilledWorkList(temp);
            } else if(this.moveRelated(temp)) {
                this.freezeWorkList = LL.<Temp>insertOrdered(this.freezeWorkList, temp);
            } else {
                this.addSimplifyWorkList(temp);
            }
        }
    }

    private boolean moveRelated(Temp temp) {
        return this.nodeMoves(temp) != null;
    }

    private LL<Instr> nodeMoves(Temp temp) {
        return LL.and(this.moveList.getOrDefault(temp, null), LL.or(this.activeMoves, this.workListMoves));
    }

    private void addSimplifyWorkList(Temp temp) {
        this.simplifyWorkList = LL.<Temp>or(this.simplifyWorkList, new LL<Temp>(temp));
    }

    private void addSpilledWorkList(Temp temp) {
        this.spillWorkList = LL.<Temp>or(this.spillWorkList, new LL<Temp>(temp));
    }

    private LL<Temp> adjacent(Temp head) {
        var one = this.adjList.get(head);
        var sortedStack = LL.<Temp>sort(this.stack);
        return LL.<Temp>andNot(one, sortedStack);
    }

    /**
     * Add nodes to the select stack and decrement their adjacent nodes degrees.
     * 
     * Removes item from simplifyWorkList and pushes it onto the stack
     */
    private void simplify() {
        Assert.assertNotNull(this.simplifyWorkList);
        LL<Temp> n = new LL<Temp>(this.simplifyWorkList.head);
        this.simplifyWorkList = LL.<Temp>andNot(this.simplifyWorkList, n);
        this.stack = LL.<Temp>insertRear(this.stack, n.head);
        for (LL<Temp> adjacent = this.adjacent(n.head); adjacent != null; adjacent = adjacent.tail) {
            this.decrementDegree(adjacent.head);
        }
    }

    /**
     * Decrement node head's degree by one. If this node moves from > K to K we
     * enable moves for this node and its adjacent nodes. We then remove this node
     * from the spill work list. If the node is move related, we move it to the
     * freeze worklist, if not, move it to the simplify worklist. This method can be
     * called from either the simplify method or the combine method.
     * 
     * @param head
     */
    private void decrementDegree(Temp head) {
        Integer d = this.degree.get(head);
        Assert.assertNotNull(d);
        this.degree.put(head, d - 1);
        if (d == this.K) {
            this.enableMoves(LL.<Temp>or(new LL<Temp>(head), this.adjacent(head)));
            this.spillWorkList = LL.<Temp>andNot(this.spillWorkList, new LL<Temp>(head));
            this.addSimplifyWorkList(head);
        }
    }

    private void enableMoves(LL<Temp> nodes) {
        for(;nodes != null; nodes = nodes.tail) {
            for(var m = this.nodeMoves(nodes.head); m != null; m = m.tail) {
                if(LL.<Instr>contains(this.activeMoves, m.head)) {
                    this.activeMoves = LL.<Instr>andNot(this.activeMoves, new LL<Instr>(m.head));
                    this.workListMoves = LL.<Instr>or(this.workListMoves, new LL<Instr>(m.head));
                }
            }
        }
    }

    private void coalesce() {
        Instr m = this.workListMoves.head;
        Temp u, v;
        Temp y = m.def().head;
        Temp x = m.use().head;
        x = this.getAlias(x);
        y = this.getAlias(y);
        if(LL.<Temp>contains(this.precoloured, y)) {
            u = y;
            v = x;
        } else {
            u = x;
            v = y;
        }
        this.workListMoves = LL.<Instr>andNot(this.workListMoves, new LL<Instr>(m));
        if(u == v) {
            this.coalescedMoves = LL.<Instr>or(this.coalescedMoves, new LL<Instr>(m));
            this.addWorkList(u);
        } else if(LL.<Temp>contains(this.precoloured, v) || this.inAdjSet(u, v)) {
            this.constrainedMoves = LL.<Instr>or(this.constrainedMoves, new LL<Instr>(m));
            this.addWorkList(u);
            this.addWorkList(v);
        } else if(
            (LL.<Temp>contains(this.precoloured, u) && isOkay(u, v)) ||
            (!LL.<Temp>contains(this.precoloured, u) && conservative(u, v))
        ) {
            this.coalescedMoves = LL.<Instr>or(this.coalescedMoves, new LL<Instr>(m));
            this.combine(u, v);
            this.addWorkList(u);
        } else {
            this.activeMoves = LL.<Instr>or(this.activeMoves, new LL<Instr>(m));
        }
    }

    private void combine(Temp u, Temp v) {
        if(LL.<Temp>contains(this.freezeWorkList, v)) {
            this.freezeWorkList = LL.<Temp>andNot(this.freezeWorkList, new LL<Temp>(v));
        } else {
            this.spillWorkList = LL.<Temp>andNot(this.spillWorkList, new LL<Temp>(v));
        }
        this.coalescedNodes = LL.<Temp>or(this.coalescedNodes, new LL<Temp>(v));
        this.alias.put(v, u);
        this.moveList.put(u, LL.<Instr>or(this.moveList.get(u), this.moveList.get(v)));
        for(LL<Temp> t = this.adjacent(v); t != null; t = t.tail) {
            this.addEdge(t.head, u);
            this.decrementDegree(t.head);
        }
        if(this.degree.get(u) >= this.K && LL.<Temp>contains(this.freezeWorkList, u)) {
            this.freezeWorkList = LL.<Temp>andNot(this.freezeWorkList, new LL<Temp>(u));
            this.spillWorkList = LL.<Temp>or(this.spillWorkList, new LL<Temp>(u));
        }
    }

    private boolean conservative(Temp u, Temp v) {
        int k = 0;
        for(var nodes = LL.<Temp>or(this.adjacent(u), this.adjacent(v)); nodes != null; nodes = nodes.tail) {
            if(this.degree.get(nodes.head) >= this.K){
                k++;
            }
        }
        return k < this.K;
    }

    private boolean isOkay(Temp u, Temp v) {
        boolean result = true; 
        for(var t = this.adjacent(v); t != null; t = t.tail) {
            result &= (
                this.degree.get(t.head) < this.K ||
                LL.<Temp>contains(this.precoloured, t.head) ||
                this.inAdjSet(t.head, u)
                );
        }
        return result;
    }

    private boolean inAdjSet(Temp u, Temp v) {
        return this.adjList.get(u) != null && LL.<Temp>contains(this.adjList.get(u), v);
    }

    private void addWorkList(Temp u) {
        if(
            !LL.<Temp>contains(this.precoloured, u) &&
            !this.moveRelated(u) && 
            this.degree.get(u) < this.K
            ) {
                this.freezeWorkList = LL.<Temp>andNot(this.freezeWorkList, new LL<Temp>(u));
                this.simplifyWorkList = LL.<Temp>or(this.simplifyWorkList, new LL<Temp>(u));
            }
    }

    private Temp getAlias(Temp x) {
        if(LL.<Temp>contains(this.coalescedNodes, x)) {
            return this.getAlias(this.alias.get(x));
        }
        return x;
    }

    private void freeze() {
        Temp u = this.freezeWorkList.head;
        this.freezeWorkList = LL.<Temp>andNot(this.freezeWorkList, new LL<Temp>(u));
        this.simplifyWorkList = LL.<Temp>or(this.simplifyWorkList, new LL<Temp>(u));
        this.freezeMoves(u);
    }

    private void freezeMoves(Temp u) {
        for(var m = this.nodeMoves(u); m != null; m = m.tail) {
            Temp v = null;
            Temp y = m.head.def().head;
            Temp x = m.head.use().head;
            if(this.getAlias(x) == this.getAlias(y)) {
                v = this.getAlias(x);
            } else {
                v = this.getAlias(y);
            }
            this.activeMoves = LL.<Instr>andNot(this.activeMoves, new LL<Instr>(m.head));
            this.frozenMoves = LL.<Instr>or(this.frozenMoves, new LL<Instr>(m.head));
            if(this.nodeMoves(v) == null && this.degree.get(v) < this.K) {
                this.freezeWorkList = LL.<Temp>andNot(this.freezeWorkList, new LL<Temp>(u));
                this.simplifyWorkList = LL.<Temp>or(this.simplifyWorkList, new LL<Temp>(u));
            }
        }
    }

    /**
     * Returns the weight of the temp.
     * 
     * @param temp
     * @return
     */
    private double weight(Temp temp) {
        int originalDegree = LL.<Temp>size(this.adjList.get(temp));
        return (((float)this.defCount.getOrDefault(temp, 0) + this.useCount.getOrDefault(temp, 0))) / originalDegree;
    }
    /**
     * Selects the node to spill from the spill worklist.
     * 
     * @return a Temp
     */
    private Temp nodeToSpill() {
        LL<Temp> potentialSpills = this.spillWorkList;
        Temp maxSpill = null;
        double sp = 0;
        for(LL<Temp> ps = potentialSpills; ps != null; ps = ps.tail) {
            double vsp = weight(ps.head) ;
            if(sp == 0 || vsp < sp) {
                maxSpill = ps.head;
                sp = vsp;
            }
        }
        return maxSpill;
    }

    /**
     * Selects a node to spill. 
     * Removes node from spillWorkList
     * Adds node to simplifyWorkList.
     */
    private void selectSpill() {
        Temp m = this.nodeToSpill();
        this.removeSpillWorkList(m);
        this.addSimplifyWorkList(m);
        this.freezeMoves(m);
    }


    private void removeSpillWorkList(Temp m) {
        this.spillWorkList = LL.<Temp>andNot(this.spillWorkList, new LL<Temp>(m));
    }

    /**
     * Add edge to both adjacent list and bit matrix Keys are non precoloured temps.
     * 
     * @param u the first temp
     * @param v the second temp
     */
    private void addEdge(Temp u, Temp v) {
        if (!this.inAdjSet(u, v) && u != v) {
            if (!LL.<Temp>contains(this.precoloured, u)) {
                this.adjList.put(u, LL.<Temp>or(this.adjList.get(u), new LL<Temp>(v)));
                this.addAdjacentSet(u, v);
                this.degree.put(u, this.degree.getOrDefault(u, 0) + 1);
            }
            if (!LL.<Temp>contains(this.precoloured, v)) {
                this.adjList.put(v, LL.<Temp>or(this.adjList.get(v), new LL<Temp>(u)));
                this.addAdjacentSet(u, v);
                this.degree.put(v, this.degree.getOrDefault(v, 0) + 1);
            }
        }
    }

    private void addAdjacentSet(Temp u, Temp v) {
        LL<Temp> value = addSet.get(u);
        addSet.put(u, LL.<Temp>insertOrdered(value, v));
        LL<Temp> value2 = addSet.get(v);
        addSet.put(v, LL.<Temp>insertOrdered(value2, u));
    }

    private void assignColours() {
        while (this.stack != null) {
            Temp n = LL.<Temp>last(this.stack);
            this.stack = LL.<Temp>removeLast(this.stack);
            TempList okColours = this.frame.registers();
            for (var w = this.adjList.get(n); w != null; w = w.tail) {
                Temp alias = this.getAlias(w.head);
                if (LL.<Temp>contains(LL.<Temp>or(this.colouredNodes, this.precoloured), alias)) {
                    Temp clr = this.colour.get(alias);
                    okColours = TempList.andNot(okColours, new TempList(clr));
                }
            }
            if (okColours == null) {
                this.addSpilledNodes(n);
            } else {
                this.addColouredNodes(n);
                var c = okColours.head;
                this.colour.put(n, c);
            }
        }
        for(var n = this.coalescedNodes; n != null; n = n.tail) {
            this.colour.put(n.head, this.getAlias(n.head));
        }
    }

    private void addColouredNodes(Temp n) {
        this.colouredNodes = LL.<Temp>or(this.colouredNodes, new LL<Temp>(n));
    }

    private void addSpilledNodes(Temp n) {
        this.spilledNodes = LL.<Temp>or(this.spilledNodes, new LL<Temp>(n));
    }

    private void rewrite() {
        LL<Temp> spills = null;
        Hashtable<Temp, Access> accessHash = new Hashtable<Temp, Access>();
        for (LL<Temp> sn = this.spilledNodes; sn != null; sn = sn.tail) {
            Access access = this.frame.allocLocal(true);
            accessHash.put(sn.head, access);
            spills = LL.<Temp>insertOrdered(spills, sn.head);
        }
        Access access = null;
        InstrList newList = null;
        LL<Temp> newTemps = null;
        for (InstrList il = this.instrList; il != null; il = il.tail) {
            LL<Temp> spilledDefs = LL.<Temp>and(def(il.head), spills);
            LL<Temp> spilledUses = LL.<Temp>and(use(il.head), spills);
            if (LL.<Temp>or(spilledDefs, spilledUses) == null) {
                newList = InstrList.append(newList, il.head);
                continue;
            }
            for (; spilledUses != null; spilledUses = spilledUses.tail) {
                access = accessHash.get(spilledUses.head);
                Temp spillTemp = Temp.create();
                this.spillTemps = LL.<Temp>insertOrdered(this.spillTemps, spillTemp);
                newTemps = LL.<Temp>or(newTemps, new LL<Temp>(spillTemp));
                InstrList memoryToTemp = frame.memoryToTemp(spilledUses.head, spillTemp, access);
                newList = InstrList.append(newList, memoryToTemp);
            }
            newList = InstrList.append(newList, il.head);
            for (; spilledDefs != null; spilledDefs = spilledDefs.tail) {
                access = accessHash.get(spilledDefs.head);
                Temp spillTemp = Temp.create();
                newTemps = LL.<Temp>or(newTemps, new LL<Temp>(spillTemp));
                this.spillTemps = LL.<Temp>insertOrdered(this.spillTemps, spillTemp);
                InstrList tempToMemory = frame.tempToMemory(spilledDefs.head, spillTemp, access);
                newList = InstrList.append(newList, tempToMemory);
            }
        }
        this.adjList.clear();
        this.addSet.clear();
        this.degree.clear();
        this.alias.clear();
        this.colour.clear();
        this.defCount.clear();
        this.useCount.clear();;
        this.moveList.clear();
        this.instrList = newList;
        //this.initial = LL.<Temp>or(LL.<Temp>or(colouredNodes, spilledNodes), newTemps);
        //this.initial = LL.<Temp>or(colouredNodes, newTemps);
        this.spilledNodes = null;
        this.coalescedMoves = null;
        this.constrainedMoves = null;
        this.coalescedNodes = null;
        this.colouredNodes = null;
        this.activeMoves = null;
    }

    int maxTries = 0;
    private void main() {
        Assert.assertLE(maxTries++, 6);
        this.initial = null;
        for (InstrList ins = this.instrList; ins != null; ins = ins.tail) {
            this.initial = LL.<Temp>or(this.initial, LL.<Temp>or(use(ins.head), def(ins.head)));
        }
        this.initial = LL.<Temp>andNot(this.initial, this.precoloured);
        this.liveness();
        this.updateUseAndDefCounts();
        this.build();
        this.makeWorklist();
     //   this.checkWorkListInvariants();
        do {
            if (this.simplifyWorkList != null) {
                this.simplify();
            } else if (this.workListMoves != null) {
                this.coalesce();
            } else if (this.freezeWorkList != null) {
                this.freeze();
            } else if (this.spillWorkList != null) {
                this.selectSpill();
            }
        } while (
            this.simplifyWorkList != null 
            ||  this.spillWorkList != null
            || this.workListMoves != null
            || this.freezeWorkList != null
        );
        this.assignColours();
        //this.liveness.dumpLiveness(this.instrList);
        if (this.spilledNodes != null) {
            this.rewrite();
            this.main();
        }
        this.checkGraphColours();
    }


    private void checkWorkListInvariants() {
        for(LL<Temp> degreeInvariant = LL.<Temp>or(this.simplifyWorkList, this.spillWorkList); degreeInvariant != null; degreeInvariant = degreeInvariant.tail) {
            int size = LL.<Temp>size(
                LL.<Temp>and(
                    this.adjList.get(degreeInvariant.head), 
                    LL.<Temp>or(
                        LL.<Temp>or(
                            this.precoloured, 
                            this.simplifyWorkList
                        ), 
                        this.spillWorkList
                    )
                )
            );
            int si = this.degree.get(degreeInvariant.head);
            if(size != si) {
                throw new Error("degree invariant:" + degreeInvariant.head + ":" + size + "!=" + si);
            }
        }
        for(LL<Temp> simplifyInvariant = this.simplifyWorkList; simplifyInvariant != null; simplifyInvariant = simplifyInvariant.tail) {
            var si = this.degree.get(simplifyInvariant.head);
            if(si >= this.K) {
                throw new Error("Simplify Invariant: " + simplifyInvariant.head + ":" + si + ">=" + this.K);
            }

        }
    }

    private void checkGraphColours() {
        for(Temp temp : this.adjList.keySet()) {
            for(LL<Temp> adj = this.adjList.get(temp); adj != null; adj = adj.tail) {
                if(this.tempMap(temp) == this.tempMap(adj.head)) {
                    throw new Error("Graph not correctly coloured ");
                }
            }
        }
    }

    public RegAllocCoalesce(Frame frame, InstrList instrList) {
        this.instrList = instrList;
        this.frame = frame;
        this.K = this.frame.registers().size(); //14
        var pctl = new TempList(IntelFrame.rbp, new TempList(IntelFrame.rsp, this.frame.registers()));
        for(TempList pc = pctl; pc != null; pc = pc.tail) {
            this.precoloured = LL.<Temp>insertOrdered(this.precoloured, pc.head);
            this.colour.put(pc.head, pc.head);
            this.degree.put(pc.head, Integer.MAX_VALUE);
        }
        this.main();
    }

    @Override
    public String tempMap(Temp t) {
		var colour = this.colour.get(t);
		if (colour == null)
			throw new Error("No colour found for " + t);
		return this.frame.tempMap(colour);
	}
}