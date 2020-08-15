package Intel;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import Canon.CanonFacadeImpl;
import ErrorMsg.ErrorMsg;
import RegAlloc.IterativeCoalescing;
import Translate.FragList;
import Translate.TestThingy;
import Util.Assert;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;

public class Tasks implements TaskProvider {

    @Override
    public void build(InputStream in, OutputStream out, ErrorMsg errorMsg) {
        Assert.assertNotNull(errorMsg);
        Assert.assertNotNull(in);
        Assert.assertNotNull(out);
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                // target_set(new x64(ruleTraceP))
                FragList frags = taskContext.fragList;
                /*
                 * if (semant.hasErrors()) { System.out.println("semant check error");
                 * System.exit(1); } // codegen, liveness, register allocation
                 */
                PrintStream fileOut = new PrintStream(out);
                // fileOut = new PrintStream(new java.io.FileOutputStream(name + ".s"));
                fileOut.println(".global tigermain");
                for (; frags != null; frags = frags.tail) {
                    // this.registerFragListners(frags.head);
                    frags.head.process(fileOut);
                }

//                frags.accept(new TestThingy(/* new IterativeCoalescing(frame, instrList) */, new CanonFacadeImpl(), out));
                fileOut.close();
            }
        }, "target-x64", "Select x64 as target", new String[] { "" });
    }
}