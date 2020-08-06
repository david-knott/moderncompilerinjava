package Parse;

import java.io.InputStream;
import java.io.OutputStream;

import ErrorMsg.ErrorMsg;
import Util.BooleanTask;
import Util.BooleanTaskFlag;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.Task;
import Util.TaskContext;
import Util.TaskProvider;

import Util.Assert;

public class Tasks implements TaskProvider {

    public static class PrivateParseTask extends Task {

        boolean parserTrace = false;
        final InputStream inputStream;
        final ErrorMsg errorMsg;
        final Yylex yylex;
        Program ast;

        public PrivateParseTask(InputStream inputStream, ErrorMsg errorMsg) {
            Assert.assertNotNull(inputStream);
            this.inputStream = inputStream;
            this.errorMsg = errorMsg;
            this.yylex = new Yylex(inputStream, errorMsg);
        }

        @Override
        public void execute(TaskContext context) {
            Grm parser = new Grm(new DebugLexer(this.yylex, System.out), this.errorMsg);
            java_cup.runtime.Symbol rootSymbol = null;
            // lexical error happens when nextToken is called
            try {
                rootSymbol = !this.parserTrace ? parser.parse() : parser.debug_parse();
            } catch (Throwable e) {
                throw new Error(e);
            } finally {
                try {
                    inputStream.close();
                } catch (final java.io.IOException e) {
                    throw new Error(e.toString());
                }
            }
            if (!this.errorMsg.anyErrors) {
                context.setAst((Program) rootSymbol.value);
            }
        }
    }

    @Override
    public void build(InputStream in, OutputStream out, ErrorMsg errorMsg) {
        Assert.assertNotNull(errorMsg);
        Assert.assertNotNull(in);
        Assert.assertNotNull(out);
        PrivateParseTask parseTask = new PrivateParseTask(in, errorMsg);
        new BooleanTask(new BooleanTaskFlag() {
            @Override
            public void set() {
                parseTask.parserTrace = true;
            }
        }, "parse-trace", "parse-trace", "parse-trace", null);
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                parseTask.execute(taskContext);
            }
        }, "parse", "parse", null); // should return a function to the
    }
}