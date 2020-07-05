package Translate;

import java.io.PrintStream;

import Assem.InstrList;
import Assem.MOVE;
import Canon.CanonFacadeImpl;
import Canon.Canonicalization;
import Core.CompilerEventType;
import Frame.Frame;
import Frame.Proc;
import RegAlloc.RegAllocCoalesce;
import Temp.CombineMap;
import Temp.Temp;
import Temp.TempMap;
import Tree.StmList;

/**
 * Function assembly fragment which also contains the frame for the procedure.
 * The frame layout contains information about locals and variables. The body
 * contains the IL code, which is returned from the procEntryExit1
 */
public class ProcFrag extends Frag {

    public static CompilerEventType CANONICAL_COMPLETE = new CompilerEventType("Canon");

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


    /**
     * Applies canonicalization, blocks and traces algorithms to the contained IR
     * tree. The produces a list of sequences without side affects and with the
     * correct flow which can be easily translated into assembly.
     */
    @Override
    public void process(PrintStream out) {
        StmList stmList = canonicalization.canon(this.body);
        this.trigger(CANONICAL_COMPLETE, stmList);
        Assem.InstrList instrs = codegen(this.frame, stmList);
        instrs = this.frame.procEntryExit2(instrs);
        //RegAlloc regAlloc = new RegAlloc(this.frame, instrs);
        RegAllocCoalesce regAlloc = new RegAllocCoalesce(this.frame, instrs);
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
                //remove moves with same src and dest
             //   if(tempMap.tempMap(def) != tempMap.tempMap(use)) {
                    out.println(body.head.format(tempMap));
             //   }
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