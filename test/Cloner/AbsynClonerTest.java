package Cloner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintStream;

import org.junit.Test;

import Absyn.Absyn;
import Absyn.PrettyPrinter;
import Bind.Binder;
import ErrorMsg.ErrorMsg;
import Parse.CupParser;
import Parse.Parser;
import Parse.Program;

public class AbsynClonerTest {

     @Test
    public void typeDef() {
        Parser parser = new CupParser("let type a = int var a:a := 1 in a end", new ErrorMsg("", System.out));
        Absyn program = parser.parse();
        AbsynCloner absynCloner = new AbsynCloner();
        program.accept(absynCloner);
        PrintStream outputStream = System.out;
        PrettyPrinter prettyPrinter = new PrettyPrinter(System.out, false, false);
        program.accept(prettyPrinter);
        absynCloner.visitedExp.accept(prettyPrinter);

    }


}
