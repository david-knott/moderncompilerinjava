package Parse;

import java.io.InputStream;

import ErrorMsg.ErrorMsg;
import Util.Assert;
import Util.Task;
import Util.TaskContext;

/**
 * Parses an input stream and reports any lexical or parse errors
 */
public class ParseTask extends Task {

    boolean parserTrace = false;
    final InputStream inputStream;
    final ErrorMsg errorMsg;
    final Yylex yylex;
    Program ast;

    public ParseTask(InputStream inputStream, ErrorMsg errorMsg) {
        Assert.assertNotNull(inputStream);
        this.inputStream = inputStream;
        this.errorMsg = errorMsg;
        this.yylex = new Yylex(inputStream, errorMsg);
    }

    @Override
    public void execute(TaskContext context) {
        Grm parser = new Grm(new DebugLexer(this.yylex, System.out), this.errorMsg);
        java_cup.runtime.Symbol rootSymbol = null;
        // lexical error happens when nextToken is called
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
}