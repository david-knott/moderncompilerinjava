package Type;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintStream;

import org.junit.Test;

import Absyn.PrettyPrinter;
import Bind.Binder;
import ErrorMsg.ErrorMsg;
import Parse.CupParser;
import Parse.Parser;
import Parse.Program;
import Types.TypeChecker;

public class TypeTest {

    @Test
    public void test_4_31a() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("1 + \"2\"",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }
 
    @Test
    public void test_4_31b() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("\"1\" + 2",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void test_4_32() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("/* error: index variable erroneously assigned to.  */ for i := 10 to 1 do i := i - 1", errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void varDec() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var a:string := 1 in end",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void ifThenElseReturnType() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let in if (1) then 2 else \"string\" end",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void ifThenElseTestType() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let in if (\"string\") then 2 else 3 end",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }
}