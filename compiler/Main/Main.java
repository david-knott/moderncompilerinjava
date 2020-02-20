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
import Semant.Semant;
import Symbol.SymbolTable;
import Temp.DefaultMap;
import Temp.TempMap;
import Translate.DataFrag;
import Translate.Frag;
import Translate.Level;
import Translate.ProcFrag;
import Translate.Translate;
import Tree.Print;
import Tree.StmList;
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
    private Frame frame = new IntelFrame(null, null);

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
        this.semant = new Semant(errorMsg, new Level(frame), new Translate());
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
        TempMap tempmap= new Temp.CombineMap(procFrag.frame,new Temp.DefaultMap());
        var print = new Print(out, tempmap);
   //     out.println("# Before canonicalization: ");
   //     print.prStm(procFrag.body);
        StmList stms = Canon.linearize(procFrag.body);
   //     out.println("# After canonicalization: ");
   //     prStmList(print, stms);
   //     out.println("# Basic Blocks: ");
        BasicBlocks b = new BasicBlocks(stms);
        for (StmListList l = b.blocks; l != null; l = l.tail) {
     //       out.println("#");
       //     prStmList(print, l.head);
        }
//        print.prStm(new Tree.LABEL(b.done));
       out.println("# Trace Scheduled: ");
        StmList traced = (new TraceSchedule(b)).stms;
        prStmList(print, traced);
        Assem.InstrList instrs = codegen(procFrag.frame, traced);
        out.println("# Instructions: ");
        for (Assem.InstrList p = instrs; p != null; p = p.tail)
            out.print(p.head.format(tempmap));

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
        new FindEscape(this.ast.absyn);
        var frags = this.semant.transProg(this.ast.absyn);
        for (Frag frag = frags; frag != null; frag = frag.next) {
            if (frag instanceof ProcFrag) {
                emitProcFrag(out, (ProcFrag)frag);
            } else {
                new Print(out).prExp(((DataFrag) frag).stringFragment);
            }
        }
        out.close();
        return 1;
    }

    public boolean hasErrors() {
        return this.errorMsg.getCompilerErrors().size() != 0;
    }
}