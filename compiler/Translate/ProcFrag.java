package Translate;

import java.io.PrintStream;

import Assem.InstrList;
import Canon.CanonFacadeImpl;
import Canon.Canonicalization;
import Frame.Frame;
import RegAlloc.RegAlloc;
import Temp.TempMap;
import Tree.Print;
import Tree.StmList;

interface CodeGenerator {

    public InstrList codegen(Frame f, StmList stms);
}

/**
 * Function assembly fragment which also contains the frame for the procedure.
 * The frame layout contains information about locals and variables. The body
 * contains the IL code, which is returned from the procEntryExit1
 */
public class ProcFrag extends Frag {

    /**
     * Intermediate representation function body
     */
    public final Tree.Stm body;
    /**
     * Activation record for this function
     */
    public final Frame frame;

    private final Canonicalization canonicalization;

    /**
     * Initialises a new instance of a ProcFrag
     * 
     * @param bdt the function body
     * @param frm the activation record for this function
     */
    public ProcFrag(Tree.Stm bdt, Frame frm) {
        body = bdt;
        frame = frm;
        canonicalization = new CanonFacadeImpl();
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

    private static void prStmList(Tree.Print print, Tree.StmList stms) {
        for (Tree.StmList l = stms; l != null; l = l.tail) {
            print.prStm(l.head);
        }
    }

    /**
     * Applies canonicalization, blocks and traces algorithms to the contained IR
     * tree. The produces a list of sequences without side affects and with the
     * correct flow which can be easily translated into assembly.
     */
    @Override
    public void process(PrintStream out) {
        StmList stmList = canonicalization.canon(this.body);
        TempMap tempmap = new Temp.CombineMap(this.frame, new Temp.DefaultMap());
        var print = new Print(out, tempmap);
        out.println("# Before canonicalization: ");
        print.prStm(this.body);
        out.println("# After canonicalization: ");
        prStmList(print, stmList);
        Assem.InstrList instrs = codegen(this.frame, stmList);
        instrs = this.frame.procEntryExit2(instrs);
        RegAlloc regAlloc = new RegAlloc(this.frame, instrs);
        this.frame.procEntryExit3(instrs);
        out.println("# Instructions: ");
        out.println("section .text");
        for(; instrs != null; instrs = instrs.tail) {
            out.print(instrs.head.format(regAlloc));
        }
    }

    @Override
    public Frame getFrame() {
        return this.frame;
    }

    

}