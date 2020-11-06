package Util;

public class SimpleTask extends Task {
    private SimpleTaskProvider prov;

    public SimpleTask(SimpleTaskProvider prov, String name, String description, String deps) {
        super(name, description, deps);
        this.prov = prov;
        TaskRegister.instance.register(this);
    }

    @Override
    public void execute(TaskContext taskContext) {
        this.prov.only(taskContext);
    }
}