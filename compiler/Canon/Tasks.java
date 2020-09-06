package Canon;

import Translate.DataFrag;
import Translate.FragList;
import Translate.FragmentVisitor;
import Translate.ProcFrag;
import Tree.TreeSimplifierVisitor;
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
    public void build() {
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                FragList frags = taskContext.hirFragList;
                CanonVisitor canonVisitor = new CanonVisitor(canonicalization);
                frags.accept(canonVisitor);
                taskContext.setLIR(canonVisitor.fragList);
            }
        }, "lir-compute", "Perform canonicalisation of HIR tree", "hir-compute");
        new SimpleTask((taskContext) -> taskContext.lirFragList.accept(new FragPrettyPrinter(new PrettyPrinter(taskContext.log))),
                "lir-display", "Displays the lir", "lir-compute");
        new SimpleTask((taskContext) -> taskContext.lirFragList.accept(new FragmentVisitor() {

            @Override
            public void visit(ProcFrag procFrag) {
                TreeSimplifierVisitor expandTempVisitor = new TreeSimplifierVisitor();
                procFrag.body.accept(expandTempVisitor);
            }

            @Override
            public void visit(DataFrag dataFrag) {
                // do nothing.
            }
                
            }), 
            "hir-simplify", 
            "Simplifies the hir in preparation for optimisation",
            "hir-compute"
        );


    }
}