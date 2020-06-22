package Semant;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

import Absyn.IntExp;
import Absyn.NameTy;
import Absyn.NilExp;
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

    private Semant semant;

    public SemantTests() {
        Frame frame = new IntelFrame(Label.create("tigermain"), null);
        Level topLevel = new Level(frame);
        Translator translate = new Translator();
        ErrorMsg errorMsg = new ErrorMsg("test");
        this.semant = new Semant(errorMsg, topLevel, translate);
    }
    
    @Test
    public void transDecVarDecVarAndInitTypesMatch() {
        VarDec varDec = new VarDec(10, Symbol.symbol("a"), new NameTy(14, Symbol.symbol("int")), new StringExp(12, "bad"));
        Exp exp = this.semant.transDec(varDec);
        assertNotNull(exp);
    }

    @Test
    public void transDecVarDecNilAssignedToRecord() {
        VarDec varDec = new VarDec(10, Symbol.symbol("a"), new NameTy(14, Symbol.symbol("int")), new NilExp(12));
        Exp exp = this.semant.transDec(varDec);
        assertNotNull(exp);
    }
}