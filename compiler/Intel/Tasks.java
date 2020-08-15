package Intel;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import Canon.CanonFacadeImpl;
import Canon.Canonicalization;
import ErrorMsg.ErrorMsg;
import RegAlloc.IterativeCoalescing;
import RegAlloc.RegAllocFactory;
import Translate.FragList;
import Translate.TestThingy;
import Util.Assert;
import Util.SimpleTask;
import Util.SimpleTaskProvider;
import Util.TaskContext;
import Util.TaskProvider;

public class Tasks implements TaskProvider {
    final RegAllocFactory regAllocFactory;
    final Canonicalization canonicalization;

    public Tasks(RegAllocFactory regAllocFactory, Canonicalization canonicalization) {
        this.regAllocFactory = regAllocFactory;
        this.canonicalization = canonicalization;
    }

    @Override
    public void build(InputStream in, OutputStream out, ErrorMsg errorMsg) {
        Assert.assertNotNull(errorMsg);
        Assert.assertNotNull(in);
        Assert.assertNotNull(out);
        new SimpleTask(new SimpleTaskProvider() {
            @Override
            public void only(TaskContext taskContext) {
                FragList frags = taskContext.fragList;
                PrintStream fileOut = new PrintStream(out);
                fileOut.println(".global tigermain");
                frags.accept(new TestThingy(regAllocFactory, canonicalization, out));
                fileOut.close();
            }
        }, "target-x64", "Select x64 as target", new String[] { "" });
    }
}