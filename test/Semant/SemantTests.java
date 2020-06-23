package Semant;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Absyn.FieldVar;
import Absyn.IntExp;
import Absyn.NameTy;
import Absyn.NilExp;
import Absyn.SimpleVar;
import Absyn.StringExp;
import Absyn.VarDec;
import ErrorMsg.ErrorMsg;
import Frame.Frame;
import Intel.IntelFrame;
import Symbol.Symbol;
import Temp.Label;
import Translate.Level;
import Translate.Translator;
import Translate.Exp;

public class SemantTests {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();


    private Semant semant;

    public SemantTests() {
//        System.setErr(new PrintStream(errContent));
        Frame frame = new IntelFrame(Label.create("tigermain"), null);
        Level topLevel = new Level(frame);
        Translator translate = new Translator();
        ErrorMsg errorMsg = new ErrorMsg("test", System.err);
        this.semant = new Semant(errorMsg, topLevel, translate);
    }

    @Test
    public void transDecVarDecVarAndInitTypesMatch() {
        VarDec varDec = new VarDec(10, Symbol.symbol("a"), new NameTy(14, Symbol.symbol("int")),
                new StringExp(12, "bad"));
        Exp exp = this.semant.transDec(varDec);
        assertNotNull(exp);
        assertTrue(errContent.toString().contains("test::1.11: string cannot be converted to a int."));
    }


    @Test
    public void transDecVarDecVarAndInitTypesMatch2() {
        VarDec varDec = new VarDec(10, Symbol.symbol("a"), new NameTy(14, Symbol.symbol("string")),
                new IntExp(12, 1));
        this.semant.transDec(varDec);
        assertTrue(errContent.toString().contains("test::1.11: int cannot be converted to a string."));
    }

    @Test
    public void transDecVarDecNilAssignedToRecord() {
        VarDec varDec = new VarDec(10, Symbol.symbol("a"), new NameTy(14, Symbol.symbol("int")), new NilExp(12));
        this.semant.transDec(varDec);
        assertTrue(errContent.toString().contains("test::1.11: nil cannot be converted to a int."));
        assertTrue(errContent.toString().contains("test::1.11: int is not a record type."));
    }

    @Test
    public void transSimpleVar() {
        SimpleVar simpleVar = new SimpleVar(1, Symbol.symbol("a"));
        semant.transVar(simpleVar);
        String actual = errContent.toString();
        assertTrue(actual.contains("test::1.2: Undefined variable a."));
    }

    @Test
    public void transFieldVar_VarTypeEqualsFieldType() {
        VarDec varDec = new VarDec(1, Symbol.symbol("v"), null, new IntExp(2, 1));

        SimpleVar simpleVar = new SimpleVar(1, Symbol.symbol("a"));
        FieldVar fieldVar = new FieldVar(1, simpleVar, Symbol.symbol("b"));
        semant.transVar(fieldVar);
        String actual = errContent.toString();
        assertTrue(actual.contains("test::1.2: Undefined variable a."));
    }


}