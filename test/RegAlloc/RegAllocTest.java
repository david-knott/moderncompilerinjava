package RegAlloc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import Graph.Graph;
import Graph.Node;
import Graph.NodeList;

public class RegAllocTest {


    @Test
    public void oneNodesAllPrecoloured() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        NodeList precolouredNodes = new NodeList(a, null);
        NodeList colours = new NodeList(a, null);
        RegisterAllocator registerAllocator = new RegisterAllocator(graph, precolouredNodes, colours);
        registerAllocator.allocate();
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
        RegisterAllocator registerAllocator = new RegisterAllocator(graph, precolouredNodes, colours);
        registerAllocator.allocate();
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
        RegisterAllocator registerAllocator = new RegisterAllocator(graph, precolouredNodes, colours);
        registerAllocator.allocate();
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
        RegisterAllocator registerAllocator = new RegisterAllocator(graph, precolouredNodes, colours);
        registerAllocator.allocate();
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
        RegisterAllocator registerAllocator = new RegisterAllocator(graph, precolouredNodes, colours);
        registerAllocator.allocate();
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
        RegisterAllocator registerAllocator = new RegisterAllocator(graph, precolouredNodes, colours);
        registerAllocator.allocate();
        assertEquals(a, registerAllocator.getNodeColour(a));
        assertEquals(b, registerAllocator.getNodeColour(b));
        assertEquals(a, registerAllocator.getNodeColour(c));
        //expect last node added ( d) to spill
        assertEquals(1, registerAllocator.getSpilledNodes().size());
        assertTrue(registerAllocator.getSpilledNodes().contains(d));
    }
 
 
 
}