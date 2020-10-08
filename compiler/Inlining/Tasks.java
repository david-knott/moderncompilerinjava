package Inlining;

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
                throw new Error("Not implemented");
            }
        }, "inline", "inline functions", "types-compute rename");
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                throw new Error("Not implemented");
            }
        }, "prune", "prune unused functions", "types-compute rename");

    }
}