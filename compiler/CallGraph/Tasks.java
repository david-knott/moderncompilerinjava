package CallGraph;

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
                }
            }, "callgraph-compute", "build the call graph", "bindings-compute")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                }
            }, "callgraph-dump", "dump the call graph", "callgraph-compute")
        );
    }
}