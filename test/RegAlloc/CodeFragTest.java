package RegAlloc;

import org.junit.Test;

import Assem.Instr;
import Assem.InstrList;
import Assem.TEST;
import Intel.IntelFrame;
import Temp.Label;
import Temp.Temp;
import Temp.TempList;

public class CodeFragTest {

    @Test
    public void interference2VarsSimple() {
        Temp a = new Temp();
        Temp b = new Temp();
        //expect a to interfer with b
        Instr instr1 = new TEST(new TempList(a), new TempList(b));
        InstrList instrList = new InstrList(instr1, null);
        InterferenceGraph interferenceGraph = new InterferenceGraphBuilder().create();

        CodeFrag codeFrag = new CodeFrag(instrList, new IntelFrame(new Label(), null));
        codeFrag.processAll(new FlowGraphBuilder(), interferenceGraph);

        System.out.println("test");
        interferenceGraph.show(System.out);
    }
    @Test
    public void interference2Vars() {
        Temp a = new Temp();
        Temp b = new Temp();
        //expect a to interfer with b
        Instr instr1 = new TEST(new TempList(a), new TempList(b));
        Instr instr2 = new TEST(new TempList(b), new TempList(a));
        InstrList instrList = new InstrList(instr1, new InstrList(instr2, null));
        InterferenceGraph interferenceGraph = new InterferenceGraphBuilder().create();

        CodeFrag codeFrag = new CodeFrag(instrList, new IntelFrame(new Label(), null));
        codeFrag.processAll(new FlowGraphBuilder(), interferenceGraph);

        System.out.println("test");
        interferenceGraph.show(System.out);
    }

}