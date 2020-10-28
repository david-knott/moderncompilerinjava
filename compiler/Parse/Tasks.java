package Parse;

import Util.BooleanTask;
import Util.BooleanTaskFlag;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;
import java_cup.parser;
import Util.Assert;

/**
 * Provides a collection of tasks related to the parse
 * phase. Constructor accepts the parser implementation
 * to use.
 */
public class Tasks implements TaskProvider {

   // final Parser parser;
    final ParserFactory parserFactory;

    public Tasks(Parser parser) {
        Assert.assertNotNull(parser);
//        this.parser = parser;
        this.parserFactory = null;
    }
    
    public Tasks(ParserFactory parserFactory) {
        Assert.assertNotNull(parserFactory);
        this.parserFactory = parserFactory;
  //      this.parser = null;
    }

	@Override
    public void build() {
        new BooleanTask(new BooleanTaskFlag() {
            @Override
            public void set() {
                parserFactory.setParserTrace(true);
            }
        }, "parse-trace", "parse-trace", "parse");
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                Parser parser = parserFactory.getParser(taskContext.in, taskContext.errorMsg); 
                Program program = parser.parse();
                if(!parser.hasErrors()) {
                    taskContext.setAst(program);
                } else {
                    throw new Error("Errors");
                }
            }
        }, "parse", "parse", ""); 
    }
}