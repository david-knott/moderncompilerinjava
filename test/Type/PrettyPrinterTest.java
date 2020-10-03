package Type;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintStream;

import org.junit.Test;

import Bind.Binder;
import ErrorMsg.ErrorMsg;
import Parse.CupParser;
import Parse.Parser;
import Parse.Program;
import Symbol.Symbol;
import Types.Constants;
import Types.INT;
import Types.PrettyPrinter;
import Types.RECORD;
import Types.STRING;
import Types.TypeChecker;


public class PrettyPrinterTest {

    @Test
    public void intprinter() {
        INT tint = Constants.INT;
        tint.accept(new PrettyPrinter(System.out));
    }
     
    @Test
    public void stringprinter() {
        STRING tstring = Constants.STRING;
        tstring.accept(new PrettyPrinter(System.out));
    }
     
    @Test
    public void recordprinter() {
        STRING tstring = Constants.STRING;
        INT tint = Constants.INT;
        RECORD record = new RECORD(Symbol.symbol("sfield"), tstring, new RECORD(Symbol.symbol("ifield"), tint, null));
        record.accept(new PrettyPrinter(System.out));
    }
}
