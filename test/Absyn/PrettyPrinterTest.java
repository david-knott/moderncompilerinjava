package Absyn;

import java.io.PrintStream;

import org.junit.Test;

import Absyn.PrettyPrinter;
import ErrorMsg.ErrorMsg;
import Parse.CupParser;
import Parse.Parser;
import Parse.Program;

public class PrettyPrinterTest {
    
    @Test
    public void test4_9() {
        Parser parser = new CupParser(
                "let /* Calculate n!. */ function fact (n : int) : int = if  n = 0 then 1 else n * fact (n - 1) in fact (10) end",
                new ErrorMsg("", System.out));
        Program program = parser.parse();
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
    }

    @Test
    public void test4_101() {
        Parser parser = new CupParser(
                "print(\"x45x50ITA\")",
                new ErrorMsg("", System.out));
        Program program = parser.parse();
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
    }

    @Test
    public void test4_102() {
        Parser parser = new CupParser(
                "print(\"\\\"x45x50ITA\\\"\")",
                new ErrorMsg("", System.out));
        Program program = parser.parse();
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
    }
}
