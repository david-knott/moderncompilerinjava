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


}
