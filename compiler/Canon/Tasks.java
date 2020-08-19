package Canon;

import java.io.InputStream;
import java.io.OutputStream;

import ErrorMsg.ErrorMsg;
import Translate.FragList;
import Tree.PrettyPrinter;
import Util.Assert;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;

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