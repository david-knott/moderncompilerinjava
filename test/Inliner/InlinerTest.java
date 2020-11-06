package Inliner;

import org.junit.Test;

import Absyn.Absyn;
import Absyn.PrettyPrinter;
import Bind.Binder;
import Bind.Renamer;
import ErrorMsg.ErrorMsg;
import Inlining.Inliner;
import Parse.ParserFactory;
import Parse.ParserService;

public class InlinerTest {

    private ParserService parserService;

    public InlinerTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Test
    public void simpleAdd() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let function sub(i: int, j: int) :int = i + j in sub(1, 2) end", new ErrorMsg("", System.out));
        program.accept(new Binder(errorMsg));
        program.accept(new Renamer());
        program.accept(new PrettyPrinter(System.out));
        Inliner inliner = new Inliner(program);
        program.accept(inliner);
        inliner.visitedExp.accept(new PrettyPrinter(System.out));
    }

    @Test
    public void nestedFunction() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let function sub(i: int, j: int) :int = i + j in printi(sub(1, 2)) end", new ErrorMsg("", System.out));
        program.accept(new Binder(errorMsg));
        program.accept(new Renamer());
        program.accept(new PrettyPrinter(System.out));
        Inliner inliner = new Inliner(program);
        program.accept(inliner);
        inliner.visitedExp.accept(new PrettyPrinter(System.out));
    }
}