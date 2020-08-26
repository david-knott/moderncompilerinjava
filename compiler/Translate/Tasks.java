package Translate;

import java.io.InputStream;
import java.io.OutputStream;

import ErrorMsg.ErrorMsg;
import Intel.IntelFrame;
import Temp.Label;
import Tree.PrettyPrinter;
import Tree.TreeVisitor;
import Util.Assert;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;

public class Tasks implements TaskProvider {

    @Override
    public void build(InputStream in, OutputStream out, ErrorMsg errorMsg) {
        Assert.assertNotNull(errorMsg);
        Assert.assertNotNull(in);
        Assert.assertNotNull(out);
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                // TODO: remove reference to IntelFrame.
                Frame.Frame frame = new IntelFrame(Label.create("tigermain"), null);
                Level topLevel = new Level(frame);
                Translator translate = new Translator();
                Semant.Semant semant = new Semant.Semant(errorMsg, topLevel, translate);
                FragList frags = FragList.reverse(semant.getTreeFragments(taskContext.program.absyn));
                taskContext.setFragList(frags);
            }
        }, "hir-compute", "Translate abstract syntax to HIR", "typed");
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                TreeVisitor prettyPrinter = new PrettyPrinter(out);
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