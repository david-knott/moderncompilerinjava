package Main;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import Canon.CanonicalizationImpl;
import ErrorMsg.ErrorMsg;
import Parse.CupParser;
import RegAlloc.RegAllocFactory;
import Util.TaskRegister;
import Util.Timer;

/**
 * Main class that executes the compiler.
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Timer.instance.start();
        Timer.instance.push("rest");
     //   PrintStream out = new PrintStream(new java.io.FileOutputStream(args[args.length - 1] + ".s"));
        PrintStream out = System.out;
        if (args.length == 1) {
           args = new String[] {"--reg-alloc", "--clone", "--escapes-compute", "--demove", args[0] };
        }
        InputStream in = new java.io.FileInputStream(args[args.length - 1]);
        PrintStream err = System.err;
        ErrorMsg errorMsg = new ErrorMsg(args[args.length - 1], err);
        TaskRegister.instance
                .register(new Tasks())
                .register(new Parse.Tasks(new CupParser(in, errorMsg)))
                .register(new Cloner.Tasks())
                .register(new Bind.Tasks())
                .register(new FindEscape.Tasks())
                .register(new Absyn.Tasks())
                .register(new Types.Tasks())
                .register(new Semant.Tasks())
                .register(new Translate.Tasks())
                .register(new Canon.Tasks(new CanonicalizationImpl()))
                .register(new Intel.Tasks(null, null))
                .register(new RegAlloc.Tasks(new RegAllocFactory()))
                .parseArgs(args)
                .execute(in, out, err, errorMsg);
        Timer.instance.stop();
        Timer.instance.dump(err);
    }
}
