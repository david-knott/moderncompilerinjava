package Intel;

import java.util.Vector;

import Core.LL;
import Temp.Temp;
import Tree.CALL;
import Tree.IR;

public class TestEmitter implements Emitter {

    LL<AssemInstructionEnum> assem;

    private void add(AssemInstructionEnum assemInstructionEnum) {
        this.assem = LL.<AssemInstructionEnum>insertRear(this.assem, assemInstructionEnum);
    }

    public AssemInstructionEnum get(int i) {
        return i < LL.<AssemInstructionEnum>size(this.assem) ? LL.<AssemInstructionEnum>get(this.assem, i) : null;
    }

    public int size() {
        return LL.<AssemInstructionEnum>size(this.assem);
    }

    @Override
    public void loadIndirect(Temp arg0, Temp arg1) {
        this.add(AssemInstructionEnum.LOAD_INDIRECT);
    }

    @Override
    public void loadIndirectDisp(int binop, Temp arg0, Temp arg1, int offset) {
        this.add(AssemInstructionEnum.LOAD_INDIRECT_DISP);
    }

    @Override
    public void startLoadIndirectDispScaled(Temp arg0, Temp arg1, IR arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void endLoadIndirectDispScaled(Temp arg0, Temp arg1) {
        this.add(AssemInstructionEnum.LOAD_INDIRECT_DISP_SCALED);
    }

    @Override
    public void moveConstToTemp(Temp arg0, IR arg1) {
        this.add(AssemInstructionEnum.MOVE_CONST_TO_TEMP);
    }

    @Override
    public void moveExpToTemp(Temp arg0, Temp arg1) {
        this.add(AssemInstructionEnum.MOVE_EXP_TO_TEMP);
    }

    @Override
    public void binop(Temp arg0, Temp arg1, Temp temp) {
        this.add(AssemInstructionEnum.BINOP);
    }

    @Override
    public void storeIndirect(Temp arg0, Temp arg1) {
        this.add(AssemInstructionEnum.STORE_INDIRECT);
    }

    @Override
    public void storeIndirectDisp(int binop, Temp arg0, Temp arg1, int offset) {
        this.add(AssemInstructionEnum.STORE_INDIRECT_DISP);

    }

    @Override
    public void startStoreIndirectDispScaled(Temp arg0, Temp arg1, IR arg2) {
        this.add(AssemInstructionEnum.STORE_INDIRECT_DISP_SCALED);
    }

    @Override
    public void call(Object call) {
        this.add(AssemInstructionEnum.CALL);
    }
}