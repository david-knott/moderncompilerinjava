package Bind;

import Absyn.DecList;
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
                    Binder b = new Binder(taskContext.errorMsg);
                    taskContext.decList.accept(b);
                }
            }, "b|bindings-compute", "Performs binding", "parse")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    taskContext.setBindingsDisplay(true);
                }
            }, "B|bindings-display", "escape", "bindings-compute")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    DecList boundProgram = taskContext.decList;
                    boundProgram.accept(new Renamer());
                }
            }, "rename", "Performs binding", "bindings-compute")
        );
    }
}