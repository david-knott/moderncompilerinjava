package Intel;

import Canon.Canonicalization;
import RegAlloc.RegAllocFactory;
import Translate.FragList;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;
import Util.TaskRegister;

/**
 * A collection of tasks related to code generation for the intel x64
 * instruction set. This class will be refactored so the AssemblyCompute visitor
 * will be split into separate modules and tasks ( Canon, Instruction Select,
 * Reg Alloc )
 */
public class Tasks implements TaskProvider {
    final RegAllocFactory regAllocFactory;
    final Canonicalization canonicalization;

    public Tasks(RegAllocFactory regAllocFactory, Canonicalization canonicalization) {
        this.regAllocFactory = regAllocFactory;
        this.canonicalization = canonicalization;
    }

    @Override
    public void build(TaskRegister taskRegister) {
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    FragList lirFragList = taskContext.lirFragList;
                    AssemblyGeneratorVisitor assemblyFragmentVisitor = new AssemblyGeneratorVisitor(new CodeGen());
                    lirFragList.accept(assemblyFragmentVisitor);
                    taskContext.setAssemFragList(assemblyFragmentVisitor.getAssemFragList());
                }
            }, "instr-compute", "Select x64 as target", "lir-compute" )
        );
        
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    Assem.FragList assemblyFragList = taskContext.assemFragList;
                    assemblyFragList.accept(new UnallocatedAssmeblyDump(taskContext.log));

                }
            }, "instr-display", "Dump the unallocated assembly", "instr-compute")
        );

        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    Assem.FragList assemblyFragList = taskContext.assemFragList;
                    UnAllocatedAssemblyStats assemblyStats = new UnAllocatedAssemblyStats();
                    assemblyFragList.accept(assemblyStats);
                    assemblyStats.dump(taskContext.log);

                }
            }, "instr-stats", "Display instruction type counts", "instr-compute")
        );
    }
}