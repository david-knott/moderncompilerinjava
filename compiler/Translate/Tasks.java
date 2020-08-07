package Translate;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import ErrorMsg.ErrorMsg;
import Intel.IntelFrame;
import Temp.Label;
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
                Frame.Frame frame = new IntelFrame(Label.create("tigermain"), null);
                Level topLevel = new Level(frame);
                Translator translate = new Translator();
                Semant.Semant semant = new Semant.Semant(errorMsg, topLevel, translate);
                FragList frags = FragList.reverse(semant.getTreeFragments(taskContext.program.absyn));
                taskContext.setFragList(frags);
            }
        }, "hir-compute", "Translate abstract syntax to HIR", new String[] { "typed" });
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                PrintStream ps = new PrintStream(out);
                for (FragList fragList = taskContext.fragList; fragList != null; fragList = fragList.tail) {
                //    ps.println(fragList.head);
                }
            }
        }, "hir-display", "Display the HIR", new String[] { "hir-compute" });
    }
}