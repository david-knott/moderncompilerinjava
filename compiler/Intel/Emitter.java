package Intel;

import Assem.Instr;
import Temp.Label;
import Temp.Temp;
import Temp.TempList;

public class Emitter {

    Assem.InstrList iList = null, last = null;

    private void emit(Instr instr) {
        if (last != null) {
            last = last.tail = new Assem.InstrList(instr, null);
        } else {
            last = iList = new Assem.InstrList(instr, null);
        }
    }

    //Move Operations
    public void loadMoveTemp(Temp src, Temp dst) {
        emit(new Assem.MOVE("movq %`s0, %`d0", dst, src));
    }

    public void moveImmediate(int immediate, Temp dst) {
        emit(new Assem.OPER("movq $" + immediate + ", %`d0", new TempList(dst), null));
    }

    public void buildMove(Label label, Temp dst) {
        emit(new Assem.OPER("leaq $" + label.toString() + ", %`d0", new TempList(dst), null));
    }

	public void loadBuild(Temp dst, Temp src) {
        emit(new Assem.MOVE("movq (%`s0), %`d0", dst, src));
    }
    
    public void loadBuild(Temp dst, Temp src, int offset) {
        emit(new Assem.MOVE("movq " + offset + "(%`s0), %`d0", dst, src));
	}

    //store Operations

    //binop Operations


    //jump Operations

    //cjump Operations
}