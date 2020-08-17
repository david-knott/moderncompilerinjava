package Intel;

import java.io.InputStream;
import java.io.OutputStream;

import Canon.Canonicalization;
import ErrorMsg.ErrorMsg;
import RegAlloc.RegAllocFactory;
import Translate.FragList;
import Util.Assert;
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
    final Canonicalization canonicalization;

    public Tasks(RegAllocFactory regAllocFactory, Canonicalization canonicalization) {
        this.regAllocFactory = regAllocFactory;
        this.canonicalization = canonicalization;
    }

    @Override
    public void build(InputStream in, OutputStream out, ErrorMsg errorMsg) {
        Assert.assertNotNull(errorMsg);
        Assert.assertNotNull(in);
        Assert.assertNotNull(out);
        /*
        new SimpleTask(new SimpleTaskProvider() {

            @Override
            public void only(TaskContext taskContext) {
                FragList frags = taskContext.hirFragList;
                PrintStream fileOut = new PrintStream(out);
                fileOut.println(".global tigermain");
                frags.accept(new AssemblyCompute(regAllocFactory, canonicalization, out));
                fileOut.close();
            }
        }, "target-x64", "Select x64 as target", new String[] { "" });*/

        new SimpleTask(new SimpleTaskProvider(){
			@Override
			public void only(TaskContext taskContext) {
                CodeGen codeGen = new CodeGen();
                FragList lirFragList = taskContext.lirFragList;
                AssemblyGeneratorVisitor assemblyFragmentVisitor = new AssemblyGeneratorVisitor(codeGen);
                lirFragList.accept(assemblyFragmentVisitor);
                taskContext.setAssemFragList(assemblyFragmentVisitor.getAssemFragList());
			}
        }, "instruction-selection", "Select x64 as target", new String[] { "" });
    }
}