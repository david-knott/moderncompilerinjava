package FlowGraph;

import java.util.Hashtable;

import Assem.Instr;
import Assem.LABEL;
import Graph.Node;
import Temp.Label;
import Temp.TempList;

public class AssemFlowGraph extends FlowGraph {

    private Hashtable<Node, Assem.Instr> nodeMap = new Hashtable<Node, Assem.Instr>();
    private Hashtable<Assem.Instr, Node> invNodeMap = new Hashtable<Assem.Instr, Node>();
    private Hashtable<Label, Instr> labelInstr = new Hashtable<Label, Instr>();

    public Node getOrCreate(Instr instr) {
        if(invNodeMap.containsKey(instr)) {
            return invNodeMap.get(instr);
        }
        Node node = this.newNode();
        nodeMap.put(node, instr);
        invNodeMap.put(instr, node);
        return node;
    }

    public AssemFlowGraph() {
        //initialize the structures.
    }

    public AssemFlowGraph(Assem.InstrList instrs) {
        //add all the labels to a hashtable first
        //add the labels to the node map
        for (Assem.InstrList p = instrs; p != null; p = p.tail) {
            if(p.head instanceof LABEL){
                var l = ((LABEL)p.head).label;
                labelInstr.put(l, p.head);
                //this.getOrCreate(p.head);
            }
        }
        Assem.InstrList p = instrs;
        Node prevNode = this.getOrCreate(p.head);
        p = p.tail;
        for (; p != null; p = p.tail) {
            Node node = this.getOrCreate(p.head);
            var prevInstr = instr(prevNode);
            var targets = prevInstr.jumps();
            if(targets != null) {
                for (var t = targets.labels; t != null; t = t.tail) {
                    var jinstr = labelInstr.get(t.head);
                    Node jNode = this.getOrCreate(jinstr);
                    this.addEdge(prevNode, jNode);
                }
            } else {
                this.addEdge(prevNode, node);                
            }
            prevNode = node;
        }
    }

    public Assem.Instr instr(Node node) {
        return nodeMap.get(node);
    }

    @Override
    public TempList def(Node node) {
        return instr(node).def();
    }

    @Override
    public TempList use(Node node) {
        return instr(node).use();
    }

    @Override
    public boolean isMove(Node node) {
        return instr(node) instanceof Assem.MOVE;
    }

}