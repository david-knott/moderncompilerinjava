package Parse;

import Util.BooleanTask;
import Util.BooleanTaskFlag;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;
import Util.TaskRegister;
import java_cup.parser;

import java.io.FileNotFoundException;

import Absyn.DecList;
import Util.Assert;

/**
 * Provides a collection of tasks related to the parse phase. Constructor
 * accepts the parser implementation to use.
 */
public class Tasks implements TaskProvider {

    final ParserService parserService;

    public Tasks(ParserService parserService) {
        Assert.assertNotNull(parserService);
        this.parserService = parserService;
    }

    @Override
    public void build(TaskRegister taskRegister) {
        taskRegister.register(
            new BooleanTask(new BooleanTaskFlag() {
                @Override
                public void set() {
                    parserService.configure(p -> p.setParserTrace(true));
                }
            }, "parse-trace", "Enable parsers traces.", "parse")
        );
        taskRegister.register(
            new BooleanTask(new BooleanTaskFlag() {
                @Override
                public void set() {
                    parserService.configure(p -> p.setNoPrelude(true));
                }
            }, "X|no-prelude", "Don’t include prelude.", "parse")
        );
        taskRegister.register(
            new SimpleTask(new SimpleTaskProvider() {
                @Override
                public void only(TaskContext taskContext) {
                    DecList decList = parserService.parse(taskContext.in, taskContext.errorMsg);
                    if(taskContext.errorMsg.anyErrors) {
                        // there was a lexical or parse error, cannot continue
                        System.exit(0);
                    } else {
                        taskContext.setDecList(decList);
                    }
                }
            }, "parse", "parse", "")
        );
    }
}