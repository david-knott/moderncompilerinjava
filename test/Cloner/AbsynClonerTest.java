package Cloner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import Absyn.Absyn;
import Absyn.PrettyPrinter;
import Bind.Binder;
import ErrorMsg.ErrorMsg;
import Parse.CupParser;
import Parse.Parser;
import Parse.ParserFactory;
import Parse.ParserService;
import Parse.Program;

public class AbsynClonerTest {

    @Test
    public void typeDef() throws FileNotFoundException {
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        ParserService parserService = new ParserService(new ParserFactory());
        Absyn program = parserService.parse("let type a = int var a:a := 1 in a end", errorMsg);
        AbsynCloner absynCloner = new AbsynCloner();
        program.accept(absynCloner);
        //create an outstream and write new pretty printed program to it
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrettyPrinter prettyPrinter = new PrettyPrinter(new PrintStream(outputStream));
        absynCloner.visitedDecList.accept(prettyPrinter);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        //System.out.println(new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
        Absyn program2 = parserService.parse(inputStream, errorMsg);
    }
}