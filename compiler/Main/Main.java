package Main;

import java.io.FileNotFoundException;
import java.io.InputStream;

import ErrorMsg.ErrorMsg;
import Frame.Frame;
import Intel.IntelFrame;
import Parse.Grm;
import Parse.Program;
import Parse.Yylex;
import Semant.Semant;
import Symbol.SymbolTable;
import Translate.Level;
import Types.Type;

public class Main {

    public static void main(final String[] args) throws FileNotFoundException {
        new Main(args[0]);
    }

    private InputStream inputStream;
    private String name;
    private Program ast;
    private Semant semant;
    private ErrorMsg errorMsg;
    private Grm parser;

    public Main(final String filename) throws FileNotFoundException {
        this(filename, new java.io.FileInputStream(filename));
    }

    public Main(final String name, final InputStream inputStream) {
        this.name = name;
        this.inputStream = inputStream;
        this.errorMsg = new ErrorMsg(this.name);
        this.parser = new Grm(new Yylex(this.inputStream, this.errorMsg), this.errorMsg);
        this.semant = new Semant(errorMsg, new Level(new IntelFrame(null, null)));
    }

    public ErrorMsg getErrorMsg() {
        return this.errorMsg;
    }

    /**
     * Returns the symbol table for types. Used for testing
     */
    public SymbolTable<Type> getTypeSymbolTable() {
        return semant.getEnv().getTEnv();
    }

    public int compile() {
        this.buildAst();
        this.typeCheck();
        return 1;
    }

    public boolean hasErrors() {
        return this.errorMsg.getCompilerErrors().size() != 0;
    }

    public void print() {
        new Absyn.Print(System.out).prExp(this.ast.absyn, 0);
    }

    private void buildAst() {
        try {
            final java_cup.runtime.Symbol rootSymbol = parser.parse();
            this.ast = (Program) rootSymbol.value;
        } catch (final Throwable e) {
            throw new Error("Unable to translate", e);
        } finally {
            try {
                this.inputStream.close();
            } catch (final java.io.IOException e) {
                throw new Error(e.toString());
            }
        }
    }

    private void typeCheck() {
        this.semant.findEscape(this.ast.absyn);
        this.semant.transExp(this.ast.absyn);
    }

}