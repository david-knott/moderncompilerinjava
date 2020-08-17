package Intel;

import Assem.Instr;
import Temp.Label;
import Temp.Temp;
import Tree.IR;
import Tree.JUMP;
public class EmitterImpl implements Emitter {

    private Assem.InstrList iList = null, last = null;
	// AssemInstructionEnum.InstrList iList = null, last = null;

	@Override
	public Assem.InstrList getInstrList() {
		return this.iList;
	}

	private void emit(Instr instr) {
		if (last != null) {
			last = last.tail = new Assem.InstrList(instr, null);
		} else {
			last = iList = new Assem.InstrList(instr, null);
		}
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

	@Override
	public void storeIndirect(Temp arg0, Temp arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeIndirectDisp(int binop, Temp arg0, Temp arg1, int offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startStoreIndirectDispScaled(Temp arg0, Temp arg1, IR arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void call(Object call) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cjump(int relop, Temp arg0, Temp arg1, Label iftrue, Label iffalse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jump(Temp arg0, JUMP jump) {
		// TODO Auto-generated method stub

	}

	@Override
	public void label(Label label) {
        emit(new Assem.LABEL(label.toString() + ":", label));
	}

    //store Operations

    //binop Operations


    //jump Operations

    //cjump Operations
}