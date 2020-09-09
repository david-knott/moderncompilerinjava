package Bind;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import Absyn.PrettyPrinter;
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
        //expect the binder to have a in the type symbol table.
    }

    @Test
    public void recursiveTypeDefs() {
        Parser parser = new CupParser("let type list = {first: int, rest: list} in 1 end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);
    }

    @Test
    public void varDefWithType() {
        Parser parser = new CupParser("let var a:int := 0 in a + a end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.absyn.accept(prettyPrinter);

    }

    @Test
    public void varDefBinop() {
        Parser parser = new CupParser("let var a:int := 3 + 4 in a end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.absyn.accept(prettyPrinter);
    }

    @Test
    public void varDefWithoutType() {
        Parser parser = new CupParser("let var a := 0 in a end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);
    }

    @Test
    public void procedureDef() {
        Parser parser = new CupParser("let function a() = () in a() end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.absyn.accept(prettyPrinter);

    }

    @Test
    public void functionDef() {
        Parser parser = new CupParser("let function a():int = 1 in a() end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);
    }

    @Test
    public void functionArgsDef() {
        Parser parser = new CupParser("let function a(a: int, b:int):int = a + b in a(3,4) end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.absyn.accept(prettyPrinter);
    }


    @Test
    public void scopeVarDef() {
        Parser parser = new CupParser("let function a() = let var b:int := 1 + 3 in end in a() end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.absyn.accept(prettyPrinter);
    }

    @Test
    public void breakTest() {
        Parser parser = new CupParser("let var x := 0 in while 1 do ( for i := 0 to 10 do ( x := x + i; if x >= 42 then break ); if x >= 51 then break ) end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.absyn.accept(prettyPrinter);
    }


}
