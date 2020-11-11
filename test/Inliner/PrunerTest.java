package Inliner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import Absyn.Absyn;
import Absyn.PrettyPrinter;
import Bind.Binder;
import Bind.Renamer;
import CallGraph.CallGraphVisitor;
import Cloner.AbsynCloner;
import ErrorMsg.ErrorMsg;
import Inlining.Pruner;
import Parse.ParserFactory;
import Parse.ParserService;

public class PrunerTest {

    private ParserService parserService;

    public PrunerTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Test
    public void prune() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let function prune() :int = 1 in 1 end", new ErrorMsg("", System.out));
        program.accept(new Binder(errorMsg));
        program.accept(new Renamer());
        Pruner pruner = new Pruner(program);
        program.accept(pruner);
        pruner.visitedDecList.accept(new PrettyPrinter(System.out));
        assertEquals(1, pruner.pruneCount);
    }

    @Test
    public void noPrune() {
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let function prune() :int = 1 in prune() end", new ErrorMsg("", System.out));
        program.accept(new Binder(errorMsg));
        program.accept(new Renamer());
        Pruner pruner = new Pruner(program);
        program.accept(pruner);
        pruner.visitedDecList.accept(new PrettyPrinter(System.out));
        assertEquals(0, pruner.pruneCount);
    }
}