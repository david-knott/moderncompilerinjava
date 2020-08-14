package Intel;

import Assem.Instr;
import Temp.Label;
import Temp.Temp;
import Temp.TempList;
import Tree.IR;

public class EmitterImpl implements Emitter {

//    AssemInstructionEnum.InstrList iList = null, last = null;

    private void emit(Instr instr) {
    }

	public void loadIndirect(Temp arg0, Temp arg1) {
	}

	public void loadIndirectDisp(int binop, Temp arg0, Temp arg1, int offset) {
	}

	public void startLoadIndirectDispScaled(Temp arg0, Temp arg1, IR arg2) {
	}

	public void endLoadIndirectDispScaled(Temp arg0, Temp arg1) {
	}

	public void moveConstToTemp(Temp arg0, IR arg1) {
	}

	public void moveExpToTemp(Temp arg0, Temp arg1) {
	}

	@Override
	public void binop(Temp arg0, Temp arg1, Temp temp) {
		// TODO Auto-generated method stub

	}

    //store Operations

    //binop Operations


    //jump Operations

    //cjump Operations
}