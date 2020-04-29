package RegAlloc;

import Assem.Instr;
import Assem.InstrList;
import Frame.Frame;
import Temp.Temp;
import Temp.TempList;

public class RegisterSpiller {

    private TempList tempList;
    private Frame frame;
    private SpillSelectStrategy spillSelectStrategy;

    public RegisterSpiller(SpillSelectStrategy spillSelectStrategy, TempList tempList, Frame frame) {
        this.tempList = tempList;
        this.spillSelectStrategy = spillSelectStrategy;
        this.frame = frame;
    }

    private boolean spilled(Temp temp) {
        return this.spillSelectStrategy.spill(this.tempList) == temp;
    }

    private Instr tempToMemory(InstrList instr, Temp temp) {
        return this.frame.tempToMemory(temp);
    }

    private Instr memoryToTemp(InstrList instr, Temp temp) {
        return this.frame.memoryToTemp(temp);
    }

    private InstrList concat(Assem.InstrList a, Assem.InstrList b) {
        if (a == null)
            return b;
        else {
            Assem.InstrList p;
            for (p = a; p.tail != null; p = p.tail)
                ;
            p.tail = b;
            return a;
        }
    }

    public InstrList rewrite(InstrList instrList) {

        InstrList result = null;
        for (InstrList loop = instrList; loop != null; loop = loop.tail) {
            Instr instr = loop.head;
            InstrList partial = new InstrList(instr, null);
            // foreach instance of a use, move from memory location to new temp
            for (TempList uses = instr.use(); uses != null; uses = uses.tail) {
                Temp use = uses.head;
                if (this.spilled(use)) {
                    var mtt = this.memoryToTemp(loop, use);
                    //prepend to partial
                    partial = new InstrList(mtt, partial);
                    break;
                }
            }
            // foreach instance of def, move the temp to a memory location
            for (TempList defs = instr.def(); defs != null; defs = defs.tail) {
                Temp def = defs.head;
                if (this.spilled(def)) {
                    var ttm = tempToMemory(loop, def);
                    //append to partial
                    partial.append(ttm);
                    break;
                }
            }
            result = concat(result, partial);
        }
        return result;
    }
}