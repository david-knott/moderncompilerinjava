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
    public void useTest() {
        
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
    

}