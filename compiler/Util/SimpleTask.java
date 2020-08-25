package Util;

public class SimpleTask extends Task {
    private SimpleTaskProvider prov;

    public SimpleTask(SimpleTaskProvider prov, String name, String description, String deps) {
        super(name, description, deps);
        TaskRegister.instance.register(this);
        this.prov = prov;
    }

    @Override
    public void execute(TaskContext taskContext) {
        this.prov.only(taskContext);
    }
}