package Intel;

import Temp.Label;
import Temp.Temp;
import Tree.IR;
import Tree.JUMP;

/**
 * Interface that code emitters must implement. This abstraction
 * was created to faciliate testing of the JBURG rules.
 */
public interface Emitter {

	public Assem.InstrList getInstrList();
	
    public void loadIndirect(Temp arg0, Temp arg1);

	public void loadIndirectDisp(int binop, Temp arg0, Temp arg1, int offset);

	public void moveConstToTemp(Temp arg0, int arg1);

	public void moveExpToTemp(Temp arg0, Temp arg1);

	public void binop(Temp arg0, int value, Temp temp);

	public void binop(int value, Temp arg0, Temp temp);

	public void binop(int value, int arg0, Temp temp);

	public void binop(Temp arg0, Temp arg1, Temp temp);

    public void storeIndirect(Temp arg0, Temp arg1);

    public void storeIndirectDisp(int binop, Temp arg0, Temp arg1, int offset);


	public void call(Object call);

	public void cjump(int relop, Temp arg0, Temp arg1, Label iftrue, Label iffalse);

	public void jump(Temp arg0, JUMP jump);

	public void label(Label label);

	public void loadIndrectDispScale(int i, Temp arg0, Temp base, Temp index, int value);
}