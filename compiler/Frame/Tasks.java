package Frame;

import java.io.InputStream;
import java.io.OutputStream;

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

            }
        }, "compute", "select the instructions", "lir-compute targeted");
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {

            }
        }, "runtime-display", "display the runtime", "targeted");
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {

            }
        }, "inst-debug", "Enable verbose instruction display", "targeted");
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {

            }
        }, "targeted", "Enable verbose instruction display", "target-ia64");
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {

            }
        }, "target-ia64", "Select x64 as target", null);
    }
}