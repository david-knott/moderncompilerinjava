package Parse;

import java.io.InputStream;

import ErrorMsg.ErrorMsg;
import Util.Assert;
import Util.TaskContext;

/**
 * Parses an input stream and reports any lexical or parse errors.
 * This implementation uses the JLex Lexer & Cup Parser. 
 */
public class CupParser implements Parser {

    boolean parserTrace = false;
    final InputStream inputStream;
    final ErrorMsg errorMsg;
    final Yylex yylex;
    Program ast;

    public CupParser(InputStream inputStream, ErrorMsg errorMsg) {
        Assert.assertNotNull(inputStream);
        this.inputStream = inputStream;
        this.errorMsg = errorMsg;
        this.yylex = new Yylex(inputStream, errorMsg);
    }

    @Override
    public void parse(TaskContext context) {
        Grm parser = new Grm(new DebugLexer(this.yylex, System.out), this.errorMsg);
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
        if (!this.errorMsg.anyErrors) {
            context.setAst((Program)rootSymbol.value);
        }
    }

    @Override
    public void setParserTrace(boolean value) {
        this.parserTrace = value;
    }
}