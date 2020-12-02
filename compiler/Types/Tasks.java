package Types;

import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;
import Util.TaskRegister;

/**
 * Task provider for type checker.
 */
public class Tasks implements TaskProvider {

    public void build(TaskRegister taskRegister) {
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    taskContext.decList.accept(new TypeChecker(taskContext.errorMsg));
                }
            }, "types-compute", "Type checks the abstract syntax tree.", "bindings-compute")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    taskContext.decList.accept(new TypeChecker(taskContext.errorMsg));
                }
            }, "typed", "Default type checking.", "types-compute")
        );
    }
}