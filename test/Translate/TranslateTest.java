package Translate;

import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;
import Semant.Semant;
import Temp.Label;
import Temp.Temp;
import Tree.BINOP;
import Tree.CONST;
import Tree.Exp;
import Tree.ExpList;
import Tree.MEM;
import Tree.Stm;
import Util.BoolList;

public class TranslateTest {

    class TestFrame extends Frame.Frame {

        @Override
        public Temp FP() {
            // TODO Auto-generated method stub
            return new Temp();
        }

        @Override
        public Temp RV() {
            // TODO Auto-generated method stub
            return new Temp();
        }

        @Override
        public int wordSize() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public Frame.Frame newFrame(Label name, BoolList formals) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Frame.Access allocLocal(boolean escape) {
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

        @Override
        public Stm procEntryExit3(Stm body) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public class TestAcess extends Frame.Access{

        @Override
        public Exp exp(Exp framePtr) {
            return new MEM(new BINOP(BINOP.PLUS, framePtr, new CONST(10)));
        }
    }

    @Test
    public void local_simpleVar_test() {
        Translate translate = new Translate();
        Level level = new Level(new TestFrame());
        var frameAccess = new TestAcess();
        var access = new Access(level, frameAccess);
        var simpleVar = translate.simpleVar(access, level);
        assertFalse(true);
    }

    @Test
    public void nonlocal_simpleVar_test() {
        Translate translate = new Translate();
        Level varUsageLevel = new Level(new TestFrame());
        Level varDefLevel = new Level(new TestFrame());
        varUsageLevel.parent = varDefLevel;
        var frameAccess = new TestAcess();
        var access = new Access(varDefLevel, frameAccess);
        var simpleVar = translate.simpleVar(access, varUsageLevel);
        assertFalse(true);
    }

    @Test
    public void subscriptVar_test() {
        Translate translate = new Translate();
        Level level = new Level(new TestFrame());
        var frameAccess = new TestAcess();
        var access = new Access(level, frameAccess);
        var simpleVar = translate.simpleVar(access, level);
        var transIndexExp = new Tree.CONST(1);
        //var subscriptVar = translate.subscriptVar(transIndexExp, simpleVar, level);
        assertFalse(true);
    }

    @Test
    public void string_literal_test() {
        Translate translate = new Translate();
        Level level = new Level(new TestFrame());
     //   var exp = translate.string("this is a string", level);
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

    @Test
    public void assign_test() {
        Translate translate = new Translate();
        Level level = new Level(new TestFrame());
        ExpTy transVar = null;
        ExpTy transExp = null;
        var assign = translate.assign(level, transVar, transExp);
        System.out.println(assign);
        assertFalse(true);
    }

    @Test
    public void let_test() {
        Translate translate = new Translate();
        var let = translate.letE(null, null);
        //a nop should be returned
    }

    @Test
    public void record_assign_test() {
        String tigerCode = "let type rectype = {age: int} var int1:int := 11 var rec2:rectype := nil var rec1:rectype := rectype {age=99}   in rec1.age := 111; int1 := 222; rec2 := rec1 end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile(); 
        System.out.println("ss");
        assertFalse(m.hasErrors());

    }






}
