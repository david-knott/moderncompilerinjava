package Main;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import ErrorMsg.ErrorMsg;
import FindEscape.FindEscape;
import Frame.Frame;
import Intel.IntelFrame;
import Parse.Grm;
import Parse.Program;
import Parse.Yylex;
import Semant.Semant;
import Symbol.SymbolTable;
import Translate.Frag;
import Translate.FragProcessor;
import Translate.Level;
import Translate.Translate;
import Types.Type;

/**
 * Main class that executes the compiler.
 */
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
    // frame implementation
    private Frame frame = new IntelFrame(null, null);
    private Level topLevel = new Level(frame);
    private boolean allVarsEscape = false;
    private FindEscape findEscape = new FindEscape(allVarsEscape);

    static void prStmList(Tree.Print print, Tree.StmList stms) {
        for (Tree.StmList l = stms; l != null; l = l.tail) {
            print.prStm(l.head);
        }
    }

    public Main(final String filename) throws FileNotFoundException {
        this(filename, new java.io.FileInputStream(filename));
    }

    public Main(final String name, final InputStream inputStream) {
        this.name = name;
        this.inputStream = inputStream;
        this.errorMsg = new ErrorMsg(this.name);
        this.parser = new Grm(new Yylex(this.inputStream, this.errorMsg), this.errorMsg);
        this.semant = new Semant(errorMsg, topLevel, new Translate());
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

    public boolean hasErrors() {
        return this.errorMsg.getCompilerErrors().size() != 0;
    }

    /**
     * Compiles the program. Using the parser we parse the source code
     * and create an abstract syntax tree. The abstract syntax tree is
     * traversed and we mark variables as escaping ( in frame ) or non
     * escaping ( in register ). Semantic analysis is performed to check
     * for type consistency and semantic meaning that cannot be infered
     * from the grammar. The semantic analysis phase then produces an
     * internediate form of the language that can be converted into assembly.
     * The intermediate form is a list of fragments, either data fragment for strings
     * or code fragment for a function. 
     * @return
     */
    public int compile() {
        PrintStream out = System.out; // java.io.PrintStream(new java.io.FileOutputStream(args[0] + ".s"));
        //parsse the input stream
        try {
            java_cup.runtime.Symbol rootSymbol = parser.parse();
            this.ast = (Program) rootSymbol.value;
        } catch (Throwable e) {
            throw new Error("Unable to parse, syntax error", e);
        } finally {
            try {
                this.inputStream.close();
            } catch (final java.io.IOException e) {
                throw new Error(e.toString());
            }
        }
        // find and mark escaping variables
        findEscape.traverse(this.ast.absyn);
        // get the IR function fragments
        var frags = this.semant.getTreeFragments(this.ast.absyn);
        //process all the frags and convert them into assembly
        frags.processAll(new FragProcessor(out));
        //close output stream
        out.close();
        return 0;
    }
}