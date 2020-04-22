package RegAlloc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import Graph.Graph;
import Graph.Node;
import Graph.NodeList;

public class SimpleGraphColouringTest {


    @Test
    public void oneNodesAllPrecoloured() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        NodeList precolouredNodes = new NodeList(a, null);
        NodeList colours = new NodeList(a, null);
        SimpleGraphColouring registerAllocator = new SimpleGraphColouring(precolouredNodes, colours);
        registerAllocator.allocate(graph);
        assertEquals(a, registerAllocator.getNodeColour(a));
    }

    @Test
    public void twoNodesAllPrecoloured() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        graph.addEdge(a, b);
        NodeList precolouredNodes = new NodeList(a, new NodeList(b, null));
        NodeList colours = new NodeList(a, new NodeList(b, null));
        SimpleGraphColouring registerAllocator = new SimpleGraphColouring(precolouredNodes, colours);
        registerAllocator.allocate(graph);
        assertEquals(a, registerAllocator.getNodeColour(a));
        assertEquals(b, registerAllocator.getNodeColour(b));
    }
 
    @Test
    public void threeNodesAllPrecoloured() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        Node c = graph.newNode();
        //a interferes with b & c
        graph.addEdge(a, b);
        graph.addEdge(a, c);
        NodeList precolouredNodes = new NodeList(a, new NodeList(b, new NodeList(c, null)));
        NodeList colours = new NodeList(a, new NodeList(b, new NodeList(c, null)));
        SimpleGraphColouring registerAllocator = new SimpleGraphColouring(precolouredNodes, colours);
        registerAllocator.allocate(graph);
        assertEquals(a, registerAllocator.getNodeColour(a));
        assertEquals(b, registerAllocator.getNodeColour(b));
        assertEquals(c, registerAllocator.getNodeColour(c));
    }

    @Test
    public void threeNodesTwoPrecoloured() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        Node c = graph.newNode();
        //a interferes with b & c
        graph.addEdge(a, b);
        graph.addEdge(a, c);
        NodeList precolouredNodes = new NodeList(a, new NodeList(b, null));
        NodeList colours = new NodeList(a, new NodeList(b, null));
        //a & b are precoloured, except c to have a colour
        SimpleGraphColouring registerAllocator = new SimpleGraphColouring(precolouredNodes, colours);
        registerAllocator.allocate(graph);
        assertEquals(a, registerAllocator.getNodeColour(a));
        assertEquals(b, registerAllocator.getNodeColour(b));
        assertEquals(b, registerAllocator.getNodeColour(c));
    }

    @Test
    public void fourNodesTwoPrecoloured() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        Node c = graph.newNode();
        Node d = graph.newNode();
        //a interferes with b & c
        graph.addEdge(a, b);
        graph.addEdge(a, c);
        graph.addEdge(a, d);
        NodeList precolouredNodes = new NodeList(a, new NodeList(b, null));
        NodeList colours = new NodeList(a, new NodeList(b, null));
        //a & b are precoloured, except c to have a colour
        SimpleGraphColouring registerAllocator = new SimpleGraphColouring(precolouredNodes, colours);
        registerAllocator.allocate(graph);
        assertEquals(a, registerAllocator.getNodeColour(a));
        assertEquals(b, registerAllocator.getNodeColour(b));
        assertEquals(b, registerAllocator.getNodeColour(c));
        assertEquals(b, registerAllocator.getNodeColour(d));
    }

    @Test
    public void fourNodesTwoPrecolouredWthSpill() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        Node c = graph.newNode();
        Node d = graph.newNode();
        //a interferes with b & c
        graph.addEdge(a, b);
        graph.addEdge(a, d);
        graph.addEdge(b, c);
        graph.addEdge(c, d);
        graph.addEdge(b, d);
        NodeList precolouredNodes = new NodeList(a, new NodeList(b, null));
        NodeList colours = new NodeList(a, new NodeList(b, null));
        //a & b are precoloured, except c to have a colour
        SimpleGraphColouring registerAllocator = new SimpleGraphColouring(precolouredNodes, colours);
        registerAllocator.allocate(graph);
        assertEquals(a, registerAllocator.getNodeColour(a));
        assertEquals(b, registerAllocator.getNodeColour(b));
        assertEquals(a, registerAllocator.getNodeColour(c));
        //expect last node added ( d) to spill
        assertEquals(1, registerAllocator.getSpilledNodes().size());
        assertTrue(registerAllocator.getSpilledNodes().contains(d));
    }
 
    @Test
    public void fiveNodesTwoPrecolouredWthSpills() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        Node c = graph.newNode();
        Node d = graph.newNode();
        Node e = graph.newNode();
        //a and b interfere with c,d & e
        graph.addEdge(a, b);
        graph.addEdge(a, c);
        graph.addEdge(a, d);
        graph.addEdge(a, e);
        graph.addEdge(b, c);
        graph.addEdge(b, d);
        graph.addEdge(b, e);
        graph.addEdge(c, d);
        graph.addEdge(c, e);
        graph.addEdge(d, e);
        NodeList precolouredNodes = new NodeList(a, new NodeList(b, null));
        NodeList colours = new NodeList(a, new NodeList(b, null));
        //a & b are precoloured, except c to have a colour
        SimpleGraphColouring registerAllocator = new SimpleGraphColouring(precolouredNodes, colours);
        registerAllocator.allocate(graph);
        assertEquals(a, registerAllocator.getNodeColour(a));
        assertEquals(b, registerAllocator.getNodeColour(b));
        assertEquals(3, registerAllocator.getSpilledNodes().size());
        assertTrue(registerAllocator.getSpilledNodes().contains(c));
        assertTrue(registerAllocator.getSpilledNodes().contains(d));
        assertTrue(registerAllocator.getSpilledNodes().contains(e));
    }
 
    @Test
    public void fiveNodesThreePrecoloured() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        Node c = graph.newNode();
        Node d = graph.newNode();
        Node e = graph.newNode();
        //a and b interfere with c,d & e
        graph.addEdge(a, b); //{ a connects to b,c,d,e }
        graph.addEdge(a, c);
        graph.addEdge(a, d);
        graph.addEdge(a, e);
        graph.addEdge(b, c);// {b connects to c,d,e }
        graph.addEdge(b, d);
        graph.addEdge(b, e);
        graph.addEdge(c, d);//{c connects to d, e }
        graph.addEdge(c, e);
     //   graph.addEdge(d, e);
        NodeList precolouredNodes = new NodeList(a, new NodeList(b, new NodeList(c, new NodeList(d, null))));
        NodeList colours = new NodeList(a, new NodeList(b, new NodeList(c, new NodeList(d, null))));
        //a & b are precoloured, except c to have a colour
        SimpleGraphColouring registerAllocator = new SimpleGraphColouring(precolouredNodes, colours);
        registerAllocator.allocate(graph);
        assertEquals(a, registerAllocator.getNodeColour(a));
        assertEquals(b, registerAllocator.getNodeColour(b));
        assertEquals(c, registerAllocator.getNodeColour(c));
        assertEquals(d, registerAllocator.getNodeColour(d));
        assertEquals(0, registerAllocator.getSpilledNodes().size());
    }
}