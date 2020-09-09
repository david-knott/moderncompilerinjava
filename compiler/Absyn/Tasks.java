package Absyn;

import java.io.PrintStream;

import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;

public class Tasks implements TaskProvider {

    @Override
    public void build() {
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                try(PrintStream printStream = new PrintStream(taskContext.log)) {
                    printStream.println("/* == Abstract Syntax Tree. == */");
                    //taskContext.program.absyn.accept(new PrettyPrinter(printStream, taskContext.escapesDisplay, taskContext.bindingsDisplay));
                    new Print(printStream).prExp(taskContext.program.absyn);
                }
            }
        }, "A|ast-display", "display abstract syntax tree", "parse");
    }
}