package Tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

public class SEQTest {

    @Test
    public void normaliseStmStm() {

        SEQ test = new SEQ(new EmptyStm(), new EmptyStm());
        SEQ normalised = test.normalise();
        assertTrue(normalised.left instanceof EmptyStm);
        assertTrue(normalised.right instanceof EmptyStm);
    }

    @Test
    public void normaliseStmSeq() {

        SEQ test = new SEQ(new EmptyStm(), new SEQ(new EmptyStm(), new EmptyStm()));
        SEQ normalised = test.normalise();
        assertTrue(normalised.left instanceof EmptyStm);
        assertTrue(normalised.right instanceof SEQ);
    }

    @Test
    public void normaliseSeqStm() {
        SEQ test = new SEQ(new SEQ(new EmptyStm("one"), new SEQ(new EmptyStm("two"), new EmptyStm("three"))),
                new SEQ(new EmptyStm("four"), new EmptyStm("five")));
        var normalised = (SEQ) test.normalise();
        assertTrue(normalised.left instanceof EmptyStm);
        assertEquals(((EmptyStm) normalised.left).toString(), "one");
        assertTrue(normalised.right instanceof SEQ);
    }

    @Test
    public void normaliseSeqSeq() {
        SEQ test = new SEQ(new SEQ(new EmptyStm("one"), new EmptyStm()), new SEQ(new EmptyStm(), new EmptyStm()));
        SEQ normalised = test.normalise();
        assertTrue(normalised.left instanceof EmptyStm);
        assertEquals(((EmptyStm) normalised.left).toString(), "one");
        assertTrue(normalised.right instanceof SEQ);
    }

    @Test
    public void normaliseSeqJagged() {
        SEQ test = new SEQ(new SEQ(new EmptyStm("one"), new SEQ(new EmptyStm("two"), new EmptyStm("three"))),
                new SEQ(new EmptyStm("four"), new EmptyStm("five")));
        SEQ normalised = test.normalise();
        System.out.println(normalised);
    }

    @Test
    public void normaliseSeqJagged2() {
        SEQ test = new SEQ(new SEQ(new EmptyStm("one"), new EmptyStm("two")),
                new SEQ(new EmptyStm("three"), new SEQ(new EmptyStm("four"), new EmptyStm("five")))

        );
        SEQ normalised = test.normalise();
        System.out.println(normalised);
    }

    @Test
    public void normaliseSeqJagged3() {
        SEQ test = new SEQ(new SEQ(new EmptyStm("one"), new EmptyStm("two")),
                new SEQ(new SEQ(new EmptyStm("three"), new EmptyStm("three.halg")),
                        new SEQ(new EmptyStm("four"), new EmptyStm("five")))

        );
        SEQ normalised = test.normalise();
        assertTrue(normalised.left instanceof EmptyStm);
        assertEquals(((EmptyStm) normalised.left).toString(), "one");
        assertTrue(normalised.right instanceof SEQ);
    }
}