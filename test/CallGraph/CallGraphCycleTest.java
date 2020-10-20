package CallGraph;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintStream;

import org.junit.Test;

import Bind.Binder;
import ErrorMsg.ErrorMsg;
import Parse.CupParser;
import Parse.Parser;
import Parse.Program;

public class CallGraphCycleTest {

    @Test
    public void noCycle() {
        Parser parser = new CupParser("let function a() : int = b() function b() : int = c() function c() : int = 1 in a() end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        CallGraphVisitor  callGraphVisitor = new CallGraphVisitor();
        program.absyn.accept(new Binder(new ErrorMsg("f", System.out)));
        program.absyn.accept(callGraphVisitor);
        assertFalse(callGraphVisitor.functionCallGraph.isCyclic());
    }

    @Test
    public void simpleCycle() {
        Parser parser = new CupParser("let function a() = a() in a() end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        CallGraphVisitor  callGraphVisitor = new CallGraphVisitor();
        program.absyn.accept(new Binder(new ErrorMsg("f", System.out)));
        program.absyn.accept(callGraphVisitor);
        assertTrue(callGraphVisitor.functionCallGraph.isCyclic());
    }

    @Test
    public void complexCycle() {
        Parser parser = new CupParser("let function a() = (b(); c()) function b() = a() function c() = c() in a() end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        CallGraphVisitor  callGraphVisitor = new CallGraphVisitor();
        program.absyn.accept(new Binder(new ErrorMsg("f", System.out)));
        program.absyn.accept(callGraphVisitor);
        assertTrue(callGraphVisitor.functionCallGraph.isCyclic());
    }
}
