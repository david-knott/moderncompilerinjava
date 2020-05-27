package RegAlloc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.BitSet;

import org.junit.Test;

import Assem.Instr;
import Assem.InstrList;
import Assem.LABEL;
import Assem.TEST;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Graph.Node;
import Temp.Label;
import Temp.LabelList;
import Temp.Temp;
import Temp.TempList;

public class InterferenceGraphTest {

    @Test
    public void bitSetIntersection() {
        BitSet bitSetA = new BitSet();
        bitSetA.set(1);
        bitSetA.set(2);
        bitSetA.set(3);
        System.out.println(bitSetA);
        BitSet bitSetB = new BitSet();
        bitSetB.set(1);
        bitSetB.set(5);
        bitSetB.set(6);
        bitSetB.set(7);
        System.out.println(bitSetB);
        bitSetB.and(bitSetA);
        System.out.println(bitSetB);
    }

    @Test
    public void bitSetUnion() {
        BitSet bitSetA = new BitSet();
        bitSetA.set(1);
        bitSetA.set(2);
        bitSetA.set(3);
        System.out.println(bitSetA);
        BitSet bitSetB = new BitSet();
        bitSetB.set(1);
        bitSetB.set(5);
        bitSetB.set(6);
        bitSetB.set(7);
        System.out.println(bitSetB);
        bitSetB.or(bitSetA);
        System.out.println(bitSetB);
    }

    @Test
    public void bitSetDifference() {
        BitSet bitSetA = new BitSet();
        bitSetA.set(1);
        bitSetA.set(2);
        bitSetA.set(5);
        System.out.println(bitSetA);
        BitSet bitSetB = new BitSet();
        bitSetB.set(0);
        bitSetB.set(2);
        bitSetB.set(5);
        bitSetB.set(6);
        bitSetB.set(7);
        System.out.println(bitSetB);
        bitSetB.andNot(bitSetA);
        System.out.println(bitSetB);
    }

    @Test
    public void forwardControlEdges() {
        Temp a = Temp.create("rax");
        Temp b = Temp.create("rdx");
        Temp c = Temp.create("rdc");
        Instr instr1 = new TEST(new TempList(a), new TempList(b)); //a is def, b is livein
        Instr instr2 = new TEST(new TempList(b), new TempList(a)); //a is livein
        Instr instr3= new TEST(new TempList(c), new TempList(a)); //a is livein
        InstrList instrList = new InstrList(instr1, new InstrList(instr2, new InstrList(instr3, null)));
        FlowGraph flowGraph = new AssemFlowGraph(instrList);
        IGForwardControlEdges graphv2 = new IGForwardControlEdges(flowGraph);
        graphv2.show(System.out);
        assertNotNull(graphv2);
    }

    @Test
    public void backwardControlEdges() {
        Temp a = Temp.create("rax");
        Temp b = Temp.create("rdx");
        Temp c = Temp.create("rdc");
        Instr instr1 = new TEST(new TempList(a), new TempList(b)); //a is def, b is livein
        Instr instr2 = new TEST(new TempList(b), new TempList(a)); //a is livein
        Instr instr3= new TEST(new TempList(c), new TempList(a)); //a is livein
        InstrList instrList = new InstrList(instr1, new InstrList(instr2, new InstrList(instr3, null)));
        FlowGraph flowGraph = new AssemFlowGraph(instrList);
        IGBackwardControlEdges graphv2 = new IGBackwardControlEdges(flowGraph);
        graphv2.show(System.out);
        assertNotNull(graphv2);
    }

    @Test
    public void forwardComplex() {
        Temp a = Temp.create();
        Temp b = Temp.create();
        Temp c = Temp.create();
        Label l = Label.create();
        Instr instr1 = new TEST(new TempList(a), null);
        Instr labelIn = new LABEL("l", l);
        Instr instr2 = new TEST(new TempList(b), new TempList(a));
        Instr instr3 = new TEST(new TempList(c), new TempList(b, new TempList(c)));
        Instr instr4 = new TEST(new TempList(a), new TempList(b));
        Instr instr5 = new TEST(null, new TempList(a), new LabelList(l, null)); //loops back to 2
        Instr instr6 = new TEST(null, new TempList(c));
        InstrList instrList = new InstrList(instr1, new InstrList(labelIn, new InstrList(instr2, new InstrList(instr3, new InstrList(instr4, new InstrList(instr5, new InstrList(instr6, null)))))));
        FlowGraph flowGraph = new AssemFlowGraph(instrList);
        flowGraph.show(System.out);
        IGForwardControlEdges graphv2 = new IGForwardControlEdges(flowGraph);
        graphv2.show(System.out);
        assertNotNull(graphv2);
    }
 

    @Test
    public void backwardComplex() {
        Temp a = Temp.create();
        Temp b = Temp.create();
        Temp c = Temp.create();
        Label l = Label.create();
        Instr instr1 = new TEST(new TempList(a), null);
        Instr labelIn = new LABEL("l", l);
        Instr instr2 = new TEST(new TempList(b), new TempList(a));
        Instr instr3 = new TEST(new TempList(c), new TempList(b, new TempList(c)));
        Instr instr4 = new TEST(new TempList(a), new TempList(b));
        Instr instr5 = new TEST(null, new TempList(a), new LabelList(l, null)); //loops back to 2
        Instr instr6 = new TEST(null, new TempList(c));
        InstrList instrList = new InstrList(instr1, new InstrList(labelIn, new InstrList(instr2, new InstrList(instr3, new InstrList(instr4, new InstrList(instr5, new InstrList(instr6, null)))))));
        FlowGraph flowGraph = new AssemFlowGraph(instrList);
        IGBackwardControlEdges graphv2 = new IGBackwardControlEdges(flowGraph);
        graphv2.show(System.out);
        assertNotNull(graphv2);
    }
}