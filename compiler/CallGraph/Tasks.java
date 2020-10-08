package CallGraph;

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
                Program boundProgram = taskContext.program;
            }
        }, "callgraph-compute", "build the call graph", "bindings-compute");
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                Program boundProgram = taskContext.program;
            }
        }, "callgraph-dump", "dump the call graph", "callgraph-compute");
    }
}