package IntelFrame;

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
import Tree.EmptyStm;
import Tree.MOVE;
import Tree.SEQ;
import Tree.Stm;
import Tree.TEMP;
import Util.BoolList;



public class IntelFrameTest {

    @Test
    public void calleeSaveRestoreTest() {
        IntelFrame intelFrame = new IntelFrame(new Label(), null);
        EmptyStm emptyStm = new EmptyStm();
        Stm result = intelFrame.procEntryExit1(emptyStm);
        //expect that result is type of sequence
        //with first n items
        System.out.println(result);
    }

    @Test
    public void moveFunctionArg1IntoPosition() {
        IntelFrame intelFrame = new IntelFrame(new Label(), new BoolList(true, null));
        EmptyStm emptyStm = new EmptyStm();
        Stm result = intelFrame.procEntryExit1(emptyStm);
        SEQ seq = (SEQ)result;
        SEQ nseq = seq.normalise();
        System.out.println(nseq);
    }

    @Test
    public void moveFunctionArg2IntoPosition() {
        IntelFrame intelFrame = new IntelFrame(new Label(), new BoolList(true, new BoolList(true, new BoolList(true, new BoolList(true, new BoolList(true, new BoolList(true, new BoolList(true, null))))))));
        EmptyStm emptyStm = new EmptyStm();
        Stm result = intelFrame.procEntryExit1(emptyStm);
        SEQ seq = (SEQ)result;
        SEQ nseq = seq.normalise();
        //first item should be a move from callee registers to temporaries
        //first is r12 to temp
        System.out.println("r12=" + IntelFrame.r12);
        matchesMove(nseq, IntelFrame.r12);
    }

    private void matchesMove(SEQ seq, Temp src) {
        assertTrue(seq.left instanceof MOVE);
        MOVE move = (MOVE)seq.left;
        TEMP mvSrc = (TEMP)move.src;
        assertEquals(src, mvSrc.temp);
    }
}