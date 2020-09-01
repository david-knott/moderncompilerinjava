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

    class TaskWrapper implements Comparable<TaskWrapper> {
        final Task task;
        final String longName;
        final char shortName;
        String args = null;

        public TaskWrapper(Task task, String longName, char shortName) {
            this.task = task;
            this.longName = longName;
            this.shortName = shortName;
        }

        @Override
        public int compareTo(TaskWrapper o) {
            // TODO Auto-generated method stub
            return 0;
        }
    }

    private LL<TaskWrapper> tasks = null;

    public TaskRegister parseArgs(String[] args) {
        //only interested in n - 1 args, as nth arg is filename.
        for(int i = 0; i < args.length - 1; i++) {
            if(args[i].startsWith("--")) {
                Task task = this.findTaskByLongName(args[i].substring(2));
                this.resolveDeps(task);
                if(i + 1 < args.length - 1  && args[i + 1].length() > 2 && !args[i + 1].substring(0, 2).equals("--")) {
                    System.out.println("lvalue =" + args[i + 1]);
                    i++;
                    // ignored for now
                    // set argument in TaskWrapper.
                    // use argument in execute method below.
                }
            } else if(args[i].startsWith("-")) {
                for(int j = 1; j < args[i].length(); j++) {
                    System.out.println("sarg=" + args[i].charAt(j));
                    Task task = this.findTaskByShortName(args[i].charAt(j));
                    this.resolveDeps(task);
                }

            } else {
                throw new Error("Invalid argument syntax, show help.. " + args[i]);
            }
        }
        return this;
    }
 
    private void resolveDeps(Task task) {
        task.active = true;
        if(task.deps != null) {
            for(String dep : task.deps.split("\\s+")) {
                if(dep != "") {
                    Task depTask = this.findTaskByLongName(dep);
                    this.resolveDeps(depTask);
                }
            }
        }
    }

    private Task findTaskByLongName(String name) {
        LL<TaskWrapper> f = this.tasks;
        for(;f != null && !f.head.longName.equals(name); f = f.tail);
        if(f == null) throw new Error("No task for long name:  '" +  name + "'");
        return f.head.task;
    }


    private Task findTaskByShortName(char name) {
        LL<TaskWrapper> f = this.tasks;
        for(;f != null && f.head.shortName != name; f = f.tail);
        if(f == null) throw new Error("No task for short name:  '" +  name + "'");
        return f.head.task;
    }

    public TaskRegister execute() {
        TaskContext taskContext = new TaskContext();
        for (LL<TaskWrapper> t = this.tasks; t != null; t = t.tail) {
            if(t.head.task.active) {
                Timer.instance.push(t.head.task.name);
                t.head.task.execute(taskContext);
                Timer.instance.pop();
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
        String longName = simpleTask.name;
        char shortName = 'x';
        this.tasks = LL.<TaskWrapper>insertRear(this.tasks, new TaskWrapper(simpleTask, longName, shortName));
    }

    public void register(BooleanTask booleanTask) {
        String longName = booleanTask.name;
        char shortName = 'x';
        this.tasks = LL.<TaskWrapper>insertRear(this.tasks, new TaskWrapper(booleanTask, longName, shortName));
    }

    public TaskRegister setErrorHandler(ErrorMsg errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}