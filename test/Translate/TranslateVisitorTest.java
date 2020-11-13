package Translate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
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
        Absyn program = parserService.parse("1", new ErrorMsg("", System.out));
        program.accept(translator);
        System.out.println("done");
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
}
