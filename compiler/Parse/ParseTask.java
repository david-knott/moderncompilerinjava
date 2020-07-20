package Parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import Absyn.Print;
import ErrorMsg.ErrorMsg;
import Util.Assert;
import Util.Task;
import java_cup.runtime.Symbol;


class DebugLexer implements Lexer {

    final Yylex yylex;
    final PrintStream printStream;
    final ErrorMsg errorMsg;

    public DebugLexer(Yylex yylex, PrintStream printStream, ErrorMsg errorMsg) {
        this.yylex = yylex;
        this.printStream = printStream;
        this.errorMsg = errorMsg;
    }

    @Override
    public Symbol nextToken() throws IOException {
        Symbol nextToken = yylex.nextToken();
        return nextToken;
    }
}

/**
 * Parses an input stream and reports any lexical or parse errors
 */
public class ParseTask extends Task {

    private static String TASK_NAME = "parse";
    private static String MODULE_NAME = "parse";
    final boolean debug = false;
    final InputStream inputStream;
    final ErrorMsg errorMsg;
    final Yylex yylex;
    Program ast;

    public ParseTask(InputStream inputStream, ErrorMsg errorMsg) {
        super(TASK_NAME, MODULE_NAME, null);
        this.inputStream = inputStream;
        this.errorMsg = errorMsg;
        this.yylex = new Yylex(inputStream, errorMsg);
    }

    @Override
    public void execute() {
        Grm parser = new Grm(new DebugLexer(this.yylex, System.out, this.errorMsg), this.errorMsg);
        java_cup.runtime.Symbol rootSymbol = null;
        // lexical error happens when nextToken is called
        try {
            rootSymbol = !this.debug ? parser.parse() : parser.debug_parse();
            this.ast = (Program) rootSymbol.value;

        } catch (Throwable e) {
            throw new Error(e);
        } finally {
            try {
                inputStream.close();
            } catch (final java.io.IOException e) {
                throw new Error(e.toString());
            }
        }
    }
}