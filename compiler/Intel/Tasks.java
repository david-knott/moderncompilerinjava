package Intel;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import Canon.Canonicalization;
import ErrorMsg.ErrorMsg;
import RegAlloc.RegAllocFactory;
import Translate.FragList;
import Translate.FragmentVisitor;
import Translate.ProcFrag;
import Tree.BINOP;
import Tree.CALL;
import Tree.CJUMP;
import Tree.CONST;
import Tree.ESEQ;
import Tree.EXP;
import Tree.JUMP;
import Tree.LABEL;
import Tree.MEM;
import Tree.MOVE;
import Tree.NAME;
import Tree.SEQ;
import Tree.TEMP;
import Tree.TreeVisitor;
import Translate.AssemblyCompute;
import Translate.DataFrag;
import Util.Assert;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;

class AssemblyFragmentVisitor implements FragmentVisitor {

    final CodeGen codeGen;

    public AssemblyFragmentVisitor(CodeGen codeGen) {
        this.codeGen = codeGen;
    }

    @Override
    public void visit(ProcFrag procFrag) {
        try {
            this.codeGen.burm(procFrag.body);
        } catch (Exception e) {
            //an exception here would suggest that a new tree
            //struction was visited.
            throw new Error(e);
        }
    }

    @Override
    public void visit(DataFrag dataFrag) {

    }
}
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
                FragList lirFragList = taskContext.lirFragList;
                CodeGen codeGen = new CodeGen();
                Emitter emitter = new EmitterImpl();
                codeGen.setEmitter(emitter);
                lirFragList.accept(new AssemblyFragmentVisitor(codeGen));


			}
        }, "instruction-selection", "Select x64 as target", new String[] { "" });
    }
}