package RegAlloc;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Stack;

import Assem.Instr;
import Assem.InstrList;
import Assem.MOVE;
import Assem.OPER;
import Frame.Frame;
import Graph.Graph;
import Graph.Node;
import Graph.NodeList;
import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;


class TempRewriter {

    private TempList tempList;
    private Frame frame;

    public TempRewriter(TempList tempList, Frame frame){
        this.tempList = tempList;
        this.frame = frame;
    }

    private boolean spilled(Temp temp) {
        return true;
    }

    private Instr tempToMemory(InstrList instr, Temp temp) {
        return new OPER("", null, new TempList(temp, null)); 
    }

    private Instr memoryToTemp(InstrList instr, Temp temp) {
        return new OPER("", new TempList(temp, null), null); 
    }

    public InstrList rewrite(InstrList instrList) {

        InstrList result = new InstrList(instrList.head, null);
        for(InstrList loop = instrList; loop != null; loop = loop.tail) {
            Instr instr = loop.head;
            //foreach instance of a use, move from memory location to new temp
            for(TempList uses = instr.use(); uses != null; uses = uses.tail) {
                Temp use = uses.head;
                if(this.spilled(use)) {
                    result.append(this.memoryToTemp(loop, use));
                }
            }
            //add the instruction
            result.append(instr);
            //foreach instance of def, move the temp to a memory location
            for(TempList defs = instr.def(); defs != null; defs = defs.tail) {
                Temp def = defs.head;
                if(this.spilled(def)) {
                    result.append(this.tempToMemory(loop, def));
                }
            }
        }
        return result;

    }

}

/**
 * Register allocator class.
 */
public class SimpleGraphColouring2 implements TempMap {

    //strictly speaking these are temps.
    private PrecolouredNode precoloured;
    private TempList colours;
    private Set<Node> colouredNodes = new HashSet<Node>();
    private Set<Node> spilledNodes = new HashSet<Node>();
    private Stack<Node> simpleStack = new Stack<Node>();
    private Hashtable<Node, Integer> degrees = new Hashtable<Node, Integer>();
    private Hashtable<Node, Temp> nodeColours = new Hashtable<Node, Temp>();

    public SimpleGraphColouring2(PrecolouredNode precoloured, TempList colours) {
        this.colours = colours;
        this.precoloured = precoloured;
        for(var nodes = this.precoloured; nodes != null; nodes = nodes.tail) {
            this.setNodeColour(nodes.node, nodes.temp);
        }
    }

    /**
     * Gets all the nodes that already have been assigned a colour.
     * 
     * @return
     */
    Set<Node> getPrecolouredNodes() {
        Set<Node> s = new HashSet<Node>();
        for(var nodes = this.precoloured; nodes != null; nodes = nodes.tail) {
            s.add(nodes.node);
        }
        return s;
    }

    /**
     * Gets all the colours for this graaph. The colours are nodes which are already
     * assigned to particular registers. The nodes in the colours set are a subset
     * of the nodes in the precoloured set. The reason for this is that some precoloured
     * nodes cannot be used in the register allocation, such as stack and frame pointer.
     * 
     * @return
     */
    Set<Temp> getColours() {
        Set<Temp> s = new HashSet<Temp>();
        for(var nodes = this.colours; nodes != null; nodes = nodes.tail) {
            s.add(nodes.head);
        }
        return s;
    }

    /**
     * Returns all the nodes that have been coloured so far.
     * It does not contain the related colours for each node.
     * 
     * @return
     */
    Set<Node> getColouredNodes() {
        return this.colouredNodes;
    }

    
    /**
     * Returns all the nodes that have been spilled. The caller
     * can then modify the instruction code, regenerate the 
     * interference graph and rerun the register allocation.
     * 
     * @return
     */
    Set<Node> getSpilledNodes() {
        return this.spilledNodes;
    }

    /**
     * Returns a count of all the nodes in the graph.
     * @return
     */
    int nodeCount(Graph graph) {
        int count = 0;
        for (var nodes = graph.nodes(); nodes != null; nodes = nodes.tail) {
            count++;
        }
        return count;
    }

    /**
     * Returns the degree of node
     * @param node
     * @return
     */
    int degree(Node node) {
        return this.degrees.get(node);
    }

    /**
     * Returns the colour of a node. If the node
     * does not have a colour, an exception is thrown.
     * The nodes colour is a reference to a precoloured
     * node.
     * @param node
     * @return
     */
    Temp getNodeColour(Node node) {
        if(this.nodeColours.containsKey(node)){
            return this.nodeColours.get(node);
        } else {
            throw new Error("Node " + node + " does not have a colour.");
        }
    }

    /**
     * Set a nodes colour. This is used to set both precoloured
     * node colours and the during graph colouring operations.
     * @param node
     * @param colour
     */
    private void setNodeColour(Node node, Temp colour) {
        this.nodeColours.put(node, colour);
    }

    /**
     * 
     * @param graph the graph
     * @param initialNodes linked list of nodes that are not precoloured.
     */
    public void allocate(Graph graph, NodeList initialNodes) {
        // store node degrees in a hash table
        for (var nodes = graph.nodes(); nodes != null; nodes = nodes.tail) {
            degrees.put(nodes.head, nodes.head.degree());
        }
        while(initialNodes != null) {
            var node = initialNodes.head;
            this.simpleStack.push(initialNodes.head);
            for (var adj = node.adj(); adj != null; adj = adj.tail) {
                var degree = degrees.get(adj.head) - 1;
                degrees.put(adj.head, degree);
            }
            initialNodes = initialNodes.tail;
        }
        assignColours();
    }

    private void assignColours() {
        // assign colours to the gragh
        while (!this.simpleStack.isEmpty()) {
            var node = this.simpleStack.pop();
            var okColours = new HashSet<Temp>(this.getColours());
            // get adjacent nodes, check their colours and remove from ok list
            for (var adj = node.adj(); adj != null; adj = adj.tail) {
                // check if adjacent node is already coloured
                // or is in the precoloured list, if it is
                // remove it from available colours.
                // we need to know the precoloured nodes colour
                if (this.getColouredNodes().contains(adj.head) || this.getPrecolouredNodes().contains(adj.head)) {
                    var colorToRemove = this.getNodeColour(adj.head);
                    System.out.println(" removing colour " + colorToRemove + " from list.");
                    okColours.remove(colorToRemove);
                }
            }
            // try to assign a colour to node
            if(okColours.isEmpty()) {
                System.out.println("Spilling node " + node);
                this.spilledNodes.add(node);
            } else {
                var colour = okColours.iterator().next();
                this.setNodeColour(node, colour);
                System.out.println("Assigning colour " + colour + " to node " + node);
            }
        }
    }

    @Override
    public String tempMap(Temp t) {
        //for given temp t, what node does it have, what temp does that node map to ?
        return null;
    }
}