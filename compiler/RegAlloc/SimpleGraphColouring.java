package RegAlloc;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Stack;

import Graph.Graph;
import Graph.Node;
import Graph.NodeList;
import Temp.Temp;
import Temp.TempMap;

/**
 * Register allocator class.
 */
public class SimpleGraphColouring implements TempMap {

    private NodeList precoloured;
    private NodeList colours;
    private Set<Node> colouredNodes = new HashSet<Node>();
    private Set<Node> spilledNodes = new HashSet<Node>();
    private Stack<Node> simpleStack = new Stack<Node>();
    private Hashtable<Node, Integer> degrees = new Hashtable<Node, Integer>();
    private Hashtable<Node, Node> nodeColours = new Hashtable<Node, Node>();

    public SimpleGraphColouring(NodeList precoloured, NodeList colours) {
        this.precoloured = precoloured;
        //add precoloured nodes to our colouredNodes set
        for(var pc = precoloured; pc != null; pc = pc.tail) {
            this.setNodeColour(pc.head, pc.head);
        }
        this.colours = colours;
    }

    /**
     * Gets all the nodes that already have been assigned a colour.
     * 
     * @return
     */
    Set<Node> getPrecolouredNodes() {
        Set<Node> s = new HashSet<Node>();
        for(var nodes = this.precoloured; nodes != null; nodes = nodes.tail) {
            s.add(nodes.head);
        }
        return s;
    }

    /**
     * Gets all the nodes that have not yet been assigned a colour.
     * 
     * @return
     */
    Set<Node> getInitialNodes(Graph graph) {
        Set<Node> s = new HashSet<Node>();
        for(var nodes = graph.nodes(); nodes != null; nodes = nodes.tail) {
            s.add(nodes.head);
        }
        return s;
    }

    /**
     * Gets all the colours for this graaph.
     * 
     * @return
     */
    Set<Node> getColours() {
        Set<Node> s = new HashSet<Node>();
        for(var nodes = this.colours; nodes != null; nodes = nodes.tail) {
            s.add(nodes.head);
        }
        return s;
    }

    /**
     * Returns all the nodes that have been coloured so far.
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
    Node getNodeColour(Node node) {
        if(this.nodeColours.containsKey(node)){
            return this.nodeColours.get(node);
        } else {
            throw new Error("Node " + node + " does not have a colour.");
        }
    }

    void setNodeColour(Node node, Node colour) {
        this.nodeColours.put(node, colour);
    }

    /**
     * Runs the allocation algorithm.
     */
    public void allocate(Graph graph) {
        // store node degrees in a hash table
        for (var nodes = graph.nodes(); nodes != null; nodes = nodes.tail) {
            degrees.put(nodes.head, nodes.head.degree());
        }
        // Adds nodes to stack and decrement their adjcacent node degrees
        var simplifyWorklist = this.getInitialNodes(graph);
        //remove precoloured items from simplify step
        simplifyWorklist.removeIf(x -> this.getPrecolouredNodes().contains(x));
        while(!simplifyWorklist.isEmpty()) {
            var node = simplifyWorklist.iterator().next();
            simplifyWorklist.remove(node);
            this.simpleStack.push(node);
            for (var adj = node.adj(); adj != null; adj = adj.tail) {
                var degree = degrees.get(adj.head) - 1;
                degrees.put(adj.head, degree);
            }
        }
        assignColours();
    }

    private void assignColours() {
        // assign colours to the gragh
        while (!this.simpleStack.isEmpty()) {
            var node = this.simpleStack.pop();
            var okColours = new HashSet<Node>(this.getColours());
            // get adjacent nodes, check their colours and remove from ok list
            for (var adj = node.adj(); adj != null; adj = adj.tail) {
                // check if adjacent node is already coloured
                // or is in the precoloured list, if it is
                // remove it from available colours.
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
        // return this.frame.tempMap(t);
        return null;
    }
}