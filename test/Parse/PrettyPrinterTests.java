package Parse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.Test;

import Absyn.PrettyPrinter;
import ErrorMsg.ErrorMsg;

public class PrettyPrinterTests {

    private final ByteArrayOutputStream baos;

    public PrettyPrinterTests() {
        this.baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
    }

    @Test
    public void nestedParens() {
        this.baos.reset();
        ErrorMsg errorMsg = new ErrorMsg("errorRecovery", System.out);
        String tiger = "((((((((((0))))))))))";
        InputStream targetStream = new ByteArrayInputStream(tiger.getBytes());
        Program program = new CupParser(targetStream, errorMsg).parse();
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.absyn.accept(prettyPrinter);
        assertTrue(!errorMsg.anyErrors);
        assertEquals(this.baos.toString(), "(\n    0\n)");
    }

    @Test
    public void booleanAndOpSugar() {
        this.baos.reset();
        ErrorMsg errorMsg = new ErrorMsg("errorRecovery", System.out);
        String tiger = "1 = 1 & 2 = 2";
        InputStream targetStream = new ByteArrayInputStream(tiger.getBytes());
        Program program = new CupParser(targetStream, errorMsg).parse();
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.absyn.accept(prettyPrinter);
        assertTrue(!errorMsg.anyErrors);
        assertEquals(this.baos.toString(), "if 1 = 1 \nthen 2 = 2\nelse 0");
    }

    @Test
    public void booleanOrOpSugar() {
        this.baos.reset();
        ErrorMsg errorMsg = new ErrorMsg("errorRecovery", System.out);
        String tiger = "1 = 1 | 2 = 2";
        InputStream targetStream = new ByteArrayInputStream(tiger.getBytes());
        Program program = new CupParser(targetStream, errorMsg).parse();
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, true, true);
        program.absyn.accept(prettyPrinter);
        assertTrue(!errorMsg.anyErrors);
        assertEquals(this.baos.toString(), "if 1 = 1 \nthen 1\nelse 2 = 2");
    }
}