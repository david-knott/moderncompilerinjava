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
import Helpers.TestFrame;
import Temp.Label;
import Temp.LabelList;
import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;
import Util.GenericLinkedList;

public class ColourTest {
    @Test
    public void oneColour() {
        TempList precoloured = TempList.create(new Temp[]{Temp.create("r1"), Temp.create("r2")});
        TempList registers = TempList.create(new Temp[]{Temp.create("r1"), Temp.create("r2")});
        TestFrame testFrame = new TestFrame(precoloured, registers);
        boolean dumpGraphs = false;
        Temp src = Temp.create();
        Temp dst = Temp.create();
        InstrList instrList = new InstrList(new TEST(new TempList(dst), new TempList(src)),
                new InstrList(new TEST(new TempList(dst), new TempList(src)), null));
        RegAlloc alloc = new RegAlloc(testFrame, instrList, dumpGraphs);
        assertNotNull(alloc.tempMap(src));
        assertNotNull(alloc.tempMap(dst));
    }
}