package Util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import Core.LL;
import ErrorMsg.ErrorMsg;

public class TaskRegister {
    public static TaskRegister instance = new TaskRegister();
    InputStream in = null;
    OutputStream out = null;
    ErrorMsg errorMsg = null;

    private LL<Task> tasks = null;

    public TaskRegister parseArgs(String[] args) {
        return this;
    }

    public TaskRegister execute() {
        TaskContext taskContext = new TaskContext();
        for (LL<Task> t = this.tasks; t != null; t = t.tail) {
            t.head.execute(taskContext);
        }
        return this;
    }

    public TaskRegister setIn(InputStream in) {
        Assert.assertNotNull(in);
        this.in = in;
        return this;
    }

    public TaskRegister setOut(PrintStream out) {
        Assert.assertNotNull(out);
        this.out = out;
        return this;
    }

    public TaskRegister register(TaskProvider taskProvider) {
        taskProvider.build(this.in, this.out, this.errorMsg);
        return this;
    }

    public void register(SimpleTask simpleTask) {
        this.tasks = LL.<Task>insertRear(this.tasks, simpleTask);
    }

    public void register(BooleanTask booleanTask) {
        this.tasks = LL.<Task>insertRear(this.tasks, booleanTask);
    }

    public TaskRegister setErrorHandler(ErrorMsg errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}