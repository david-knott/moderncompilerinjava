package Canon;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import Canon.Canonicalization;
import ErrorMsg.ErrorMsg;
import RegAlloc.RegAllocFactory;
import Translate.FragList;
import Translate.FragmentVisitor;
import Translate.ProcFrag;
import Tree.PrettyPrinter;
import Tree.TreeVisitor;
import Translate.AssemblyCompute;
import Translate.DataFrag;
import Util.Assert;
import Util.BooleanTask;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;

class FragPrettyPrinter implements FragmentVisitor {
    final TreeVisitor treeVisitor;

    public FragPrettyPrinter(TreeVisitor treeVisitor) {
        this.treeVisitor = treeVisitor;
    }

    @Override
    public void visit(ProcFrag procFrag) {
        procFrag.body.accept(this.treeVisitor);
    }

    @Override
    public void visit(DataFrag dataFrag) {
    }

}

public class Tasks implements TaskProvider {

    final Canonicalization canonicalization;

    public Tasks(Canonicalization canonicalization) {
        this.canonicalization = canonicalization;
    }

    @Override
    public void build(InputStream in, OutputStream out, ErrorMsg errorMsg) {
        Assert.assertNotNull(errorMsg);
        Assert.assertNotNull(in);
        Assert.assertNotNull(out);
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                FragList frags = taskContext.hirFragList;
                CanonVisitor canonVisitor = new CanonVisitor(canonicalization);
                frags.accept(canonVisitor);
                taskContext.setLIR(canonVisitor.fragList);
            }
        }, "lir-compute", "Perform canonicalisation of HIR tree", new String[] { "" });
        new SimpleTask(
            (taskContext) -> taskContext.lirFragList.accept(new FragPrettyPrinter(new PrettyPrinter(out))), 
            "lir-display", 
            "Displays the lir",
            new String[]{"lir-compute"}
        );
    }
}