package Util;

import Canon.StmListList;
import Parse.Program;
import Translate.FragList;

public class TaskContext {
    public Program program;
    public boolean escapesDisplay;
    public boolean bindingsDisplay;
    public FragList hirFragList;
    public FragList lirFragList;

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
}