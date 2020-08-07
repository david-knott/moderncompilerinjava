package Main;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import Absyn.Exp;
import Absyn.PrettyPrinter;
import Core.Listener;
import ErrorMsg.ErrorMsg;
import Intel.IntelFrame;
import Parse.Grm;
import Parse.Program;
import Parse.Yylex;
import Temp.Label;
import Translate.Frag;
import Translate.FragList;
import Translate.Level;
import Translate.ProcFrag;
import Translate.Translator;
import Tree.Stm;
import Tree.StmList;
import Util.TaskRegister;

class Options {

}

/**
 * Main class that executes the compiler.
 */
public class Main {

    public static void main(final String[] args) throws FileNotFoundException {
        // new Main(args[0]);
        PrintStream out = new PrintStream(new java.io.FileOutputStream(args[0] + ".s"));
        InputStream in = new java.io.FileInputStream(args[0]);
        PrintStream err = System.err;
        ErrorMsg errorMsg = new ErrorMsg(args[0], err);
        TaskRegister.instance
                .setErrorHandler(errorMsg)
                .setIn(in)
                .setOut(out)
                .register(new Parse.Tasks())
                .register(new FindEscape.Tasks())
                .register(new Absyn.Tasks())
          //      .register(new Frame.Tasks())
          //      .register(new Semant.Tasks())
                .register(new Translate.Tasks())
                .register(new Intel.Tasks())
                .parseArgs(args)
                .execute();
    }

    public static void main2(final String[] args) throws FileNotFoundException {

    }

    /**
     * If true, all variables should be stored on the stack, otherwise variables are
     * stored in temporaries if they do not escape.
     */
    private boolean allVarsEscape = false;

    /**
     * Compiles the program. Using the parser we parse the source code and create an
     * abstract syntax tree. The abstract syntax tree is traversed and we mark
     * variables as escaping ( in frame ) or non escaping ( in register ). Semantic
     * analysis is performed to check for type consistency and semantic meaning that
     * cannot be infered from the grammar. The semantic analysis phase then produces
     * an internediate form of the language that can be converted into assembly. The
     * intermediate form is a list of fragments, either data fragment for strings or
     * code fragment for a function.
     * 
     * @return
     */
    public Main(final String name) throws FileNotFoundException {
        // PrintStream out = System.out;
        PrintStream err = System.err;
        InputStream inputStream = new java.io.FileInputStream(name);
        ErrorMsg errorMsg = new ErrorMsg(name, err);
        // lexing and parsing
        Grm parser = new Grm(new Yylex(inputStream, errorMsg), errorMsg);
        Program ast = null;
        try {
            java_cup.runtime.Symbol rootSymbol = parser.parse();
            ast = (Program) rootSymbol.value;
        } catch (Throwable e) {
            throw new Error("Unable to parse, syntax error", e);
        } finally {
            try {
                inputStream.close();
            } catch (final java.io.IOException e) {
                throw new Error(e.toString());
            }
        }

        // find variables that must be stored in frame
        FindEscape.FindEscape findEscape = new FindEscape.FindEscape(allVarsEscape);
        findEscape.traverse(ast.absyn);
        // semantic analysis & translation to IR
        Frame.Frame frame = new IntelFrame(Label.create("tigermain"), null);
        Level topLevel = new Level(frame);
        Translator translate = new Translator();
        Semant.Semant semant = new Semant.Semant(errorMsg, topLevel, translate);
        FragList frags = FragList.reverse(semant.getTreeFragments(ast.absyn));
        if (semant.hasErrors()) {
            System.out.println("semant check error");
            System.exit(1);
        }
        // codegen, liveness, register allocation
        PrintStream fileOut = null;
        try {
            fileOut = new PrintStream(new java.io.FileOutputStream(name + ".s"));
            fileOut.println(".global tigermain");
            for (; frags != null; frags = frags.tail) {
                // this.registerFragListners(frags.head);
                frags.head.process(fileOut);
            }
            fileOut.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            throw new Error(e1.toString());
        } finally {
            if (fileOut != null) {
                fileOut.close();
            }
        }
    }
}