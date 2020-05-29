package Helpers;

import Assem.Instr;
import Assem.InstrList;
import Assem.OPER;
import Frame.Access;
import Frame.Frame;
import Frame.Proc;
import Temp.Label;
import Temp.Temp;
import Temp.TempList;
import Tree.Exp;
import Tree.ExpList;
import Tree.Stm;
import Util.BoolList;

public class TestFrame extends Frame {
    private TempList precoloured; 
    private TempList registers;

    public TestFrame(TempList precoloured, TempList registers) {
        this.precoloured = precoloured;
        this.registers = registers;
    }

    @Override
    public String tempMap(Temp t) {
        return precoloured.toTempMap().get(t);
    }

    @Override
    public Temp FP() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Temp RV() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int wordSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Frame newFrame(Label name, BoolList formals) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Access allocLocal(boolean escape) {
        return new Access() {

            @Override
            public Exp exp(Exp framePtr) {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

    @Override
    public Stm procEntryExit1(Stm body) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InstrList procEntryExit2(InstrList body) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Proc procEntryExit3(InstrList body) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Exp externalCall(String func, ExpList args) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String string(Label l, String literal) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InstrList codegen(Stm head) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TempList registers() {
        return this.registers;
    }

    @Override
    public InstrList tempToMemory(Temp temp, Temp spillTemp, Access access) {
        Instr moveTempToNewTemp = new Assem.MOVE("movq %`s0, %`d0; ttm", spillTemp, temp);
        Instr moveNewTempToFrame = new OPER("movq %`s0, " + 0 + "(%`d0); ttm", this.precoloured, new TempList(spillTemp, null));
        return new InstrList(moveTempToNewTemp, new InstrList(moveNewTempToFrame, null)); 
    }

    @Override
    public InstrList memoryToTemp(Temp temp, Temp spillTemp, Access access) {
        Instr moveFrameToNewTemp = new OPER("movq " + 0 + "(%`s0), %`d0; mtt", new TempList(spillTemp, null), this.precoloured);
        Instr moveNewTempToTemp = new Assem.MOVE("movq %`s0, %`d0; mtt", temp, spillTemp);
        return new InstrList(moveFrameToNewTemp, new InstrList(moveNewTempToTemp, null)); 
    }
    
}