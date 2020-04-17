package RegAlloc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import Graph.Node;
import Graph.NodeList;
import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;

class Colour implements TempMap {
    InterferenceGraph ig;
    TempMap initial;
    TempList registers;
    RegAllocImpl regAllocImpl;

    Hashtable<Node, Temp> color = new Hashtable<Node, Temp>();
    
    private Collection<Temp> linkedListToCollection(TempList tempList) {
        List<Temp> list = new ArrayList<Temp>();
        for(;tempList != null; tempList = tempList.tail) {
            list.add(tempList.head);
        }
        return list;
    }
    /**
     * Constructor for a Colour. This class implements the TempMap
     * interface, which provides a TempMap that contains all the
     * colour for the programs temporaries. 
     * @param ig the temporary inteference graph for the program
     * @param initial a TempMap that contains the pre coloured registers, provided by frame
     * @param registers the list of all the machine registers
     */
    public Colour(InterferenceGraph ig, TempMap initial, TempList registers) {
        if (ig == null)
            throw new Error("ig cannot be null");
        if (initial == null)
            throw new Error("initial cannot be null");
        if (registers == null)
            throw new Error("register cannot be null");
        this.ig = ig;
        this.initial = initial;
        this.registers = registers;
        this.regAllocImpl = new RegAllocImpl();
        //data structures to hold colouring data
        Hashtable<Node, Integer> degree = new Hashtable<Node, Integer>();
        Set<Node> simplifyWorklist = new HashSet<Node>();
        Set<Node> coloured = new HashSet<Node>();
        Set<Node> precoloured = new HashSet<Node>();
        Stack<Node> selectStack = new Stack<Node>();
        //add interfering nodes to simply work list
        for(NodeList nl = ig.nodes(); nl != null; nl = nl.tail) {
            simplifyWorklist.add(nl.head);
            degree.put(nl.head, nl.head.degree());
        }
        //simplify graph by pushing onto the stack
        while(!simplifyWorklist.isEmpty()) {
            var node = simplifyWorklist.iterator().next();
            simplifyWorklist.remove(node);
            selectStack.push(node);
            for(NodeList anl = node.adj(); anl != null; anl = anl.tail) {
                var d = degree.get(anl.head) - 1;
                degree.put(anl.head, d);
                if(d == 3) {
                    simplifyWorklist.add(anl.head);
                }
            }
        }
        //assign colours
        while(!selectStack.empty()) {
            Node n = selectStack.pop();
            var okColours = new HashSet<Temp>();
            okColours.addAll(this.linkedListToCollection(this.registers));
            for(NodeList w = n.adj(); w != null; w = w.tail) {
                //node w is in coloured set, remove its assigned color from okColours set
                if(coloured.contains(w.head) || precoloured.contains(w.head)) {
                    okColours.remove(color.get(w.head));
                }
            }
            if(okColours.isEmpty()) {
                //add spilled node
                throw new Error("Unable to spill");
            } else {
                coloured.add(n);
                var nc = okColours.iterator().next();
                color.put(n, nc);
            }
        }


        System.out.println("");

    }

    public TempList spills() {
        return null;
    }

    @Override
    public String tempMap(Temp t) {
        //eturn new CombineMap(this.initial, this).tempMap(t);
        var node = this.ig.tnode(t);
        if(node != null && this.color.containsKey(node)) {
      //      return "ta" + this.color.get(node).toString();
        }
        return this.initial.tempMap(t);
    }
}