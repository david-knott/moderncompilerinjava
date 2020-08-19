package RegAlloc;

import java.io.OutputStream;
import java.io.PrintStream;

import Assem.DataFrag;
import Assem.FragmentVisitor;
import Assem.InstrList;
import Assem.MOVE;
import Assem.ProcFrag;
import Frame.Proc;
import Temp.CombineMap;
import Temp.Temp;
import Temp.TempMap;

class AssemFragmentVisitor implements FragmentVisitor {
    final RegAllocFactory regAlloc;
    final PrintStream out;

    public AssemFragmentVisitor(RegAllocFactory regAlloc, OutputStream outputStream) {
        this.regAlloc = regAlloc;
        this.out = new PrintStream(outputStream);
        this.out.println(".global tigermain");
    }

    @Override
    public void visit(ProcFrag procFrag) {
        var instrs = procFrag.body;
        instrs = procFrag.frame.procEntryExit2(instrs);
        var regAlloc = this.regAlloc.getRegAlloc(procFrag.frame.name.toString(), procFrag.frame, instrs);
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
                if(tempMap.tempMap(def) != tempMap.tempMap(use)) {
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
		out.println(dataFrag.toString());
    }
}