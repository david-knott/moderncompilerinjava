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
        for(int i = 0; i < args.length - 1; ++i) {
            this.resolve(args[i]);
        }
        return this;
    }

    private Task findTask(String name) {
        LL<Task> f = this.tasks;
        for(;f != null && f.head.name != name; f = f.tail);
        if(f == null) throw new Error("No task for name = '" +  name + "'");
        return f.head;
    }

    private void resolve(String name) {
        Task task = this.findTask(name);
        task.active = true;
        if(task.deps != null) {
            for(String dep : task.deps.split("\\s+")) {
                if(dep != "") {
                    this.resolve(dep);
                }
            }
        }
    }

    public TaskRegister execute() {
        TaskContext taskContext = new TaskContext();
        for (LL<Task> t = this.tasks; t != null; t = t.tail) {
            if(t.head.active) {
                t.head.execute(taskContext);
            }
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