package Inliner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintStream;

import org.junit.Test;

import Absyn.Absyn;
import Absyn.PrettyPrinter;
import Bind.Binder;
import Bind.Renamer;
import ErrorMsg.ErrorMsg;
import Inlining.Inliner;
import Parse.CupParser;
import Parse.Parser;
import Parse.ParserFactory;
import Parse.ParserService;
import Parse.Program;



public class InlinerTest {

    private ParserService parserService;

    public InlinerTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Test
    public void foorLoop() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let function sub(i: int, j: int) :int = i + j in sub(1, 2) end", new ErrorMsg("", System.out));
        program.accept(new Binder(errorMsg));
        program.accept(new Renamer());
        Inliner inliner = new Inliner(program);
        program.accept(inliner);
        inliner.visitedExp.accept(new PrettyPrinter(System.out));
    }
}