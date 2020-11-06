package Cloner;

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
                    AbsynCloner absynCloner = new AbsynCloner();
                    taskContext.decList.accept(absynCloner);
                    taskContext.setDecList(absynCloner.visitedDecList);
                }
            }, "clone", "Clone the ast", "parse")
        );
    }
}