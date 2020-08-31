package Canon;

import java.io.InputStream;
import java.io.OutputStream;

import ErrorMsg.ErrorMsg;
import Translate.DataFrag;
import Translate.FragList;
import Translate.FragmentVisitor;
import Translate.ProcFrag;
import Tree.ExpandTempVisitor;
import Tree.PrettyPrinter;
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
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                FragList frags = taskContext.hirFragList;
                CanonVisitor canonVisitor = new CanonVisitor(canonicalization);
                frags.accept(canonVisitor);
                taskContext.setLIR(canonVisitor.fragList);
            }
        }, "lir-compute", "Perform canonicalisation of HIR tree", "hir-compute");
        new SimpleTask((taskContext) -> taskContext.lirFragList.accept(new FragPrettyPrinter(new PrettyPrinter(out))),
                "lir-display", "Displays the lir", "lir-compute");
        new SimpleTask((taskContext) -> taskContext.lirFragList.accept(new FragmentVisitor() {

            @Override
            public void visit(ProcFrag procFrag) {
                ExpandTempVisitor expandTempVisitor = new ExpandTempVisitor();
                procFrag.body.accept(expandTempVisitor);
            }

            @Override
            public void visit(DataFrag dataFrag) {
            }
                
            }), 
            "temp-expand", 
            "Expands the hir",
            "lir-compute"
        );


    }
}