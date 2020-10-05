package Cloner;

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
                AbsynCloner absynCloner = new AbsynCloner();
                taskContext.program.absyn.accept(absynCloner);
                taskContext.setAst(new Program(absynCloner.visitedExp));
            }
        }, "clone", "Clone the ast", "parse");
    }
}