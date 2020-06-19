package Translate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import Assem.InstrList;
import Assem.MOVE;
import Canon.CanonFacadeImpl;
import Canon.Canonicalization;
import Frame.Frame;
import Frame.Proc;
import RegAlloc.RegAlloc;
import Temp.CombineMap;
import Temp.DefaultMap;
import Temp.Temp;
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

    /**
     * Performs canonicalisation on the Tree
     */
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
        TempMap tempmap = new CombineMap(this.frame, new DefaultMap());
        try {
            PrintStream ps = new PrintStream(new FileOutputStream("./tree_" + this.frame.name + ".txt"));
            var print = new Print(ps, tempmap);
            ps.println("# Before canonicalization: ");
            print.prStm(this.body);
            ps.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PrintStream ps = new PrintStream(new FileOutputStream("./canon_" + this.frame.name + ".txt"));
            var print = new Print(ps, tempmap);
            ps.println("# After canonicalization: ");
            prStmList(print, stmList);
            ps.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assem.InstrList instrs = codegen(this.frame, stmList);
        instrs = this.frame.procEntryExit2(instrs);
        RegAlloc regAlloc = new RegAlloc(this.frame, instrs, false);
        TempMap tempMap = new CombineMap(this.frame, regAlloc);
        instrs = regAlloc.instrList;
        Proc proc = this.frame.procEntryExit3(instrs);
        out.println(".text");
        for (InstrList prolog = proc.prolog; prolog != null; prolog = prolog.tail) {
            out.println(prolog.head.format(this.frame));
        }
        for (InstrList body = proc.body; body != null; body = body.tail) {
            if(body.head instanceof MOVE) {
                Temp def = body.head.def().head;
                Temp use = body.head.use().head;
                if(tempMap.tempMap(def) != tempMap.tempMap(use)) {
                }

                    out.println(body.head.format(tempMap));
            }
            else {
                out.println(body.head.format(tempMap));
            }
        }
        for (InstrList epilog = proc.epilog; epilog != null; epilog = epilog.tail) {
            out.println(epilog.head.format(this.frame));
        }
    }

    @Override
    public Frame getFrame() {
        return this.frame;
    }
}