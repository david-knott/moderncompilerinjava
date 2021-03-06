package Util;

import java.io.InputStream;
import java.io.OutputStream;

import Absyn.DecList;
import Assem.InstrList;
import ErrorMsg.ErrorMsg;
import Parse.Program;
import Translate.FragList;

public class TaskContext {
   // public Program program;
    public boolean escapesDisplay = false;
    public boolean bindingsDisplay = false;
    public FragList hirFragList;
    public FragList lirFragList;
    public Assem.FragList assemFragList;
    public InputStream in = null;
    public OutputStream out = null;
    public OutputStream log = null;
    public ErrorMsg errorMsg = null;
    public DecList decList;

    public TaskContext(InputStream in, OutputStream out, OutputStream log, ErrorMsg errorMsg) {
        this.in = in;
        this.out = out;
        this.log = log;
        this.errorMsg = errorMsg;
    }

    /*
    public void setAst(final Program value) {
        this.program = value;
    }
*/
    public void setDecList(final DecList value) {
        this.decList = value;
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

	public void setAssemFragList(Assem.FragList frags) {
        this.assemFragList = frags;
	}
}