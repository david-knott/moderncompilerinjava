package Util;

public abstract class Task {
    final String name;
    final String module;
    final String[] deps;

    public Task(String name, String module, String[] deps) {
        this.name = name;
        this.module = module;
        this.deps = deps;
    }

    public abstract void execute();
}