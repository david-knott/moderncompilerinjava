package Codegen;

import Assem.InstrList;
import Assem.InstrListList;
import Frame.Frame;
import Tree.StmList;
import Tree.StmListList;


class CodeGenExampleReturn {

    //TreeContainerExampleReturn;
    StmList stmList;



}
public class CodeGeneratorFacade {
    private StmListList stmListList;

    public CodeGeneratorFacade(StmListList stmListList) {
        this.stmListList = stmListList;
    }

    /**
     * Generates assembly code for the entire list. Calls
     * frame.procEntryExit2 which appends a sink instruction
     * which is used to in flow analysis to mark certain temporaries
     * as live at exit.
     * Returns a list of assmebly instruction lists.
     */
    public InstrListList generateCode() {
        InstrListList list = null;
        for(var stmList = this.stmListList; stmList != null; stmList = stmList.tail) {
            var frame = stmList.head.frame;
            Assem.InstrList instrs = codegen(frame, stmList.head);
            instrs = frame.procEntryExit2(instrs);
            if(list == null) {
                list = new InstrListList(instrs, null);
            } else {
                list.append(instrs);
            }
        }
        return list;
    }

    /**
     * Generates assembly code from the IR tree using
     * the supplied frame and statement list.
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