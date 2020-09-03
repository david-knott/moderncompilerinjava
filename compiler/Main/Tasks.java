package Main;

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
            //    taskContext.out = taskContext.log;
            }
        }, "output", "Output assembly to file", "");

        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
            //    taskContext.out = taskContext.log;
            }
        }, "input", "Input tiger from file", "");
    }
}