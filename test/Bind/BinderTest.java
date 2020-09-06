package Bind;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import ErrorMsg.ErrorMsg;
import Parse.CupParser;
import Parse.Parser;
import Parse.Program;


public class BinderTest {

    @Test
    public void _default() {
        Parser parser = new CupParser("let in end", new ErrorMsg("", System.out));
        Program program = parser.parse();
        Binder binder = new Binder();
        program.absyn.accept(binder);

    }
    
}
