package CallGraph;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintStream;

import org.junit.Test;

import Absyn.PrettyPrinter;
import Bind.Binder;
import ErrorMsg.ErrorMsg;
import Inlining.Inliner;
import Parse.CupParser;
import Parse.Parser;
import Parse.Program;

public class CallGraphTest {


    @Test
    public void noCycle() {
        Parser parser = new CupParser("let function a() : int = b() function b() : int = c() function c() : int = 1 in a() end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        CallGraphVisitor  callGraphVisitor = new CallGraphVisitor();
        program.absyn.accept(new Binder(new ErrorMsg("f", System.out)));
        program.absyn.accept(callGraphVisitor);
        callGraphVisitor.functionCallGraph.show(System.out);
        //graph should contain zero cycles.

        /*
        Inliner inliner = new Inliner(program.absyn);
        program.absyn.accept(inliner);
        inliner.visitedExp.accept(new PrettyPrinter(System.out));*/

    }
    @Test
    public void typeDef() {
        Parser parser = new CupParser("let function a() = (b(); c()) function b() = a() function c() = c() in a() end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        CallGraphVisitor  callGraphVisitor = new CallGraphVisitor();
        program.absyn.accept(new Binder(new ErrorMsg("f", System.out)));
        program.absyn.accept(callGraphVisitor);
        callGraphVisitor.functionCallGraph.show(System.out);
        //graph should contain 1 cycle.
    }
}
