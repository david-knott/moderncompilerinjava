package Absyn;

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
                PrintStream printStream = new PrintStream(out);
                printStream.println("/* == Abstract Syntax Tree. == */");
                taskContext.program.absyn.accept(new PrettyPrinter(printStream));
            }
        }, "ast-display", "absyn", new String[]{"parse"});
    }
}