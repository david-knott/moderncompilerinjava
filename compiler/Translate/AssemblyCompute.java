package Translate;

import java.io.OutputStream;
import java.io.PrintStream;

import Assem.InstrList;
import Assem.MOVE;
import Canon.Canonicalization;
import Frame.Frame;
import Frame.Proc;
import RegAlloc.RegAlloc;
import RegAlloc.RegAllocFactory;
import Temp.CombineMap;
import Temp.Temp;
import Temp.TempMap;
import Tree.StmList;

public class AssemblyCompute implements FragmentVisitor {

    Canonicalization canonicalization;
    PrintStream out;
    boolean moves = false;
    RegAllocFactory regAllocFactory;
    /* String regAllocStrategy; */

    public AssemblyCompute(RegAllocFactory regAlloc, Canonicalization canonicalization, OutputStream outputStream) {
        this.canonicalization = canonicalization;
        this.out = new PrintStream(outputStream);
        this.regAllocFactory = regAlloc;
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

    @Override
    public void visit(ProcFrag procFrag) {
        StmList stmList = canonicalization.canon(procFrag.body);
        Assem.InstrList instrs = codegen(procFrag.frame, stmList);
        instrs = procFrag.frame.procEntryExit2(instrs);
        RegAlloc regAlloc = this.regAllocFactory.getRegAlloc("TDB", procFrag.frame, instrs);
        TempMap tempMap = new CombineMap(procFrag.frame, regAlloc);
        instrs = regAlloc.getInstrList();
        Proc proc = procFrag.frame.procEntryExit3(instrs);
        out.println(".text");
        for (InstrList prolog = proc.prolog; prolog != null; prolog = prolog.tail) {
            out.println(prolog.head.format(procFrag.frame));
        }
        for (InstrList body = proc.body; body != null; body = body.tail) {
            if(body.head instanceof MOVE) {
                Temp def = body.head.def().head;
                Temp use = body.head.use().head;
                if(!moves || tempMap.tempMap(def) != tempMap.tempMap(use)) {
                    out.println(body.head.format(tempMap));
                }
            }
            else {
                out.println(body.head.format(tempMap));
            }
        }
        for (InstrList epilog = proc.epilog; epilog != null; epilog = epilog.tail) {
            out.println(epilog.head.format(procFrag.frame));
        }
    }

    @Override
    public void visit(DataFrag dataFrag) {
        out.println(".data");
		out.println(dataFrag.data);
    }
}