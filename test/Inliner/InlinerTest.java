package Inliner;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintStream;

import org.junit.Test;

import Absyn.PrettyPrinter;
import Bind.Binder;
import Bind.Renamer;
import ErrorMsg.ErrorMsg;
import Inlining.Inliner;
import Parse.CupParser;
import Parse.Parser;
import Parse.Program;



public class InlinerTest {

     @Test
    public void foorLoop() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Parser parser = new CupParser("let function sub(i: int, j: int) :int = i + j in sub(1, 2) end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        program.absyn.accept(new Binder(errorMsg));
        program.absyn.accept(new Renamer());
        Inliner inliner = new Inliner();
        program.absyn.accept(inliner);
        inliner.visitedExp.accept(new PrettyPrinter(System.out));
    }
}