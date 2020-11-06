package Translate;

import Intel.IntelFrame;
import Temp.Label;
import Tree.PrettyPrinter;
import Tree.TreeVisitor;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;
import Util.TaskRegister;

public class Tasks implements TaskProvider {

    @Override
    public void build(TaskRegister taskRegister) {
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    Semant.Semant semant = new Semant.Semant(taskContext.errorMsg, new Translator());
                    taskContext.setFragList(semant.getTreeFragments(taskContext.decList));
                }
            }, "hir-compute", "Translate abstract syntax to HIR", "typed")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    TreeVisitor prettyPrinter = new PrettyPrinter(taskContext.log);
                    taskContext.hirFragList.accept(new FragmentVisitor(){

                        @Override
                        public void visit(ProcFrag procFrag) {
                            procFrag.body.accept(prettyPrinter);
                        }

                        @Override
                        public void visit(DataFrag dataFrag) {
                        }
                    });
                }
            }, "hir-display", "Display the HIR", "hir-compute")
        );

    }
}