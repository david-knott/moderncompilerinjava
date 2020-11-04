package Absyn;

import java.io.FileNotFoundException;

import org.junit.Test;

import ErrorMsg.ErrorMsg;
import Parse.Parser;
import Parse.ParserFactory;
import Parse.ParserService;

public class PrettyPrinterTest {

    @Test
    public void test4_9() throws FileNotFoundException {
        ParserService parserService = new ParserService(new ParserFactory());
        Absyn program = parserService.parse(
            "let /* Calculate n!. */ function fact (n : int) : int = if  n = 0 then 1 else n * fact (n - 1) in fact (10) end", 
            new ErrorMsg("f", System.out)
        );
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
    }

    @Test
    public void test4_10() throws FileNotFoundException {
        ParserService parserService = new ParserService(new ParserFactory());
        Absyn program = parserService.parse(
            "print(\"\\\"\u0045\u0050ITA\\\"\\n\")",
            new ErrorMsg("f", System.out)
        );
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
    }
}
