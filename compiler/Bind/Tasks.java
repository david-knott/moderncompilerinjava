package Bind;

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
        }, "bind", "Performs binding", "parse");
    }
}