package FlowGraph;

import java.util.Hashtable;

import Assem.Instr;
import Assem.LABEL;
import Graph.Node;
import Temp.Label;
import Temp.TempList;

public class AssemFlowGraph extends FlowGraph {

    private Hashtable<Node, Assem.Instr> nodeMap = new Hashtable<Node, Assem.Instr>();
    private Hashtable<Label, Node> labels= new Hashtable<Label, Node>();

    public AssemFlowGraph(Assem.InstrList instrs) {
        //get labels first so they are present 
        for (Assem.InstrList p = instrs; p != null; p = p.tail) {
            if(p.head instanceof LABEL) {
                //create new node for label
                var node = this.newNode();
                nodeMap.put(node, instrs.head);
                var label = (LABEL)p.head;
                //insert into labels hash
                labels.put(label.label, node);
            }
        }
        Node previous = this.newNode();
        nodeMap.put(previous, instrs.head);
        instrs = instrs.tail;
        for (Assem.InstrList p = instrs; p != null; p = p.tail) {
            //its a label, do we need to
            //create a link for it ?
            var node = this.newNode();
            nodeMap.put(node, p.head);
            var targets = p.head.jumps();
            if (targets == null) {
                this.addEdge(previous, node);                
                previous = node;
            } else {
                this.addEdge(previous, node);                
                previous = node;
                for (var t = targets.labels; t != null; t = t.tail) {
                    var label = t.head;
                    Node branch = labels.get(label);
                    this.addEdge(previous, branch);
                }
            }
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