package RegAlloc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Stack;

import Assem.Instr;
import Assem.InstrList;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Frame.Access;
import Frame.Frame;
import Graph.GraphvisRenderer;
import Graph.Node;
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
    private TempList spillTemps;
    private InterferenceGraph interferenceGraph;

    private InstructionWorkList workListMoves;
    private InstructionWorkList activeMoves;
    private InstructionWorkList frozenMoves;
    private InstructionWorkList constrainedMoves;

    private Hashtable<Node, InstructionWorkList> moveList = new Hashtable<Node, InstructionWorkList>();
    private Hashtable<Node, NodeWorkList> adjList = new Hashtable<Node, NodeWorkList>();

    private NodeWorkList spilledNodes; //nodes marked for spilling.
    private NodeWorkList colouredNodes; //nodes that have been coloured.
    private NodeWorkList precoloured; //can be created from our list of registers

    private Hashtable<Node, Temp> colour = new Hashtable<Node, Temp>();

    private TempList initial;
    private Hashtable<Node, Integer> degree = new Hashtable<Node, Integer>();
    private int K;

    private NodeWorkList spillWorkList;
    private NodeWorkList freezeWorkList;
    private NodeWorkList simplifyWorkList;
    private NodeWorkList coalescedNodes;
    private Stack<Node> stack = new Stack<Node>();;
    private Hashtable<Node, Node> alias = new Hashtable<Node, Node>();

    private void build() {
        FlowGraph fg = new AssemFlowGraph(instrList);
        interferenceGraph = new IGBackwardControlEdges(fg);
        for(var tl = this.frame.registers(); tl != null; tl = tl.tail) {
            this.precoloured = NodeWorkList.append(this.precoloured, this.interferenceGraph.tnode(tl.head));
        }
        for (NodeList nodeList = fg.nodes(); nodeList != null; nodeList = nodeList.tail) {
            if (fg.isMove(nodeList.head)) {
                workListMoves = InstructionWorkList.or(workListMoves, new InstructionWorkList(fg.instr(nodeList.head)));
                for (TempList uod = TempList.or(fg.instr(nodeList.head).def(),
                        fg.instr(nodeList.head).def()); uod != null; uod = uod.tail) {
                    Node nuod = interferenceGraph.tnode(uod.head);
                    InstructionWorkList instrList = moveList.get(nuod);
                    moveList.put(nuod, InstructionWorkList.or(instrList, new InstructionWorkList(fg.instr(nodeList.head))));
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
            Node node = interferenceGraph.tnode(tempList.head);
            if (degree.get(node) >= K) {
                this.spillWorkList = NodeWorkList.or(this.spillWorkList, new NodeWorkList(node));
            } else if (moveRelated(node)) {
                this.freezeWorkList = NodeWorkList.or(this.freezeWorkList, new NodeWorkList(node));
            } else {
                this.simplifyWorkList = NodeWorkList.or(this.simplifyWorkList, new NodeWorkList(node));
            }
        }
    }

    private InstructionWorkList nodeMoves(Node node) {
        return InstructionWorkList.and(this.moveList.get(node),
                InstructionWorkList.or(this.activeMoves, this.workListMoves));
    }

    private boolean moveRelated(Node node) {
        return nodeMoves(node) == null;
    }

    /**
     * Add nodes to the select stack and decrement their adjacent nodes degrees.
     */
    private void simplify() {
        for (NodeWorkList nodeWorkList = this.simplifyWorkList; nodeWorkList != null; nodeWorkList = nodeWorkList.next) {
            stack.push(nodeWorkList.me);
            for (NodeWorkList adjacent = this.adjacent(nodeWorkList.me); adjacent != null; adjacent = adjacent.next) {
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
    private void decrementDegree(Node head) {
        int d = this.degree.get(head);
        d = d - 1;
        if (d == this.K) {
            this.enableMoves(NodeWorkList.or(new NodeWorkList(head), this.adjacent(head)));
            this.spillWorkList = NodeWorkList.andOr(this.spillWorkList, new NodeWorkList(head));
            if (this.moveRelated(head)) {
                this.freezeWorkList = NodeWorkList.or(this.freezeWorkList, new NodeWorkList(head));
            } else {
                this.simplifyWorkList = NodeWorkList.or(this.simplifyWorkList, new NodeWorkList(head));
            }
        }
    }

    private void enableMoves(NodeWorkList or) {
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

    private Node getAlias(Node node) {
        if (NodeWorkList.contains(this.coalescedNodes, node)) {
            return this.getAlias(this.alias.get(node));
        }
        return node;
    }

    private void coalesce() {
        Instr instr = this.workListMoves.me;
        Node x = this.getAlias(interferenceGraph.tnode(instr.use().head));
        Node y = this.getAlias(interferenceGraph.tnode(instr.def().head));
        Node u, v;
        if (TempList.contains(this.frame.registers(), interferenceGraph.gtemp(y))) {
            u = y;
            v = x;
        } else {
            u = x;
            v = y;
        }
        this.workListMoves = InstructionWorkList.andOr(this.workListMoves, new InstructionWorkList(instr));
        if (u == v) {
            this.coalescedNodes = NodeWorkList.or(this.coalescedNodes, new InstructionWorkList(instr));
            this.addWorkList(u);
        } else if (TempList.contains(this.frame.registers(), interferenceGraph.gtemp(v)) || this.inAdjacentSet(u, v)) {
            this.constrainedMoves = InstructionWorkList.or(this.constrainedMoves, new InstructionWorkList(instr));
            this.addWorkList(u);
            this.addWorkList(v);
        } else if (TempList.contains(this.frame.registers(), interferenceGraph.gtemp(u)) && this.checkOk(v, u)
                || (!TempList.contains(this.frame.registers(), interferenceGraph.gtemp(u))
                        && this.conservative(NodeWorkList.or(this.adjacent(u), this.adjacent(v))))) {
            this.coalescedNodes = NodeWorkList.or(this.coalescedNodes, new InstructionWorkList(instr));
            this.combine(u, v);
            this.addWorkList(u);
        } else {
            this.activeMoves = InstructionWorkList.or(this.activeMoves, new InstructionWorkList(instr));
        }
    }

    private boolean checkOk(Node v, Node u) {
        for (var t = this.adjacent(v); t != null; t = t.next) {
            if (!okay(t.me, u)) {
                return false;
            }
        }
        return true;
    }

    private boolean okay(Node t, Node r) {
        return degree.get(t) < K || NodeWorkList.contains(this.precoloured, t) || this.inAdjSet(t, r);
    }

    private boolean inAdjSet(Node t, Node r) {
        throw new Error("Not impleented");
    }

    private void combine(Node u, Node v) {
        if (NodeWorkList.contains(this.freezeWorkList, v)) {
            this.freezeWorkList = NodeWorkList.andOr(this.freezeWorkList, new NodeWorkList(v));
        } else {
            this.spillWorkList = NodeWorkList.andOr(this.spillWorkList, new NodeWorkList(v));
        }
        this.coalescedNodes = NodeWorkList.or(this.coalescedNodes, new NodeWorkList(v));
        this.alias.put(v, u);
        var uwl = this.moveList.get(u);
        var vwl = this.moveList.get(v);
        this.moveList.put(u, InstructionWorkList.or(uwl, vwl));
        for (NodeWorkList t = this.adjacent(v); t != null; t = t.next) {
            this.addEdge(t.me, u);
            this.decrementDegree(t.me);
        }
        if (degree.get(u) >= this.K && NodeWorkList.contains(this.freezeWorkList, u)) {
            this.freezeWorkList = NodeWorkList.andOr(this.freezeWorkList, new NodeWorkList(u));
            this.spillWorkList = NodeWorkList.andOr(this.spillWorkList, new NodeWorkList(u));
        }
    }

    private void freeze() {
        Node u = this.freezeWorkList.me;
        this.freezeWorkList = NodeWorkList.andOr(this.freezeWorkList, new NodeWorkList(u));
        this.simplifyWorkList = NodeWorkList.or(this.simplifyWorkList, new NodeWorkList(u));
        this.freezeMoves(u);
    }

    private void freezeMoves(Node u) {
        for (var m = this.nodeMoves(u); m != null; m = m.next) {
            Node x = null;
            Node y = null;
            Node v = null;
            if (this.getAlias(y) == this.getAlias(u)) {
                v = this.getAlias(x);
            } else {
                v = this.getAlias(y);
            }
            this.activeMoves = InstructionWorkList.andOr(this.activeMoves, new InstructionWorkList(m.me));
            this.frozenMoves = InstructionWorkList.or(this.frozenMoves, new InstructionWorkList(m.me));
            if (this.nodeMoves(v) == null && this.degree.get(v) < this.K) {
                this.freezeWorkList = NodeWorkList.andOr(this.freezeWorkList, new NodeWorkList(v));
                this.simplifyWorkList = NodeWorkList.or(this.simplifyWorkList, new NodeWorkList(v));
            }
        }
    }

    private void selectSpill() {
        Node m = this.spillWorkList.me;
        this.spillWorkList = NodeWorkList.andOr(this.spillWorkList, new NodeWorkList(m));
        this.simplifyWorkList = NodeWorkList.or(this.simplifyWorkList, new NodeWorkList(m));
        this.freezeMoves(m);
    }

    private void addEdge(Node me, Node u) {
        throw new Error("Not impleented");
    }

    private boolean conservative(NodeWorkList nodeWorkList) {
        int k = 0;
        for (; nodeWorkList != null; nodeWorkList = nodeWorkList.next) {
            if (degree.get(nodeWorkList.me) >= K) {
                k++;
            }
        }
        return k < K;
    }

    private boolean inAdjacentSet(Node u, Node v) {
        throw new Error("Not impleented");
    }

    private void addWorkList(Node u) {
        throw new Error("Not impleented");
    }

    private NodeWorkList adjacent(Node me) {
        throw new Error("Not impleented");
    }

    private void assignColours() {
        while (!this.stack.empty()) {
            Node n = this.stack.pop();
            TempList okColours = this.frame.registers();
            for (var w = this.adjList.get(n); w != null; w = w.next) {
                if (NodeWorkList.contains(NodeWorkList.or(this.colouredNodes, this.precoloured), this.getAlias(w.me))) {
                    okColours = TempList.andNot(okColours, new TempList(this.colour.get(this.getAlias(w.me))));
                }
            }
            if (okColours == null) {
                this.spilledNodes = NodeWorkList.or(this.spilledNodes, n);
            } else {
                this.colouredNodes = NodeWorkList.or(this.colouredNodes, n);
                var c = okColours.head;
                this.colour.put(n, c);
            }
        }
        for(var n = this.coalescedNodes; n != null; n = n.next) {
            this.colour.put(n.me, this.colour.get(this.getAlias(n.me)));
        }
    }

    private void rewrite() {
        // TempList spills = this.selectSpill();
        TempList spills = null;
        InstrList newList = null;
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
                this.spillTemps = TempList.append(this.spillTemps, spillTemp);
                InstrList memoryToTemp = frame.memoryToTemp(spilledUses.head, spillTemp, access);
                newList = InstrList.append(newList, memoryToTemp);
            }
            newList = InstrList.append(newList, instrList.head);
            for (; spilledDefs != null; spilledDefs = spilledDefs.tail) {
                Temp spillTemp = Temp.create();
                Access access = this.frame.allocLocal(false);
                accessHash.put(spilledDefs.head, access);
                this.spillTemps = TempList.append(this.spillTemps, spillTemp);
                InstrList tempToMemory = frame.tempToMemory(spilledDefs.head, spillTemp, access);
                newList = InstrList.append(newList, tempToMemory);
            }
        }
        this.instrList = newList;
    }

    private void main() {
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

    public RegAllocWithCoalescing(Frame frame, InstrList instrList, boolean dumpGraphs /* dump graphs */) {
        this.instrList = instrList;
        this.frame = frame;
        this.K = this.frame.registers().size();
        
        this.main();
    }

    @Override
    public String tempMap(Temp t) {
        return null;
    }
}