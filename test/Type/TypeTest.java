package Type;

import static org.junit.Assert.assertFalse;
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
    public void ifThenElseOk() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in r := (if (1) then 2 else 3) end",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void ifThenElseAssignType() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:string := \"\" in r := (if (1) then 2 else 3) end",errorMsg);
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
        Parser parser = new CupParser("let var r:int := 0 in r := (if (1) then 2 else \"string\") end",errorMsg);
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
        Parser parser = new CupParser("let var r:int := 0 in r := (if (\"string\") then 2 else 3) end",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void sequenceAssign() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in r := (1; 2; 3; 4; \"string\") end",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void sequenceAssignVoid() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in r := (1; 2; 3; 4; r := r + 1) end",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void sequenceAssignOk() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in r := (1; 2; 3; 4; 5) end",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void whileTestType() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in while (\"string\") do r := r + 1 end",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void whileBodyType() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in while (1) do r end",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void whileOk() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in while (1) do r := r + 1 end",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void whileOk2() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let var r:int := 0 in while (1) do () end",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void functionInvalidArgType() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let function a(a:int, b:int) = () in a(1, \"string\") end",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void functionMissingArgs() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let function a(a:int, b:int) = () in a() end",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void functionExtraArgs() {
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Parser parser = new CupParser("let function a(a:int, b:int) = () in a(1, 2, 3) end",errorMsg);
        Program program = parser.parse();
        Binder binder = new Binder(errorMsg);
        program.absyn.accept(binder);
        program.absyn.accept(new TypeChecker(errorMsg));
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.absyn.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }





}