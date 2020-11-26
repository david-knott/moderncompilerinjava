package Translate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

import Absyn.Absyn;
import Bind.Binder;
import ErrorMsg.ErrorMsg;
import FindEscape.EscapeVisitor;
import Parse.ParserFactory;
import Parse.ParserService;


public class TranslateVisitorTest {

    private ParserService parserService;

    public TranslateVisitorTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Test
    public void canCreateInstance() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
    }

    @Test
    public void translateEmpty() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("/* just a comment */", new ErrorMsg("", System.out));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        assertNotNull(fragList);
    }

    @Test
    public void translateInt() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("3", new ErrorMsg("", System.out));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        assertNotNull(fragList);
    }

    @Test
    public void translateAdd() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("3 + 5", new ErrorMsg("", System.out));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        assertNotNull(fragList);
    }

    @Test
    public void translateVarDec() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("var a:int := 1", new ErrorMsg("", System.out));
        program.accept(translator);
        System.out.println("done");
    }

    @Test
    public void translateVarDecUsage() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("var a:int := 1 var b:int := a", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        System.out.println("done");
    }
    
    @Test
    public void primitiveUsage() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("print(1)", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        System.out.println("done");
    }

    @Test
    public void forTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("for a := 3 to 13 do ()", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
    }

    @Test
    public void whileTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("while -2 > 3  do ()", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
    }

    @Test
    public void recordExpZeroFieldTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let type rec = { } var v := rec {  } in end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        // expect 1 fragment, with zero SEQ nodes.
    }


    @Test
    public void recordExpOneFieldTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let type rec = { a : int } var v := rec { a = 1 } in end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        // expect 1 fragment, with 0 SEQ node and a MOVE node with offset 0 from record pointer.
    }

    @Test
    public void recordExpTwoFieldsTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let type rec = { a : int, b : int } var v := rec { a = 1, b = 2} in end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        // expect 1 fragment, with 1 SEQ node, 
        // with left containing MOVE with offset 0 from record pointe.
        // right containing MOVE with offset 8 from record pointer.
    }

    @Test
    public void recordExpThreeFieldsTest() {
        TranslatorVisitor translator = new TranslatorVisitor();
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("let type rec = { a : int, b : int, c: int } var v := rec { a = 1, b = 2, c = 3} in end", errorMsg);
        program.accept(new EscapeVisitor(errorMsg));
        program.accept(new Binder(errorMsg));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        fragList.accept(new FragmentPrinter(System.out));
        // expect 1 fragment, with 2 SEQ nodes, 
        // with left containing MOVE with offset 0 from record pointe.
        // with right containing SEQ node
        // with left containing MOVE with offset 8 from record pointe.
        // right containing MOVE with offset 16 from record pointer.

    }
}
