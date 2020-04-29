package RegAlloc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.Test;

import Assem.InstrList;
import Assem.TEST;
import Frame.Frame;
import Graph.Graph;
import Graph.Node;
import Graph.NodeList;
import Intel.IntelFrame;
import Temp.Label;
import Temp.Temp;
import Temp.TempList;


public class RegisterSpillerTest {

    @Test
    public void spillDef() {
        Frame frame =  new IntelFrame(new Label(), null);
        Temp a = new Temp();
        Temp b = new Temp();
        TempList src = new TempList(a, null);
        TempList dst = new TempList(b, null);
        //notice that a variable must be added as a dst
        TempList spill = new TempList(b, null);
        RegisterSpiller registerSpiller = new RegisterSpiller(new DefaultSpillSelectStrategy(), spill, frame);
        TEST defInstr = new TEST(dst, src);
        InstrList instrList = new InstrList(defInstr, null);
        InstrList newInstrList = registerSpiller.rewrite(instrList);
        //we expect one additional statement for the save to frame
        assertEquals(2, newInstrList.size());
        //we expect the first instuction to be the test
        assertEquals(defInstr, newInstrList.head);
        //we expect the second instructio to be an OPER
    }    

    @Test
    public void spillDefUse() {
        Frame frame =  new IntelFrame(new Label(), null);
        Temp a = new Temp();
        Temp b = new Temp();
        Temp c = new Temp();
        TempList src = new TempList(a, null);
        TempList dst = new TempList(b, null);
        //notice that a variable must be added as a dst
        TempList spill = new TempList(b, null);
        RegisterSpiller registerSpiller = new RegisterSpiller(new DefaultSpillSelectStrategy(), spill, frame);
        TEST defInstr = new TEST(dst, src);
        TEST srcInstr = new TEST(new TempList(c, null), dst);
        InstrList instrList = new InstrList(defInstr, new InstrList(srcInstr, null));
        InstrList newInstrList = registerSpiller.rewrite(instrList);
        //we expect two additional statement for the save to frame
        //one to move from frame back to temp
        assertEquals(4, newInstrList.size());
        //we expect the first instuction to be the test
        assertEquals(defInstr, newInstrList.head);
        //we expect the last instruction to be an test
        assertEquals(srcInstr, newInstrList.tail.tail.tail.head);
    }    



}