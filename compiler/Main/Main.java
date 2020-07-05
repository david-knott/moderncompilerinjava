package Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import Absyn.Absyn;
import Absyn.Exp;
import Absyn.Print;
import Core.Listener;
import ErrorMsg.ErrorMsg;
import FindEscape.FindEscape;
import Frame.Frame;
import Intel.IntelFrame;
import Parse.Grm;
import Parse.Program;
import Parse.Yylex;
import Semant.Semant;
import Temp.Label;
import Translate.ExpTy;
import Translate.Frag;
import Translate.FragList;
import Translate.Level;
import Translate.ProcFrag;
import Translate.Translator;
import Tree.Stm;
import Tree.StmList;

class Options {
    
}
/**
 * Main class that executes the compiler.
 */
public class Main {

    public static void main(final String[] args) throws FileNotFoundException {
        new Main(args[0]);
    }

    /**
     * If true, all variables should be stored on the stack, otherwise variables
     * are stored in temporaries if they do not escape.
     */
    private boolean allVarsEscape = false;

    private static void prStmList(Tree.Print print, Tree.StmList stms) {
        for (Tree.StmList l = stms; l != null; l = l.tail) {
            print.prStm(l.head);
        }
    }

    private void registerFragListners(Frag frag) {
        frag.on(ProcFrag.CANONICAL_COMPLETE, new Listener<StmList>() {
            @Override
            public void handle(StmList message) {
                System.out.println("### Cannonical Tree ###");
                for(; message != null; message = message.tail) {
                    new Tree.Print(System.out).prStm(message.head);
                }
            }
        });
    }

    private void registerListeners(Translator translator) {
        translator.on(Translator.TRANSLATOR_PROC_ENTRY_EXIT_END, new Listener<Stm>() {
            @Override
            public void handle(Stm message) {
                // TODO Auto-generated method stub
                new Tree.Print(System.out).prStm(message);
            }
        });
    }

    private void registerListeners(Semant semant) {
        semant.on(Semant.SEMANT_START, new Listener<Exp>() {
            @Override
            public void handle(Exp message) {
                new Print(System.out).prExp(message);
            }
        });
    }

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
     //   PrintStream out = System.out;
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
        FindEscape findEscape = new FindEscape(allVarsEscape);
        findEscape.traverse(ast.absyn);
        // semantic analysis & translation to IR
        Frame frame = new IntelFrame(Label.create("tigermain"), null);
        Level topLevel = new Level(frame);
        Translator translate = new Translator();
    //    this.registerListeners(translate);
        Semant semant = new Semant(errorMsg, topLevel, translate);
     //   this.registerListeners(semant);
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
                this.registerFragListners(frags.head);
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