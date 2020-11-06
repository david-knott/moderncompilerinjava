package Absyn;

import java.io.PrintStream;

import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;
import Util.TaskRegister;

public class Tasks implements TaskProvider {

    @Override
    public void build(TaskRegister taskRegister) {
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    try(PrintStream printStream = new PrintStream(taskContext.log)) {
                        printStream.println("/* == Abstract Syntax Tree. == */");
                        taskContext.decList.accept(new PrettyPrinter(printStream, taskContext.escapesDisplay, taskContext.bindingsDisplay));
                        //new Print(printStream).prExp(taskContext.program.absyn);
                    }
                }
            }, "A|ast-display", "display abstract syntax tree", "parse")
        );
    }
}