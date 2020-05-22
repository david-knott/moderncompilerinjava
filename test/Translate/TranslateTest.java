package Translate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;

import Assem.Instr;
import Assem.InstrList;
import Assem.TEST;
import Frame.Frame;
import Helpers.TestFrame;
import Intel.IntelFrame;
import Semant.Semant;
import Temp.Label;
import Temp.Temp;
import Temp.TempList;
import Tree.MEM;
import Tree.TEMP;
import Types.ARRAY;

public class TranslateTest {

    private Translator translator;
    private Frame frame;
    private Level level;

    @Before
    public void setup() {
        translator = new Translator(false);
        frame = new IntelFrame(new Label("test"), null);
        level = new Level(frame);
    }

    @Test
    public void simpleVarNotNull() {
        Access access = level.allocLocal(true);
        assertNotNull(access);
        Exp simpleVar = translator.simpleVar(access, level);
        assertNotNull(simpleVar);
    }

    @Test
    public void simpleVarInReg() {
        Access access = level.allocLocal(false);
        Exp simpleVar = translator.simpleVar(access, level);
        assertNotNull(simpleVar);
        assertTrue(simpleVar instanceof Ex);
        Ex ex = (Ex) simpleVar;
        assertNotNull(ex);
        assertTrue(ex.exp instanceof TEMP);
    }

    @Test
    public void simpleVarInFrame() {
        Access access = level.allocLocal(true);
        Exp simpleVar = translator.simpleVar(access, level);
        assertNotNull(simpleVar);
        assertTrue(simpleVar instanceof Ex);
        Ex ex = (Ex) simpleVar;
        assertNotNull(ex);
        assertTrue(ex.exp instanceof MEM);
    }

    @Test
    public void varExpInReg() {
        Access access = level.allocLocal(false);
        Exp simpleVar = translator.simpleVar(access, level);
        ExpTy expTy = new ExpTy(simpleVar, Semant.INT);
        Exp varExp = translator.varExp(expTy);
        assertNotNull(varExp);
        assertEquals(simpleVar, varExp);
    }

    @Test
    public void varExpInFrame() {
        Access access = level.allocLocal(true);
        Exp simpleVar = translator.simpleVar(access, level);
        ExpTy expTy = new ExpTy(simpleVar, Semant.INT);
        Exp varExp = translator.varExp(expTy);
        assertNotNull(varExp);
        assertEquals(simpleVar, varExp);
    }

    @Test
    public void initArray() {
        Access access = level.allocLocal(true);
        ExpTy integerExp = new ExpTy(translator.integer(1), Semant.INT);
        Exp array = translator.array(level, integerExp, integerExp);
        assertNotNull(array);
    }

    @Test
    public void subscriptVar() {
        Access access = level.allocLocal(true);
        ExpTy integerExp = new ExpTy(translator.integer(1), Semant.INT);
        ExpTy arrayVar = new ExpTy(translator.simpleVar(access, level), new ARRAY(Semant.INT));
        Exp subscriptVar = translator.subscriptVar(integerExp, arrayVar, level);
        assertNotNull(subscriptVar);

     
    }
}
