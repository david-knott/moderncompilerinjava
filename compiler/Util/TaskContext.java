package Util;

import Assem.InstrList;
import Parse.Program;
import Translate.FragList;

public class TaskContext {
    public Program program;
    public boolean escapesDisplay;
    public boolean bindingsDisplay;
    public FragList hirFragList;
    public FragList lirFragList;
    //public InstrList instrList;

    public void setAst(final Program value) {
        this.program = value;
    }

    public void setEscapesDisplay(final boolean value) {
        this.escapesDisplay = value;
    }

    public void setBindingsDisplay(final boolean value) {
        this.bindingsDisplay = value;
    }

    public void setFragList(final FragList frags) {
        this.hirFragList = frags;
    }

    public void setLIR(final FragList lirFragList) {
        this.lirFragList = lirFragList;
	}

	public void setInstrList(InstrList instrList) {
    //    this.instrList = instrList;
	}

	public void setAssemFragList(Object object) {
	}
}