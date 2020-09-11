package Bind;

import Parse.Program;
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
                Binder b = new Binder(taskContext.errorMsg);
                taskContext.program.absyn.accept(b);
            }
        }, "b|bindings-compute", "Performs binding", "parse");
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                taskContext.setBindingsDisplay(true);
            }
        }, "B|bindings-display", "escape", "bindings-compute");
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                Program boundProgram = taskContext.program;
                boundProgram.absyn.accept(new Renamer());
            }
        }, "rename", "Performs binding", "bindings-compute");
    }
}