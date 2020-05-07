package RegAlloc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import Assem.Instr;
import Assem.InstrList;
import Assem.LABEL;
import Assem.TEST;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Graph.Node;
import Graph.NodeList;
import Temp.Label;
import Temp.LabelList;
import Temp.Temp;
import Temp.TempList;

public class LiveOutTest {

    /**
     * a <- b ( a dest, b src )
     * b <- a ( b dest, a src )
     * a should be live out
     */
    @Test
    public void liveOut2Vars() {

        Temp a = new Temp();
        Temp b = new Temp();
        Instr instr1 = new TEST(new TempList(a), new TempList(b));
        Instr instr2 = new TEST(new TempList(b), new TempList(a));
        InstrList instrList = new InstrList(instr1, new InstrList(instr2, null));
        FlowGraph flowGraph = new AssemFlowGraph(instrList);
        LiveOut liveOut = new LiveOut(flowGraph);
        Node node = flowGraph.nodes().head;
        flowGraph.show(System.out);
        assertTrue(node != null);
        var lo1 = liveOut.liveOut(node);
        assertEquals(a, lo1.head);
    }
 
    /**
     * a <- b ( a dest, b src )
     * b <- a ( b dest, a src )
     * c <- b ( c dest, a src )
     * a should be live at 0,
     * b should be live out at 1
     */
    @Test
    public void liveOut3Vars() {
        Temp a = new Temp();
        Temp b = new Temp();
        Temp c = new Temp();
        Instr instr1 = new TEST(new TempList(a), new TempList(b));
        Instr instr2 = new TEST(new TempList(b), new TempList(a));
        Instr instr3 = new TEST(new TempList(c), new TempList(b));
        Instr instr4 = new TEST();
        InstrList instrList = new InstrList(instr1, new InstrList(instr2, new InstrList(instr3, new InstrList(instr4, null))));
        FlowGraph flowGraph = new AssemFlowGraph(instrList);
        LiveOut liveOut = new LiveOut(flowGraph);
        flowGraph.show(System.out);
        NodeList nodeList = flowGraph.nodes();
        assertEquals(a, liveOut.liveOut(nodeList.head).head );
        assertEquals(b, liveOut.liveOut(nodeList.tail.head).head );

    }

    @Test
    public void liveOutLoop() {
        Temp a = new Temp();
        Temp b = new Temp();
        Temp c = new Temp();
        Label l = new Label();
        Instr instr1 = new TEST(new TempList(a), null);
        Instr labelIn = new LABEL("l", l);
        Instr instr2 = new TEST(new TempList(b), new TempList(a));
        Instr instr3 = new TEST(new TempList(c), new TempList(b, new TempList(c)));
        Instr instr4 = new TEST(new TempList(a), new TempList(b));
        Instr instr5 = new TEST(null, new TempList(a), new LabelList(l, null)); //loops back to 2
        Instr instr6 = new TEST(null, new TempList(c));
        InstrList instrList = new InstrList(instr1, new InstrList(labelIn, new InstrList(instr2, new InstrList(instr3, new InstrList(instr4, new InstrList(instr5, new InstrList(instr6, null)))))));
        FlowGraph flowGraph = new AssemFlowGraph(instrList);
        LiveOut liveOut = new LiveOut(flowGraph);
        flowGraph.show(System.out);
        NodeList nodeList = flowGraph.nodes();
        assertEquals(7, nodeList.size());
        Node first = nodeList.head;
        assertEquals(2, liveOut.liveOut(first).size());
        assertTrue(liveOut.liveOut(first).contains(a));
        assertTrue(liveOut.liveOut(first).contains(c));
        Node second = nodeList.tail.head; //second node is label, so same live out as first
        assertEquals(2, liveOut.liveOut(second).size());
        assertTrue(liveOut.liveOut(second).contains(a));
        assertTrue(liveOut.liveOut(second).contains(c));
        Node third = nodeList.tail.tail.head; 
        assertEquals(2, liveOut.liveOut(third).size());
        assertTrue(liveOut.liveOut(third).contains(b));
        assertTrue(liveOut.liveOut(third).contains(c));
        Node fourth = nodeList.tail.tail.tail.head; 
        assertEquals(2, liveOut.liveOut(fourth).size());
        assertTrue(liveOut.liveOut(fourth).contains(b));
        assertTrue(liveOut.liveOut(fourth).contains(c));
        Node fifth = nodeList.tail.tail.tail.tail.head; 
        assertEquals(2, liveOut.liveOut(fifth).size());
        assertTrue(liveOut.liveOut(fifth).contains(a));
        assertTrue(liveOut.liveOut(fifth).contains(c));
        Node sixth = nodeList.tail.tail.tail.tail.tail.head; 
        assertEquals(2, liveOut.liveOut(sixth).size());
        assertTrue(liveOut.liveOut(sixth).contains(a));
        assertTrue(liveOut.liveOut(sixth).contains(c));
    }
}