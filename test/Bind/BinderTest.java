package Bind;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintStream;

import org.junit.Test;

import Absyn.Absyn;
import Absyn.PrettyPrinter;
import ErrorMsg.ErrorMsg;
import Parse.CupParser;
import Parse.Parser;
import Parse.Program;

public class BinderTest {

    @Test
    public void typeDef() {
        Parser parser = new CupParser("let type a = int var a:a := 1 in a end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
        assertTrue(!errorMsg.anyErrors);
    }

    @Test
    public void typeNoDef() {
        Parser parser = new CupParser("let type a = int var a:b := 1 in a end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }


    @Test
    public void arrayTypeDef() {
        Parser parser = new CupParser("let type intArray = array of int var a := intArray [ 10 ] of 3 in a[3] end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
        assertTrue(!errorMsg.anyErrors);
    }

    @Test
    public void arrayTypeNoDef1() {
        Parser parser = new CupParser("let type intArray = array of int var a := badArray [ 10 ] of 3 in a[3] end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void arrayTypeNoDef2() {
        Parser parser = new CupParser("let type intArray = array of bint var a := intArray [ 10 ] of 3 in a[3] end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void arrayTypeNoDef3() {
        Parser parser = new CupParser("let type intArray = array of bint var a := badArray [ 10 ] of 3 in a[3] end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }


    @Test
    public void recTypeDef() {
        Parser parser = new CupParser("let type tuple = {first: int, second: int} var t1: tuple := tuple {first = 1, second = 2} in t1.first end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void recursiveTypeDefs() {
        Parser parser = new CupParser("let type list = {first: int, rest: list} in 1 end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void varDefWithType() {
        Parser parser = new CupParser("let var a:int := 0 in a + a end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void varDefBinop() {
        Parser parser = new CupParser("let var a:int := 3 + 4 in a end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void varDefWithoutType() {
        Parser parser = new CupParser("let var a := 0 in a end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void procedureDef() {
        Parser parser = new CupParser("let function a() = (1) in a() end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void functionDef() {
        Parser parser = new CupParser("let function a():int = 1 in a() end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);

        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
    }

    @Test
    public void functionNoDef() {
        Parser parser = new CupParser("let function a():int = 1 in b() end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);

        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }


    @Test
    public void functionArgsDef() {
        Parser parser = new CupParser("let function a(a: int, b:int):int = a + b in a(3,4) end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);

        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void scopeVarDef() {
        Parser parser = new CupParser("let function a() = let var b:int := 1 + 3 in end in a() end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);

        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void breakTest() {
        Parser parser = new CupParser(
                "let var x := 3 in while 1 do ( for i := 3 to 10 do ( x := x + i; if x >= 42 then break ); if x >= 51 then break ) end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertFalse(errorMsg.anyErrors);
    }

    @Test
    public void badBreakTest() {
        Parser parser = new CupParser("let var x:int := 3 in if x >= 42 then break end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);

        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void test4_19() {
        Parser parser = new CupParser("let var me := 0 in me end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertTrue(!errorMsg.anyErrors);
    }

    @Test
    public void test4_20() {
        Parser parser = new CupParser("let var me := 0 function id(me : int) : int = me in print_int(me) end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);
        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertTrue(!errorMsg.anyErrors);
    }

    @Test
    public void test4_21() {
        Parser parser = new CupParser("me", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);

        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void test4_25() {
        Parser parser = new CupParser(
                "let type me = {} type me = {} function twice(a: int, a: int) : int = a + a in me {} = me {} end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);

        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void test4_26() {
        Parser parser = new CupParser(
                "let var x := 0 in while 1 do ( for i := 0 to 10 do ( x := x + i; if x >= 42 then break ); if x >= 51 then break ) end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);

        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertTrue(!errorMsg.anyErrors);
    }

    @Test
    public void test4_27() {
        Parser parser = new CupParser("break", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);

        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }

    @Test
    public void test4_28() {
        Parser parser = new CupParser(
                "let type box = { value : int } type dup = { value : int, value : string } var box := box { value = 51 } in box.head end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);

        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertTrue(!errorMsg.anyErrors);
    }

    @Test
    public void test4_29() {
        Parser parser = new CupParser("let type rec = { a : unknown } in rec { a = 42 } end",
                new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        PrintStream outputStream = System.out;
        ErrorMsg errorMsg = new ErrorMsg("", outputStream);
        Binder binder = new Binder(errorMsg);

        program.accept(binder);
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, true);
        program.accept(prettyPrinter);
        assertTrue(errorMsg.anyErrors);
    }
}