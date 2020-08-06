package Util;

public class SimpleTask extends Task {
    private SimpleTaskProvider name;

    public SimpleTask(SimpleTaskProvider name, String module, String description, String[] deps) {
        TaskRegister.instance.register(this);
        this.name = name;
    }

    @Override
    public void execute(TaskContext taskContext) {
        this.name.only(taskContext);
    }
}