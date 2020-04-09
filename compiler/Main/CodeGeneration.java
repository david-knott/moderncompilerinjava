package Main;

import Assem.InstrList;
import Canon.StmListList;
import Frame.Frame;
import Tree.StmList;

/**
 * Encapsulates the code generation phase
 */
class CodeGeneration {

    public CodeGeneration(Frame frame, StmListList traces) {
//        Assem.InstrList instrs = this.codegen(frame, traced);
    }

    /**
     * Returns a assemby translation for the supplied statement list stms
     * and activation record f
     * @param f
     * @param stms
     * @return
     */
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
}