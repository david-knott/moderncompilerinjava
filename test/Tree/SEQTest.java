package Tree;

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
import Tree.SEQ;
import Tree.Stm;
import Util.BoolList;




public class SEQTest {

    @Test
    public void normaliseStmStm() {

        SEQ test = new SEQ(
            new EmptyStm(),
            new EmptyStm()
        );
        SEQ normalised = test.normalise();
        System.out.println(normalised);
    }

    @Test
    public void normaliseStmSeq() {

        SEQ test = new SEQ(
            new EmptyStm(),
            new SEQ(new EmptyStm(), new EmptyStm())
        );
        SEQ normalised = test.normalise();
        System.out.println(normalised);
    }

    @Test
    public void normaliseSeqStm() {
        SEQ test = new SEQ(
            new SEQ(new EmptyStm(), new EmptyStm()),
            new EmptyStm()
        );
        SEQ normalised = test.normalise();
        System.out.println(normalised);
    }

    @Test
    public void normaliseSeqSeq() {
        SEQ test = new SEQ(
            new SEQ(new EmptyStm(), new EmptyStm()),
            new SEQ(new EmptyStm(), new EmptyStm())
        );
        SEQ normalised = test.normalise();
        System.out.println(normalised);
    }


    @Test
    public void normaliseSeqJagged() {
        SEQ test = new SEQ(
            new SEQ(new EmptyStm("one"), new SEQ(new EmptyStm("two"), new EmptyStm("three"))),
            new SEQ(new EmptyStm("four"), new EmptyStm("five"))
        );
        SEQ normalised = test.normalise();
        System.out.println(normalised);
    }

    @Test
    public void normaliseSeqJagged2() {
        SEQ test = new SEQ(
            new SEQ(new EmptyStm("one"), new EmptyStm("two")),
            new SEQ(new EmptyStm("three"), new SEQ(new EmptyStm("four"), new EmptyStm("five")))

        );
        SEQ normalised = test.normalise();
        System.out.println(normalised);
    }

    @Test
    public void normaliseSeqJagged3() {
        SEQ test = new SEQ(
            new SEQ(new EmptyStm("one"), new EmptyStm("two")),
            new SEQ(new SEQ(new EmptyStm("three"), new EmptyStm("three.halg")), new SEQ(new EmptyStm("four"), new EmptyStm("five")))

        );
        SEQ normalised = test.normalise();
        System.out.println(normalised);
    }
}