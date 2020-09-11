package FindEscape;


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
                EscapeVisitor escapeVisitor = new EscapeVisitor(taskContext.errorMsg);
                taskContext.program.absyn.accept(escapeVisitor);
            }
        }, "e|escapes-compute", "escape", "parse");

        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                taskContext.setEscapesDisplay(true);

            }
        }, "E|escapes-display", "escape", "escapes-compute");
    }
}