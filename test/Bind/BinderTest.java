package Bind;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import ErrorMsg.ErrorMsg;
import Parse.CupParser;
import Parse.Parser;
import Parse.Program;

public class BinderTest {

    @Test
    public void typeDefs() {
        Parser parser = new CupParser("let type a = int in end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);

    }

    @Test
    public void recursiveTypeDefs() {
        Parser parser = new CupParser("let type list = {first: int, rest: list} in end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);
    }

    @Test
    public void varDefWithType() {
        Parser parser = new CupParser("let var a:int := 0 in end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);
    }

    @Test
    public void varDefWithoutType() {
        Parser parser = new CupParser("let var a := 0 in end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);
    }

    @Test
    public void procedureDef() {
        Parser parser = new CupParser("let function a() = () in end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);
    }

    @Test
    public void functionDef() {
        Parser parser = new CupParser("let function a():int = 1 in end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);
    }

    @Test
    public void scopeVarDef() {
        Parser parser = new CupParser("let function a() = let var b:int := 1 in end in end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);
    }
}
