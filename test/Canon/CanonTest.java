package Canon;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;

import Translate.Access;
import Frame.Frame;
import Intel.IntelFrame;
import Temp.Label;
import Translate.Exp;
import Translate.FragList;
import Translate.FragmentPrinter;
import Translate.Level;
import Translate.Translator;
import Translate.TranslatorVisitor;
import Tree.TEMP;

import Absyn.Absyn;
import Bind.Binder;
import ErrorMsg.ErrorMsg;
import FindEscape.EscapeVisitor;
import Parse.ParserFactory;
import Parse.ParserService;
import Tree.PrettyPrinter;
import Tree.Stm;
import Tree.XmlPrinter;
import junit.framework.AssertionFailedError;


public class CanonTest {

    private ParserService parserService;
    private CanonVisitor canonVisitor;

    public CanonTest() {
        parserService = new ParserService(new ParserFactory());
    }

    @Before
    public void setup() {
        this.canonVisitor = new CanonVisitor(new CanonicalizationImpl());
    }

    @Test
    public void liftEseqs() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("3 + 5", new ErrorMsg("", System.out));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
        fragList.accept(this.canonVisitor);
        this.canonVisitor.fragList.accept(new FragmentPrinter(System.out));
        assertTrue(false);
    }

    @Test
    public void liftSeqs() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("3 + 5", new ErrorMsg("", System.out));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
        fragList.accept(this.canonVisitor);
        this.canonVisitor.fragList.accept(new FragmentPrinter(System.out));
        assertTrue(false);
    }

    @Test
    public void extractCalls() {
        TranslatorVisitor translator = new TranslatorVisitor();
        assertNotNull(translator);
        Absyn program = parserService.parse("3 + 5", new ErrorMsg("", System.out));
        program.accept(translator);
        FragList fragList = translator.getFragList();
        assertNotNull(fragList);
        fragList.accept(this.canonVisitor);
        this.canonVisitor.fragList.accept(new FragmentPrinter(System.out));
        assertTrue(false);
    }
}
