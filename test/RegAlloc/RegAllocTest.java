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
    public void empty() {
    }

    @Test
    public void noSpills() {
        Temp r0 = Temp.create("rbp");
        Temp r1 = Temp.create("rax");
        Temp r2 = Temp.create("rdx");
        Temp r3 = Temp.create("rdi");
        Temp r4 = Temp.create("r10");
        Temp t1 = Temp.create("t1");
        Temp t2 = Temp.create("t2");
        Temp t3 = Temp.create("t3");
        Temp t4 = Temp.create("t4");
        TempList precoloured = TempList.create(new Temp[] { r0, r1, r2, r3, r4 });
<<<<<<< HEAD
        TempList registers = TempList.create(new Temp[] { r1, r2, r3   });
=======
        TempList registers = TempList.create(new Temp[] { r1, r2, r3  });
>>>>>>> a3b980be0951f09261be07de1591e28bdbc37970
        TestFrame testFrame = new TestFrame(precoloured, registers);
        InstrList instrList = new InstrList(new TEST(new TempList(t1, new TempList(r3)), new TempList(t2)),
                new InstrList(new TEST(new TempList(t3), new TempList(t4)),
                        new InstrList(new TEST(new TempList(t2), new TempList(t3)),
                                new InstrList(new TEST(new TempList(t4), new TempList(t1)),
                                        new InstrList(new TEST(new TempList(t2), new TempList(t3)),
                                                new InstrList(new TEST(new TempList(t4), new TempList(t2)), null))))));
        RegAlloc alloc = new RegAlloc(testFrame, instrList, true);
        assertEquals(1, alloc.iterations, "No spills means only one iteration");
    }

    @Test
    public void loop() {

        Label l = Label.create();
        Temp a = Temp.create("rbp");
        Temp b = Temp.create("rax");
        Temp c = Temp.create("rdx");
        Instr instr1 = new TEST(new TempList(a), null);
        Instr labelIn = new LABEL("l", l);
        Instr instr2 = new TEST(new TempList(b), new TempList(a));
        Instr instr3 = new TEST(new TempList(c), new TempList(b, new TempList(c)));
        Instr instr4 = new TEST(new TempList(a), new TempList(b));
        Instr instr5 = new TEST(null, new TempList(a), new LabelList(l, null)); // loops back to 2
        Instr instr6 = new TEST(null, new TempList(c));
        InstrList instrList = new InstrList(instr1, new InstrList(labelIn, new InstrList(instr2,
                new InstrList(instr3, new InstrList(instr4, new InstrList(instr5, new InstrList(instr6, null)))))));
TempList precoloured = TempList.create(new Temp[] { a, b, c });
        TempList registers = TempList.create(new Temp[] { b, c });
        TestFrame testFrame = new TestFrame(precoloured, registers);

        RegAlloc alloc = new RegAlloc(testFrame, instrList, true);
        TempMapHelper helper = new TempMapHelper(alloc, registers);


    }
}