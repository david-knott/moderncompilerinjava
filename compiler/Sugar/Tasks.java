package Sugar;

import Parse.Program;
import Util.BooleanTask;
import Util.BooleanTask;
import Util.BooleanTaskFlag;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;

public class Tasks implements TaskProvider {

    @Override
    public void build() {
        new BooleanTask(new BooleanTaskFlag() {
            @Override
            public void set() {
                //set
            }
        }, "desugar-for", "Desugar 'for' loops", "");
        new BooleanTask(new BooleanTaskFlag() {
            @Override
            public void set() {
                //set
            }
        }, "desugar-string-cmp", "Desugar string comparisions", "");
        //TODO: create disjunctive tasks desugared ( removes all syntactix sugar )
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                Desugar desugar = new Desugar();
                taskContext.program.absyn.accept(desugar);
                taskContext.setAst(new Program(desugar.visitedExp));
            }
        }, "desugar", "desugar the AST", "types-compute rename");
    }
}