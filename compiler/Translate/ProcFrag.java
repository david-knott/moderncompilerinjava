package Translate;

import Assem.InstrList;
import Canon.Canonicalization;
import Frame.Frame;
import Tree.StmList;

interface CodeGenerator {

    public InstrList codegen(Frame f, StmList stms);
}

/**
 * Function assembly fragment which also contains the frame for the procedure.
 * The frame layout contains 
 * information about locals and variables. The body contains the IL code, which
 * is returned from the procEntryExit1
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
     * Initialises a new instance of a ProcFrag
     * 
     * @param bdt the function body
     * @param frm the activation record for this function
     */
    public ProcFrag(Tree.Stm bdt, Frame frm) {
        body = bdt;
        frame = frm;
    }

    public Object codeGen(CodeGenerator codeGenerator) {
        codeGenerator.codegen(this.frame, null);
        return null;
    }

    /**
     * Applies canonicalization, blocks and traces algorithms
     * to the contained IR tree. The produces a list of sequences without
     * side affects and with the correct flow which can be easily 
     * translated into assembly.
     */
    @Override
    public StmList process(Canonicalization canonicalization, FragProcessor processor) {
        StmList stmList = canonicalization.canon(this.body);
        stmList.frame = this.frame;
        return stmList;
        //Assem.InstrList instrs = codegen(this.frame, stmList);
        // append instruction sink for liveness analysis
        //instrs = this.frame.procEntryExit2(instrs);
    //    out.println("section .text");
        //create interference graph
    //    RegisterAllocator regAlloc = new RegisterAllocator(frame, instrs);
      //  regAlloc.allocate();
     //   TempMap tempmap2 = new Temp.CombineMap(this.frame, regAlloc);
    //    for (Assem.InstrList p = instrs; p != null; p = p.tail)
    //        out.print(p.head.format(tempmap2));
        // buildInterferenceGraph(instrs);
        // var procs = this.frame.procEntryExit3(instrs);*/


        /*
        PrintStream out = processor.getOut();
        TempMap tempmap = new Temp.CombineMap(this.frame, new Temp.DefaultMap());
        var print = new Print(out, tempmap);
        out.println("# Before canonicalization: ");
        print.prStm(this.body);
        StmList stms = Canon.linearize(this.body);
        out.println("# After canonicalization: ");
        prStmList(print, stms);
        out.println("# Basic Blocks: ");
        BasicBlocks b = new BasicBlocks(stms);
        for (StmListList l = b.blocks; l != null; l = l.tail) {
            out.println("#");
            prStmList(print, l.head);
        }
        print.prStm(new Tree.LABEL(b.done));
        out.println("# Trace Scheduled: ");
        StmList traced = (new TraceSchedule(b)).stms;
        prStmList(print, traced);
        // generate assembly code, perhaps this shouldn't be done here ?
        Assem.InstrList instrs = codegen(this.frame, traced);
        // append instruction sink for liveness analysis
        instrs = this.frame.procEntryExit2(instrs);
        out.println("section .text");
        //create interference graph
    //    RegisterAllocator regAlloc = new RegisterAllocator(frame, instrs);
      //  regAlloc.allocate();
     //   TempMap tempmap2 = new Temp.CombineMap(this.frame, regAlloc);
    //    for (Assem.InstrList p = instrs; p != null; p = p.tail)
    //        out.print(p.head.format(tempmap2));
        // buildInterferenceGraph(instrs);
        // var procs = this.frame.procEntryExit3(instrs);*/

    }

    /**
     * Generates assembly code from the IR tree
     * 
     * @param f
     * @param stms
     * @return
     */
    private static InstrList codegen(Frame f, StmList stms) {
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

    static void prStmList(Tree.Print print, Tree.StmList stms) {
        for (Tree.StmList l = stms; l != null; l = l.tail) {
            print.prStm(l.head);
        }
    }

}