package Translate;

import Intel.IntelFrame;
import Temp.Label;
import Tree.PrettyPrinter;
import Tree.TreeVisitor;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;

public class Tasks implements TaskProvider {

    @Override
    public void build() {
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                // Level outerMost contains the primitive language functions, such as print and printi.
              //  Level outerMost = new Level(new IntelFrame(Label.create("tigermain"), null));
                // create new semantic analysis module to type check the AST.
                Semant.Semant semant = new Semant.Semant(taskContext.errorMsg, new Translator());
                // pass the AST into the semantic analysis module and get the generated tree fragments.
                taskContext.setFragList(semant.getTreeFragments(taskContext.decList));
            }
        }, "hir-compute", "Translate abstract syntax to HIR", "typed");
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
                        // TODO: Implement.
                    }
                });
            }
        }, "hir-display", "Display the HIR", "hir-compute");

    }
}