package FlowGraph;

import java.util.Hashtable;

import Assem.Instr;
import Assem.LABEL;
import Graph.Node;
import Temp.Label;
import Temp.TempList;

public class AssemFlowGraph extends FlowGraph {

    private Hashtable<Node, Assem.Instr> nodeMap = new Hashtable<Node, Assem.Instr>();
    private Hashtable<Label, Instr> labelInstr = new Hashtable<Label, Instr>();

    public AssemFlowGraph(Assem.InstrList instrs) {
        //add all the labels too a hashtable first
        for (Assem.InstrList p = instrs; p != null; p = p.tail) {
            if(p.head instanceof LABEL){
                var l = ((LABEL)p.head).label;
                labelInstr.put(l, p.head);
            }
        }
        //
        Node prevNode = this.newNode();
        Instr prevInstr = instrs.head;
        nodeMap.put(prevNode, prevInstr);
        instrs = instrs.tail;
        for (Assem.InstrList p = instrs; p != null; p = p.tail) {
            var targets = prevInstr.jumps();
            Node node = null;
            if(targets != null && targets.labels!= null && targets.labels.head != null) {
                node = this.newNode();
                nodeMap.put(node, p.head);
                for (var t = targets.labels; t != null; t = t.tail) {
                    var jnode = this.newNode();
                    var jinstr = labelInstr.get(t.head);
                    nodeMap.put(jnode, jinstr);
                    this.addEdge(node, jnode);
                }
            } else {
                node = this.newNode();
                nodeMap.put(node, p.head);
                this.addEdge(prevNode, node);                
                prevInstr = p.head;
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