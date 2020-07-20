package Parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import Absyn.Print;
import ErrorMsg.ErrorMsg;
import Util.Assert;
import java_cup.runtime.Symbol;


class LexicalError extends Exception {

}

class DebugLexer implements Lexer {

    final Yylex yylex;
    final PrintStream printStream;

    public DebugLexer(Yylex yylex, PrintStream printStream) {
        this.yylex = yylex;
        this.printStream = printStream;
    }

    @Override
    public Symbol nextToken() throws IOException {
        Symbol nextToken = yylex.nextToken();
     //   this.printStream.println("Token:" + nextToken.sym);
        return nextToken;
    }
}

/**
 * Parses an input stream and reports any lexical or parse errors
 */
public class ParseTask {

    final boolean debug = false;
    final InputStream inputStream;
    final ErrorMsg errorMsg;
    final Yylex yylex;
    Program ast;

    public ParseTask(InputStream inputStream, ErrorMsg errorMsg) {
        this.inputStream = inputStream;
        this.errorMsg = errorMsg;
        this.yylex = new Yylex(inputStream, errorMsg);
    }

    public void execute() {
        Grm parser = new Grm(new DebugLexer(this.yylex, System.out), this.errorMsg);
        java_cup.runtime.Symbol rootSymbol = null;
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