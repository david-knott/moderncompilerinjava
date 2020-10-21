package Parse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import Absyn.PrettyPrinter;
import ErrorMsg.ErrorMsg;

public class DecWrapperTest {

    @Test
    public void wrapExp() {
        ErrorMsg errorMsg = new ErrorMsg("errorRecovery", System.out);
        String tiger = "1 = 1 | 2 = 2";
        InputStream targetStream = new ByteArrayInputStream(tiger.getBytes());
        Program program = new CupParser(targetStream, errorMsg).parse();
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.getDecList().accept(prettyPrinter);
    }

    @Test
    public void wrapCallExp() {
        ErrorMsg errorMsg = new ErrorMsg("errorRecovery", System.out);
        String tiger = "print(\"hi\")";
        InputStream targetStream = new ByteArrayInputStream(tiger.getBytes());
        Program program = new CupParser(targetStream, errorMsg).parse();
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.getDecList().accept(prettyPrinter);
    }

    @Test
    public void wrapFunctionExp() {
        ErrorMsg errorMsg = new ErrorMsg("errorRecovery", System.out);
        String tiger = "let function a():int = 1 in a() end";
        InputStream targetStream = new ByteArrayInputStream(tiger.getBytes());
        Program program = new CupParser(targetStream, errorMsg).parse();
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.getDecList().accept(prettyPrinter);
    }
}