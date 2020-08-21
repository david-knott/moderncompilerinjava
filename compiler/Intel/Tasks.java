package Intel;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import Assem.DataFrag;
import Assem.FragmentVisitor;
import Assem.InstrList;
import Assem.ProcFrag;
import Canon.Canonicalization;
import ErrorMsg.ErrorMsg;
import RegAlloc.RegAllocFactory;
import Temp.DefaultMap;
import Translate.FragList;
import Util.Assert;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;

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
    public void build(InputStream in, OutputStream out, ErrorMsg errorMsg) {
        /*
         * new SimpleTask(new SimpleTaskProvider() {
         * 
         * @Override public void only(TaskContext taskContext) { FragList frags =
         * taskContext.hirFragList; PrintStream fileOut = new PrintStream(out);
         * fileOut.println(".global tigermain"); frags.accept(new
         * AssemblyCompute(regAllocFactory, canonicalization, out)); fileOut.close(); }
         * }, "target-x64", "Select x64 as target", new String[] { "" });
         */

        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                FragList lirFragList = taskContext.lirFragList;
                AssemblyGeneratorVisitor assemblyFragmentVisitor = new AssemblyGeneratorVisitor(new CodeGen());
                lirFragList.accept(assemblyFragmentVisitor);
                taskContext.setAssemFragList(assemblyFragmentVisitor.getAssemFragList());
            }
        }, "instruction-selection", "Select x64 as target", new String[] { "" });
        
        /*
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                Assem.FragList assemblyFragList = taskContext.assemFragList;
                assemblyFragList.accept(new FragmentVisitor() {

                    @Override
                    public void visit(ProcFrag procFrag) {
                        PrintStream printStream = new PrintStream(out);
                        printStream.println("proc:" + procFrag.frame.name);
                        for(InstrList instrList = procFrag.body; instrList != null; instrList = instrList.tail) {
                            printStream.println(instrList.head.format(new DefaultMap()));
                        }
                        printStream.println();
                    }

                    @Override
                    public void visit(DataFrag dataFrag) {
                        // TODO Auto-generated method stub

                    }
                    
                });

			}
        }, "dump-it", "", new String[] { "" });*/
        

    }
}