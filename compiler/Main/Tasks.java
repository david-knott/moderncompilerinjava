package Main;

import Util.TaskProvider;
import Util.TaskRegister;

public class Tasks implements TaskProvider {

    @Override
    public void build(TaskRegister taskRegister) {
        /*
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
        */
    }
}