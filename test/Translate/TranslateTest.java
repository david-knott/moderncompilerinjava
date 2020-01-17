package Translate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import ErrorMsg.ArgumentMismatchError;
import ErrorMsg.ErrorMsg;
import ErrorMsg.FieldNotDefinedError;
import ErrorMsg.FunctionNotDefinedError;
import ErrorMsg.TypeMismatchError;
import ErrorMsg.UndefinedVariableError;
import Frame.Access;
import Frame.Frame;
import Main.Main;
import Semant.Semant;
import Temp.Label;
import Temp.Temp;
import Translate.Translate;
import Tree.CALL;
import Tree.Exp;
import Tree.ExpList;
import Tree.Stm;
import Util.BoolList;

public class TranslateTest {

    class TestFrame extends Frame {

        @Override
        public Temp FP() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Temp RV() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int wordSize() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public Frame newFrame(Label name, BoolList formals) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Access allocLocal(boolean escape) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Stm procEntryExit1(Stm body) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Exp externalCall(String func, ExpList args) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Exp string(Label l, String literal) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    @Test
    public void nil() {
        Translate translate = new Translate();
        var ex = translate.Noop().unEx();
        var nx = translate.Noop().unNx();
        assertFalse(true);
    }

    @Test
    public void string_literal_test() {
        Translate translate = new Translate();
        Level level = new Level(new TestFrame());
        var exp = translate.string("this is a string", level);
        assertFalse(true);
    }


    @Test
    public void string_comparision_test() {
        Translate translate = new Translate();
        var left = new ExpTy(null, Semant.STRING);
        var right = new ExpTy(null, Semant.STRING);
        var exp = translate.equalsOperator(1, left, right);
        assertFalse(true);
    }

    @Test
    public void function_call_test() {
        Translate translate = new Translate();
        Level level = new Level(new TestFrame());
        Level level2 = new Level(new TestFrame());
        level2.parent = level;
        //pass in IL versions of call parameters
        var exp = translate.call(level, level2, new Label("test"), null, Semant.VOID);
        if(exp instanceof Ex){
          //  ((Ex)exp);
        }
        System.out.println(exp);
        assertFalse(true);
    }

    @Test
    public void level_is_ancestor_depth_1_test() {
        Level level = new Level(new TestFrame());
        Level level2 = new Level(new TestFrame());
        level2.parent = level;
        assertTrue(level.ancestor(level2));
    }

    @Test
    public void level_is_ancestor_depth_2_test() {
        Level level1 = new Level(new TestFrame());
        Level level2 = new Level(new TestFrame());
        Level level3 = new Level(new TestFrame());
        level2.parent = level1;
        level3.parent = level2;
        assertTrue(level1.ancestor(level3));
        assertTrue(level1.ancestor(level2));
    }


    @Test
    public void level_is_not_ancestor_depth_1_test() {
        Level level1 = new Level(new TestFrame());
        Level level2 = new Level(new TestFrame());
        level2.parent = level1;
        assertFalse(level2.ancestor(level1));
    }

    @Test
    public void level_is_not_ancestor_depth_2_test() {
        Level level1 = new Level(new TestFrame());
        Level level2 = new Level(new TestFrame());
        Level level3 = new Level(new TestFrame());
        level2.parent = level1;
        level3.parent = level2;
        assertFalse(level2.ancestor(level1));
    }


    @Test
    public void level_is_descendant_depth_1_test() {
        Level level1 = new Level(new TestFrame());
        Level level2 = new Level(new TestFrame());
        level2.parent = level1;
        assertTrue(level2.descendant(level1));
    }

    @Test
    public void level_is_descendant_depth_2_test() {
        Level level1 = new Level(new TestFrame());
        Level level2 = new Level(new TestFrame());
        Level level3 = new Level(new TestFrame());
        level2.parent = level1;
        level3.parent = level2;
        assertTrue(level2.descendant(level1));
    }

    @Test
    public void level_is_depth_minus_2_difference_test() {
        Level level1 = new Level(new TestFrame());
        Level level2 = new Level(new TestFrame());
        Level level3 = new Level(new TestFrame());
        level2.parent = level1;
        level3.parent = level2;
        assertEquals(-2, level1.depthDifference(level3));
    }

    @Test
    public void level_is_depth_plus_2_difference_test() {
        Level level1 = new Level(new TestFrame());
        Level level2 = new Level(new TestFrame());
        Level level3 = new Level(new TestFrame());
        level2.parent = level1;
        level3.parent = level2;
        assertEquals(2, level3.depthDifference(level1));
    }

    @Test
    public void level_is_depth_minus_1_difference_test() {
        Level level1 = new Level(new TestFrame());
        Level level2 = new Level(new TestFrame());
        level2.parent = level1;
        assertEquals(-1, level1.depthDifference(level2));
    }

    @Test
    public void level_is_depth_plus_1_difference_test() {
        Level level1 = new Level(new TestFrame());
        Level level2 = new Level(new TestFrame());
        level2.parent = level1;
        assertEquals(1, level2.depthDifference(level1));
    }


    @Test
    public void level_is_depth_0_difference_test() {
        Level level1 = new Level(new TestFrame());
        Level level2 = new Level(new TestFrame());
        level2.parent = level1;
        assertEquals(0, level2.depthDifference(level2));
    }

    @Test
    public void call_static_link_test(){
        Translate translate = new Translate();
        Level callerLevel = new Level(new TestFrame());
        Level calleeLevel = new Level(new TestFrame());
        calleeLevel.parent = callerLevel;
        var callExp = translate.call(callerLevel, calleeLevel, new Label("function test"), null, Semant.VOID);
        //the first argument of callExp.exp.args should be a static link with one level
        if(callExp instanceof Ex){
            Ex ex = (Ex)callExp;

            System.out.println(ex);
        }

    }
}
