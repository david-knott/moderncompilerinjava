package Intel;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import Temp.Label;
import Tree.IR;

public class CjumpTest extends BaseCodeGenTest {

    @Test
    public void cjumpTempTemp() throws Exception {
        IR tree = new Tree.CJUMP(
            0, 
            getTemp(),
            getTemp(), 
            Label.create(), 
            Label.create()
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.CJUMP);
        assertEquals(1, testEmitter.size());
    }

    @Test
    public void cjumpBinopBinop() throws Exception {
        IR tree = new Tree.CJUMP(
            0, 
            getBinopExp(),
            getBinopExp(), 
            Label.create(), 
            Label.create()
        );
        TestEmitter testEmitter = new TestEmitter();
        this.createCodeGen(testEmitter).burm(tree);
        assertTrue(testEmitter.get(0) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(1) == AssemInstructionEnum.BINOP);
        assertTrue(testEmitter.get(2) == AssemInstructionEnum.CJUMP);
        assertEquals(3, testEmitter.size());
    }
}