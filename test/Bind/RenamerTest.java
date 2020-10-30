package Bind;

import java.io.PrintStream;

import org.junit.Test;

import Absyn.Absyn;
import Absyn.PrettyPrinter;
import ErrorMsg.ErrorMsg;
import Parse.CupParser;
import Parse.Parser;
import Parse.Program;

public class RenamerTest {

    @Test
    public void test4_30() {
        Parser parser = new CupParser(
                "let type a = { a: int } function a(a: a): a = a { a = a + a } var a : a := a(1, 2) in a.a end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);

        program.accept(binder);
        Renamer renamer = new Renamer();
        program.accept(renamer);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
    }

    @Test
    public void test4_recfunction() {
        Parser parser = new CupParser(
                "let function foo() : int = bar() function bar() : int = foo() function foobar() : int = let function foofoo() : int = bar() in 1 end in 0 end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);

        program.accept(binder);
        Renamer renamer = new Renamer();
        program.accept(renamer);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
    }

    @Test
    public void test4_string() {
        Parser parser = new CupParser(
                "let type a = { a: string } function a(a: a): a = a { a = a.a + a.a } var a : a := a(a{a = \"\"}) in a.a end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);

        program.accept(binder);
        Renamer renamer = new Renamer();
        program.accept(renamer);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
    }


}
