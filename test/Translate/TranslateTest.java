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
    }

    @Test
    public void nil() {
        Translate translate = new Translate();
        var ex = translate.Noop().unEx();
        var nx = translate.Noop().unNx();
        assertFalse(true);

    }

    @Test
    public void string_test() {
        Translate translate = new Translate();
        var exp = translate.string("this is a string");
        assertFalse(true);
    }

    @Test
    public void call_test() {
        Translate translate = new Translate();
        Level level = new Level(new TestFrame());
        //pass in IL versions of call parameters
        ExpTyList expTyList = null; //new ExpTyList(new ExpTy());
        var exp = translate.call(level, new Label("test"), null);
        System.out.println(exp);
        assertFalse(true);
    }




}
