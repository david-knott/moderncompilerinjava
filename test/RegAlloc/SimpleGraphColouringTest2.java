package RegAlloc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.Test;

import Graph.Graph;
import Graph.Node;
import Graph.NodeList;
import Intel.IntelFrame;
import Temp.TempList;

public class SimpleGraphColouringTest2 {


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
        PrecolouredNode precolouredNodes= new PrecolouredNode(a, IntelFrame.r10)
        .append(b, IntelFrame.r11)
        .append(c, IntelFrame.r12)
        .append(d, IntelFrame.r13)
        ;
        NodeList initial = new NodeList(c, new NodeList(d, new NodeList(e, null)));
        //a & b are precoloured, except c to have a colour
        SimpleGraphColouring2 registerAllocator = new SimpleGraphColouring2(precolouredNodes, IntelFrame.calleeSaves);
        //execute the allocation..
        registerAllocator.allocate(graph, initial);
        assertTrue(registerAllocator.getSpilledNodes().isEmpty());
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


        PrecolouredNode precolouredNodes= new PrecolouredNode(a, IntelFrame.r10).append(b, IntelFrame.r11);
        NodeList initial = new NodeList(c, new NodeList(d, new NodeList(e, null)));
        TempList colours = new TempList(IntelFrame.r10, new TempList(IntelFrame.r11, null));
        //a & b are precoloured, except c to have a colour
        SimpleGraphColouring2 registerAllocator = new SimpleGraphColouring2(precolouredNodes, colours);
        //set the precoloured nodes temps.
      //  registerAllocator.setNodeColour(c, IntelFrame.r12);
       // registerAllocator.setNodeColour(d, IntelFrame.r13);
        //execute the allocation..
        registerAllocator.allocate(graph, initial);
        assertFalse(registerAllocator.getSpilledNodes().isEmpty());
    }
}

    /*
    @Test
    public void twoNodesAllPrecoloured() {
        Graph graph = new Graph();
        Node a = graph.newNode();
        Node b = graph.newNode();
        graph.addEdge(a, b);
        NodeList precolouredNodes = new NodeList(a, new NodeList(b, null));
        NodeList colours = new NodeList(a, new NodeList(b, null));
        SimpleGraphColouring registerAllocator = new SimpleGraphColouring(precolouredNodes, colours);
        registerAllocator.allocate(graph, null);
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
        registerAllocator.allocate(graph, null);
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
        NodeList initial = new NodeList(c, null);
        //a & b are precoloured, except c to have a colour
        SimpleGraphColouring registerAllocator = new SimpleGraphColouring(precolouredNodes, colours);
        registerAllocator.allocate(graph,initial);
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
        NodeList initial = new NodeList(c, new NodeList(d, null));
        //a & b are precoloured, except c to have a colour
        SimpleGraphColouring registerAllocator = new SimpleGraphColouring(precolouredNodes, colours);
        registerAllocator.allocate(graph, initial);
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
        NodeList initial = new NodeList(c, new NodeList(d, null));
        //a & b are precoloured, except c to have a colour
        SimpleGraphColouring registerAllocator = new SimpleGraphColouring(precolouredNodes, colours);
        registerAllocator.allocate(graph, initial);
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
        NodeList initial = new NodeList(c, new NodeList(d, new NodeList(e, null)));
        //a & b are precoloured, except c to have a colour
        SimpleGraphColouring registerAllocator = new SimpleGraphColouring(precolouredNodes, colours);
        registerAllocator.allocate(graph, initial);
        assertEquals(a, registerAllocator.getNodeColour(a));
        assertEquals(b, registerAllocator.getNodeColour(b));
        assertEquals(3, registerAllocator.getSpilledNodes().size());
        assertTrue(registerAllocator.getSpilledNodes().contains(c));
        assertTrue(registerAllocator.getSpilledNodes().contains(d));
        assertTrue(registerAllocator.getSpilledNodes().contains(e));
    }
*/ 