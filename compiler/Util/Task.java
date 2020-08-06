package Util;

public abstract class Task implements Comparable<Task> {
    public abstract void execute(TaskContext taskContext);

    public Task next;

    @Override
    public int compareTo(Task o) {
        return this.hashCode();
    }
}