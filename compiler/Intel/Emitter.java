package Intel;

import Temp.Label;
import Temp.Temp;
import Tree.CALL;
import Tree.IR;

/**
 * Interface that code emitters must implement. This abstraction
 * was created to faciliate testing of the JBURG rules.
 */
public interface Emitter {
    public void loadIndirect(Temp arg0, Temp arg1);

	public void loadIndirectDisp(int binop, Temp arg0, Temp arg1, int offset);

	public void startLoadIndirectDispScaled(Temp arg0, Temp arg1, IR arg2);

	public void endLoadIndirectDispScaled(Temp arg0, Temp arg1);

	public void moveConstToTemp(Temp arg0, IR arg1);

	public void moveExpToTemp(Temp arg0, Temp arg1);

	public void binop(Temp arg0, Temp arg1, Temp temp);

    public void storeIndirect(Temp arg0, Temp arg1);

    public void storeIndirectDisp(int binop, Temp arg0, Temp arg1, int offset);

	public void startStoreIndirectDispScaled(Temp arg0, Temp arg1, IR arg2);

	public void call(Object call);

	public void cjump(int relop, Temp arg0, Temp arg1, Label iftrue, Label iffalse);
}