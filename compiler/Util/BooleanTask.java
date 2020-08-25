package Util;

/**
 * A task that sets a boolean value.
 */
public class BooleanTask extends Task {
    private BooleanTaskFlag booleanTaskFlag;

    public BooleanTask(BooleanTaskFlag flag, String name, String description, String deps) {
        super(name, description, deps);
        Assert.assertNotNull(flag);
        TaskRegister.instance.register(this);
        this.booleanTaskFlag = flag;
    }

    @Override
    public void execute(TaskContext taskContext) {
        this.booleanTaskFlag.set();
    }
}