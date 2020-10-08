package Inliner;

import org.junit.Test;

import Absyn.PrettyPrinter;
import Bind.Binder;
import Bind.Renamer;
import ErrorMsg.ErrorMsg;
import Inlining.Pruner;
import Parse.CupParser;
import Parse.Parser;
import Parse.Program;

public class PrunerTest {

     @Test
    public void foorLoop() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Parser parser = new CupParser("let function sub(i: int, j: int) :int = i + j in sub(1, 2) end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        program.absyn.accept(new Binder(errorMsg));
        program.absyn.accept(new Renamer());
        Pruner pruner = new Pruner();
        program.absyn.accept(pruner);
        pruner.visitedExp.accept(new PrettyPrinter(System.out));
    }
}