package Util;

/**
 * A task that sets a boolean value.
 */
public class BooleanTask extends Task {
    private BooleanTaskFlag booleanTaskFlag;

    public BooleanTask(BooleanTaskFlag flag, String module, String description, String name, String[] deps) {
        TaskRegister.instance.register(this);
        this.booleanTaskFlag = flag;
    }

    @Override
    public void execute(TaskContext taskContext) {
        this.booleanTaskFlag.set();
    }
}