package Parse;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;

import Absyn.DecList;
import Absyn.PrettyPrinter;
import ErrorMsg.ErrorMsg;

public class PrimitiveTest {

    @Test
    public void wrapExp() throws FileNotFoundException {
        ErrorMsg errorMsg = new ErrorMsg("Dec Wrapper", System.out);
        String tiger = "1";

        InputStream targetStream = new ByteArrayInputStream(tiger.getBytes());
        ParserService parserService = new ParserService(new ParserFactory());
        DecList decList = parserService.parse(targetStream, errorMsg);
        decList.accept(new PrettyPrinter(System.out));
        

    }
}
