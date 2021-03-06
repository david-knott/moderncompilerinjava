package Intel;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import Temp.Temp;
import Tree.IR;


public class SeqTest extends BaseCodeGenTest {

    @Test
    public void seqTwoItems() throws Exception {
        IR tree = new Tree.SEQ(
            new Tree.MOVE(
                new Tree.TEMP(Temp.create()),
                new Tree.TEMP(Temp.create())
            ),
            new Tree.MOVE(
                new Tree.TEMP(Temp.create()),
                new Tree.TEMP(Temp.create())
            )
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.MOVE_EXP_TO_TEMP);
        assertTrue(testEmitter.get(1) == AssemInstructionEnum.MOVE_EXP_TO_TEMP);
        assertEquals(2, testEmitter.size());
    }

    @Test
    public void seqThreeItems() throws Exception {
        IR tree = new Tree.SEQ(
            new Tree.MOVE(
                new Tree.TEMP(Temp.create()),
                new Tree.TEMP(Temp.create())
            ),
            new Tree.SEQ(
                new Tree.MOVE(
                    new Tree.TEMP(Temp.create()),
                    new Tree.TEMP(Temp.create())
                ),
                new Tree.MOVE(
                    new Tree.TEMP(Temp.create()),
                    new Tree.TEMP(Temp.create())
                )
            )
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.MOVE_EXP_TO_TEMP);
        assertTrue(testEmitter.get(1) == AssemInstructionEnum.MOVE_EXP_TO_TEMP);
        assertTrue(testEmitter.get(2) == AssemInstructionEnum.MOVE_EXP_TO_TEMP);
        assertEquals(3, testEmitter.size());
    }


}