package Helpers;

import Assem.Instr;
import Assem.InstrList;
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

    @Override
    public String tempMap(Temp t) {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TempList precoloured() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InstrList tempToMemory(Temp temp) {
        // TODO Auto-generated method stub
        System.out.println("temp to memory");
        return null;
    }

    @Override
    public InstrList memoryToTemp(Temp temp) {
        // TODO Auto-generated method stub
        System.out.println("memory to temp");
        return null;
    }
    
}