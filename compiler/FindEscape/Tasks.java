package FindEscape;

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
                    EscapeVisitor escapeVisitor = new EscapeVisitor(taskContext.errorMsg);
                    taskContext.decList.accept(escapeVisitor);
                }
            }, "e|escapes-compute", "escape", "parse")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    taskContext.setEscapesDisplay(true);

                }
            }, "E|escapes-display", "escape", "escapes-compute")
        );
    }
}