package RegAlloc;

import org.junit.Test;

import Assem.Instr;
import Assem.InstrList;
import Assem.TEST;
import Helpers.TestFrame;
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
        InterferenceGraph interferenceGraph = new InterferenceGraphImpl(); 
        CodeFrag codeFrag = new CodeFrag(instrList, new IntelFrame(new Label(), null));
        codeFrag.processAll(interferenceGraph, new RegisterAllocator());
        interferenceGraph.show(System.out);
    }

    @Test
    public void interference2Vars() {
        Temp a = new Temp();
        Temp b = new Temp();
        Temp c = new Temp();
        //expect a to interfere with b
        Instr instr1 = new TEST(new TempList(a), new TempList(b)); // a <- b
        Instr instr2 = new TEST(new TempList(b), new TempList(a , new TempList(b))); // b <- a op b
        Instr instr3 = new TEST(new TempList(c), new TempList(b, new TempList(c, new TempList(a)))); //c <- b op c op a
        InstrList instrList = new InstrList(instr1, new InstrList(instr2, new InstrList(instr3, null)));
        InterferenceGraph interferenceGraph = new InterferenceGraphImpl(); 
        CodeFrag codeFrag = new CodeFrag(instrList, new IntelFrame(new Label(), null));
        codeFrag.processAll(interferenceGraph, new RegisterAllocator());
    }

    @Test
    public void spillTest() {
        Temp a = new Temp();
        Temp b = new Temp();
        Temp c = new Temp();
        //expect a to interfere with b
        Instr instr1 = new TEST(new TempList(a), new TempList(b)); // a <- b
        Instr instr2 = new TEST(new TempList(b), new TempList(a , new TempList(b))); // b <- a op b
        Instr instr3 = new TEST(new TempList(c), new TempList(b, new TempList(c, new TempList(a)))); //c <- b op c op a
        InstrList instrList = new InstrList(instr1, new InstrList(instr2, new InstrList(instr3, null)));
        InterferenceGraph interferenceGraph = new InterferenceGraphImpl(); 
        CodeFrag codeFrag = new CodeFrag(instrList, new TestFrame());
        codeFrag.processAll(interferenceGraph, new RegisterAllocator());
    }

}