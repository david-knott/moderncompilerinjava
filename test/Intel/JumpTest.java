package Intel;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import Temp.Label;
import Tree.IR;

public class JumpTest extends BaseCodeGenTest {

    @Test
    public void jumpTest() throws Exception {
        IR tree = new Tree.JUMP(
            Label.create()
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.JUMP);
        assertEquals(1, testEmitter.size());
    }
}