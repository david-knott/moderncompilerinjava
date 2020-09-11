package Parse;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import Absyn.PrettyPrinter;
import ErrorMsg.ErrorMsg;

public class PrettyPrinterTests {

    @Test
    public void nestedParens() {
        ErrorMsg errorMsg = new ErrorMsg("errorRecovery", System.out);
        String tiger = "((((((((((0))))))))))";
        InputStream targetStream = new ByteArrayInputStream(tiger.getBytes());
        Program program = new CupParser(targetStream, errorMsg).parse();
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.absyn.accept(prettyPrinter);
        assertTrue(!errorMsg.anyErrors);
    }
}