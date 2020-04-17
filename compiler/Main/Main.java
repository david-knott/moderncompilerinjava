package Main;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import Assem.InstrList;
import Canon.BasicBlocks;
import Canon.Canon;
import Canon.StmListList;
import Canon.TraceSchedule;
import ErrorMsg.ErrorMsg;
import FindEscape.FindEscape;
import Frame.Frame;
import Intel.IntelFrame;
import Parse.Grm;
import Parse.Program;
import Parse.Yylex;
import RegAlloc.RegAlloc;
import Semant.Semant;
import Symbol.SymbolTable;
import Temp.TempMap;
import Translate.Frag;
import Translate.Level;
import Translate.ProcFrag;
import Translate.Translate;
import Tree.Print;
import Tree.StmList;
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

    private void emitProcFrag(PrintStream out, ProcFrag procFrag) {
        TempMap tempmap = new Temp.CombineMap(procFrag.frame, new Temp.DefaultMap());
        var print = new Print(out, tempmap);
         out.println("# Before canonicalization: ");
         print.prStm(procFrag.body);
        StmList stms = Canon.linearize(procFrag.body);
        // out.println("# After canonicalization: ");
        // prStmList(print, stms);
        // out.println("# Basic Blocks: ");
        BasicBlocks b = new BasicBlocks(stms);
        for (StmListList l = b.blocks; l != null; l = l.tail) {
            // out.println("#");
            // prStmList(print, l.head);
        }
        // print.prStm(new Tree.LABEL(b.done));
        out.println("# Trace Scheduled: ");
        StmList traced = (new TraceSchedule(b)).stms;
        prStmList(print, traced);
        Assem.InstrList instrs = codegen(procFrag.frame, traced);
        instrs = procFrag.frame.procEntryExit2(instrs);
        out.println("# Instructions: ");
        out.println("section .text");
        RegAlloc regAlloc = new RegAlloc(frame, instrs);
        for (Assem.InstrList p = instrs; p != null; p = p.tail)
            out.print(p.head.format(regAlloc));

        // buildInterferenceGraph(instrs);

        var procs = procFrag.frame.procEntryExit3(instrs);

    }

    public boolean hasErrors() {
        return this.errorMsg.getCompilerErrors().size() != 0;
    }

    private InstrList codegen(Frame f, StmList stms) {
        Assem.InstrList first = null, last = null;
        for (Tree.StmList s = stms; s != null; s = s.tail) {
            Assem.InstrList i = f.codegen(s.head);
            if (last == null) {
                first = last = i;
            } else {
                while (last.tail != null)
                    last = last.tail;
                last = last.tail = i;
            }
        }
        return first;
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
        // find escaping variables
        findEscape.traverse(this.ast.absyn);
        var frags = this.semant.transProg(this.ast.absyn);
        for (Frag frag = frags; frag != null; frag = frag.next) {
            if (frag instanceof ProcFrag) {
                // write function data
                emitProcFrag(out, (ProcFrag) frag);
            } else {
                // write string data
                out.println("section .data");
                out.println(frag);
            }
        }
        out.close();
        return 0;
    }
}