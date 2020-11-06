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
import Util.TaskRegister;

public class Tasks implements TaskProvider {

    final Canonicalization canonicalization;

    public Tasks(Canonicalization canonicalization) {
        this.canonicalization = canonicalization;
    }

    @Override
    public void build(TaskRegister taskRegister) {
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    FragList frags = taskContext.hirFragList;
                    CanonVisitor canonVisitor = new CanonVisitor(canonicalization);
                    frags.accept(canonVisitor);
                    taskContext.setLIR(canonVisitor.fragList);
                }
            }, "lir-compute", "Perform canonicalisation of HIR tree", "hir-compute")
        );

        taskRegister.register(
            new SimpleTask((taskContext) -> taskContext.lirFragList.accept(new FragPrettyPrinter(new PrettyPrinter(taskContext.log))),
                "lir-display", "Displays the lir", "lir-compute")
        );


        taskRegister.register(
            new SimpleTask((taskContext) -> taskContext.hirFragList.accept(new FragmentVisitor() {
                @Override
                public void visit(ProcFrag procFrag) {
                    TreeSimplifierVisitor expandTempVisitor = new TreeSimplifierVisitor();
                    procFrag.body.accept(expandTempVisitor);
                }
                @Override
                public void visit(DataFrag dataFrag) {
                }
                }), 
                "hir-simplify", 
                "Simplifies the hir in preparation for optimisation",
                "hir-compute"
            )
        );

        taskRegister.register(
            new SimpleTask((taskContext) -> taskContext.hirFragList.accept(new FragmentVisitor() {
                    public void visit(ProcFrag frags) {
                        CanonVisitor canonVisitor = new CanonVisitor(canonicalization);
                        frags.accept(canonVisitor);
                        taskContext.setLIR(canonVisitor.fragList);
                    }
                    @Override
                    public void visit(DataFrag dataFrag) {
                    }
                }), 
                "hir-simplify-canon", 
                "Simplifies the hir in preparation for optimisation",
                "hir-simplify"
            )
        );

        taskRegister.register(
            new SimpleTask((taskContext) -> taskContext.lirFragList.accept(new FragmentVisitor() {

                public void visit(ProcFrag procFrag) {
                    procFrag.body.accept(new PrettyPrinter(taskContext.log));
                }

                @Override
                public void visit(DataFrag dataFrag) {
                    // do nothing.
                }
                    
                }), 
                "lir-simplify-display", 
                "Simplifies the hir in preparation for optimisation",
                "hir-simplify-canon"
            )
        );


    }
}