package RegAlloc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import Helpers.TempMapHelper;
import Helpers.TestFrame;
import Temp.Label;
import Temp.LabelList;
import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;
import Util.GenericLinkedList;



public class RegAllocTest {

    @Test
     public void simple() {
        Temp r1 = Temp.create("rax");
        Temp r2 = Temp.create("rdx");
        Temp r3 = Temp.create("rdi");
        Temp t1 = Temp.create("t1");
        Temp t2 = Temp.create("t2");
        Temp t3 = Temp.create("t3");
        Temp t4 = Temp.create("t4");
        TempList initial = new TempList(t1, new TempList(t2, new TempList(t3, new TempList(t4))));
        TempList precoloured = TempList.create(new Temp[] { r1, r2, r3 });
        TempList registers = TempList.create(new Temp[] { r1, r2});
        TestFrame testFrame = new TestFrame(precoloured, registers);
        InstrList instrList = new InstrList(new TEST(new TempList(t1, new TempList(r3)), new TempList(t2)),
                new InstrList(new TEST(new TempList(t3), new TempList(t4)),
                        new InstrList(new TEST(new TempList(t2), new TempList(t3)),
                                new InstrList(new TEST(new TempList(t4), new TempList(t1)),
                                        new InstrList(new TEST(new TempList(t2), new TempList(t3)),
                                                new InstrList(new TEST(new TempList(t4), new TempList(t2)), null))))));
        var fg = new AssemFlowGraph(instrList);
        RegAlloc alloc = new RegAlloc(testFrame, instrList, true);

    }
}