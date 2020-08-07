package Util;

import Parse.Program;
import Translate.FragList;

public class TaskContext {
    public Program program;
    public boolean escapesDisplay;
    public boolean bindingsDisplay;
    public FragList fragList;

    public void setAst(Program value) {
        this.program = value;
    }

    public void setEscapesDisplay(boolean value) {
        this.escapesDisplay = value;
    }

    public void setBindingsDisplay(boolean value) {
        this.bindingsDisplay = value;
    }

	public void setFragList(FragList frags) {
        this.fragList = frags;
	}
}