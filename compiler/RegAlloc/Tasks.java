package RegAlloc;

import Util.BooleanTask;
import Util.BooleanTaskFlag;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;
import Util.TaskRegister;

/**
 * A collection of tasks related to code generation for the intel x64 instruction set.
 * This class will be refactored so the AssemblyCompute visitor will be split into 
 * separate modules and tasks ( Canon, Instruction Select, Reg Alloc )
 * 
 * asm-coalesce-disable - disable coalescing /sets a flag.
 * asm-trace - trace register allocation / sets a flag.
 * asm-compute - perform register allocation
 * asm-display - display the final assmebly.
 */
public class Tasks implements TaskProvider {
    final RegAllocFactory regAllocFactory;
    boolean disableCoalesce = false;

    public Tasks(RegAllocFactory regAllocFactory) {
        this.regAllocFactory = regAllocFactory;
    }

    @Override
    public void build(TaskRegister taskRegister) {
        taskRegister.register(
            new BooleanTask(new BooleanTaskFlag(){
                @Override
                public void set() {
                    disableCoalesce = true;
                }
                
            }, "demove", "Disable register coalescing", "reg-alloc")
        );

        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider(){
                @Override
                public void only(TaskContext taskContext) {
                    taskContext.assemFragList.accept(new AssemFragmentVisitor(disableCoalesce, regAllocFactory, taskContext.out));
                }
            }, "reg-alloc", "Select x64 as target", "instr-compute")
        );
    }
}