package RegAlloc;

import java.io.InputStream;
import java.io.OutputStream;

import ErrorMsg.ErrorMsg;
import Util.BooleanTask;
import Util.BooleanTaskFlag;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;

/**
 * A collection of tasks related to code generation for the intel x64 instruction set.
 * This class will be refactored so the AssemblyCompute visitor will be split into 
 * separate modules and tasks ( Canon, Instruction Select, Reg Alloc )
 */
public class Tasks implements TaskProvider {
    final RegAllocFactory regAllocFactory;
    boolean demove = false;

    public Tasks(RegAllocFactory regAllocFactory) {
        this.regAllocFactory = regAllocFactory;
    }

    @Override
    public void build(InputStream in, OutputStream out, ErrorMsg errorMsg) {

        new BooleanTask(new BooleanTaskFlag(){
            @Override
            public void set() {
                demove = true;
            }
            
        }, "demove", "Remove redundant moves", "reg-alloc");

        new SimpleTask(new SimpleTaskProvider(){
			@Override
			public void only(TaskContext taskContext) {
                taskContext.assemFragList.accept(new AssemFragmentVisitor(demove, regAllocFactory, out));
			}
        }, "reg-alloc", "Select x64 as target", "instr-compute");
    }
}