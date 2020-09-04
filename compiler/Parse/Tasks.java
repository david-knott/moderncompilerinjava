package Parse;

import Util.BooleanTask;
import Util.BooleanTaskFlag;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;

import Util.Assert;

/**
 * Provides a collection of tasks related to the parse
 * phase. Constructor accepts the parser implementation
 * to use.
 */
public class Tasks implements TaskProvider {

    final Parser parseTask;

    public Tasks(Parser parser) {
        Assert.assertNotNull(parser);
        this.parseTask = parser;
	}

	@Override
    public void build() {
        new BooleanTask(new BooleanTaskFlag() {
            @Override
            public void set() {
                parseTask.setParserTrace(true);
            }
        }, "parse-trace", "parse-trace", "parse");
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                parseTask.parse(taskContext);
            }
        }, "parse", "parse", ""); 
    }
}