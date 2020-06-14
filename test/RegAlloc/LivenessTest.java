package RegAlloc;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

import Assem.InstrList;
import Assem.MOVE;
import Assem.OPER;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Temp.Temp;
import Temp.TempList;

public class LivenessTest {
    @Test
    public void argTest() {
        
        Temp r1 = Temp.create();
        Temp r2 = Temp.create();
        Temp r3 = Temp.create();
        Temp r4 = Temp.create();
        Temp r5 = Temp.create();
        InstrList instrs = null;
        instrs = InstrList.append(instrs, new MOVE("`s0 -> `d0", r1, r2));
        instrs = InstrList.append(instrs, new MOVE("`s0 -> `d0", r3, r4));
        instrs = InstrList.append(instrs, new OPER("3", null, null));
        instrs = InstrList.append(instrs, new OPER("4", null, null));
        instrs = InstrList.append(instrs, new OPER("5", null, new TempList(r5)));

        FlowGraph flowGraph = new AssemFlowGraph(instrs);
        flowGraph.show(System.out);
        Liveness liveness = new Liveness(flowGraph);
        liveness.dumpLiveness(instrs);
        assertNotNull(liveness);

    }
    
@Test
    public void callTest() {
        
        Temp r1 = Temp.create();
        Temp r2 = Temp.create();
        Temp r3 = Temp.create();
        Temp r4 = Temp.create();
        Temp r5 = Temp.create();
        Temp r6 = Temp.create();
        Temp r7 = Temp.create();
        Temp r8 = Temp.create();
        InstrList instrs = null;
        instrs = InstrList.append(instrs, new OPER("1 -> `d0", new TempList(r1), null));
        instrs = InstrList.append(instrs, new MOVE("`s0 -> `d0", r2, r1));
        instrs = InstrList.append(instrs, new OPER("2 -> `d0", new TempList(r3), null));
        instrs = InstrList.append(instrs, new MOVE("`s0 -> `d0", r4, r3));
        instrs = InstrList.append(instrs, new OPER("3 -> `d0", new TempList(r5), null));
        instrs = InstrList.append(instrs, new MOVE("`s0 -> `d0", r6, r5));
        instrs = InstrList.append(instrs, new OPER("4 -> `d0", new TempList(r7), null));
        instrs = InstrList.append(instrs, new MOVE("`s0 -> `d0", r8, r7));

        instrs = InstrList.append(instrs, new OPER("", null, null));
        instrs = InstrList.append(instrs, new OPER("", null, null));
        instrs = InstrList.append(instrs, new OPER("CALL", null, new TempList(r7, new TempList(r5, new TempList(r3, new TempList(r1))))));

        FlowGraph flowGraph = new AssemFlowGraph(instrs);
        flowGraph.show(System.out);
        Liveness liveness = new Liveness(flowGraph);
        liveness.dumpLiveness(instrs);
        assertNotNull(liveness);

    }

}