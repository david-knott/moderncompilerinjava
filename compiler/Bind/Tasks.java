package Bind;

import Util.TaskProvider;

import Translate.DataFrag;
import Translate.FragList;
import Translate.FragmentVisitor;
import Translate.ProcFrag;
import Tree.TreeSimplifierVisitor;
import Tree.PrettyPrinter;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;

public class Tasks implements TaskProvider {

    @Override
    public void build() {
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                Binder b = new Binder();
                taskContext.program.absyn.accept(b);
            }
        }, "bind", "Performs binding", "parse");
    }
}