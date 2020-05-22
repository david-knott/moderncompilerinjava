package Codegen;

import Assem.InstrList;
import Frame.Frame;
import Tree.StmList;

public class CodeGeneratorFacade {

    /**
     * Generates assembly code for the entire list. Calls frame.procEntryExit2 which
     * appends a sink instruction which is used to in flow analysis to mark certain
     * temporaries as live at exit. Returns a list of assmebly instruction lists.
     */
    public InstrList generateCode(Frame frame, StmList stms) {
        Assem.InstrList instrs = codegen(frame, stms);
        return frame.procEntryExit2(instrs);
    }

    /**
     * Generates assembly code from the IR tree using the supplied frame and
     * statement list.
     * 
     * @param frame
     * @param stms
     * @return
     */
    private static InstrList codegen(Frame frame, StmList stms) {
        Assem.InstrList first = null, last = null;
        for (Tree.StmList s = stms; s != null; s = s.tail) {
            Assem.InstrList i = frame.codegen(s.head);
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
}