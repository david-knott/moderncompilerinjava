package Parse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import Absyn.Absyn;
import ErrorMsg.ErrorMsg;
import Util.Assert;

/**
 * Parses an input stream and reports any lexical or parse errors. This
 * implementation uses the JLex Lexer & Cup Parser.
 */
public class CupParser implements Parser {

    boolean parserTrace = false;
    final InputStream inputStream;
    final ErrorMsg errorMsg;
    Program ast;

    public CupParser(InputStream inputStream, ErrorMsg errorMsg) {
        Assert.assertNotNull(inputStream);
        Assert.assertNotNull(errorMsg);
        this.inputStream = inputStream;
        this.errorMsg = errorMsg;
    }

    public CupParser(String string, ErrorMsg errorMsg) {
        Assert.assertNotNull(string);
        Assert.assertNotNull(errorMsg);
        this.inputStream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
        this.errorMsg = errorMsg;
    }

    @Override
    public Absyn parse() {
        Yylex yylex = new Yylex(inputStream, errorMsg);
        Grm parser = new Grm(new DebugLexer(yylex, System.out), this.errorMsg);
        java_cup.runtime.Symbol rootSymbol = null;
        try {
            rootSymbol = !this.parserTrace ? parser.parse() : parser.debug_parse();
        } catch (Throwable e) {
            throw new Error(e);
        } finally {
            try {
                inputStream.close();
            } catch (final java.io.IOException e) {
                throw new Error(e.toString());
            }
        }
        //return rootSymbol != null ? (Program) rootSymbol.value : null;
        return rootSymbol != null ? (Absyn) rootSymbol.value : null;
    }

    @Override
    public boolean hasErrors() {
        return this.errorMsg.anyErrors;
    }

    @Override
    public void setParserTrace(boolean value) {
        this.parserTrace = value;
    }
}