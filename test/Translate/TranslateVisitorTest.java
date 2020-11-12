package Translate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;

import Absyn.Absyn;
import ErrorMsg.ErrorMsg;
import Parse.ParserFactory;
import Parse.ParserService;


public class TranslateVisitorTest {

    private ParserService parserService;

    public TranslateVisitorTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Test
    public void canCreateInstance() {
        TranslatorV translator = new TranslatorV();
        assertNotNull(translator);
    }

    @Test
    public void translateInt() {

        TranslatorV translator = new TranslatorV();
        assertNotNull(translator);
        ErrorMsg errorMsg = new ErrorMsg("", System.out);
        Absyn program = parserService.parse("1", new ErrorMsg("", System.out));
        program.accept(translator);
        
    }



}
