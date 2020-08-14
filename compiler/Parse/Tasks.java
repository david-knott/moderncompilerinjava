package Parse;

import java.io.InputStream;
import java.io.OutputStream;

import ErrorMsg.ErrorMsg;
import Util.BooleanTask;
import Util.BooleanTaskFlag;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;

import Util.Assert;

public class Tasks implements TaskProvider {

    final Parser parseTask;

    public Tasks(Parser parser) {
        this.parseTask = parser;
	}

	@Override
    public void build(InputStream in, OutputStream out, ErrorMsg errorMsg) {
        Assert.assertNotNull(errorMsg);
        Assert.assertNotNull(in);
        Assert.assertNotNull(out);
        new BooleanTask(new BooleanTaskFlag() {
            @Override
            public void set() {
                //parseTask.
            }
        }, "parse-trace", "parse-trace", "parse-trace", null);
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                parseTask.parse(taskContext);
            }
        }, "parse", "parse", null); // should return a function to the
    }
}