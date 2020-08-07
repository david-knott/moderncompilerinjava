package FindEscape;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import ErrorMsg.ErrorMsg;
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
                FindEscape findEscape = new FindEscape(false);
                findEscape.traverse(taskContext.program.absyn);
            }
        }, "escapes-compute", "escape", new String[]{"bound"});

        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                taskContext.setEscapesDisplay(true);

            }
        }, "escapes-display", "escape", new String[]{"parse"});

    }
}