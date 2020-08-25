package Main;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import Canon.CanonicalizationImpl;
import ErrorMsg.ErrorMsg;
import Parse.CupParser;
import RegAlloc.RegAllocFactory;
import Util.TaskRegister;

/**
 * Main class that executes the compiler.
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        PrintStream out = new PrintStream(new java.io.FileOutputStream(args[0] + ".s"));
        if (args.length == 1) {
            args = new String[] { "reg-alloc", args[0] };
        }
        // PrintStream out = System.out;
        InputStream in = new java.io.FileInputStream(args[args.length - 1]);
        PrintStream err = System.err;
        ErrorMsg errorMsg = new ErrorMsg(args[args.length - 1], err);
        TaskRegister.instance.setErrorHandler(errorMsg).setIn(in).setOut(out)
                .register(new Parse.Tasks(new CupParser(in, errorMsg))).register(new FindEscape.Tasks())
                .register(new Absyn.Tasks()).register(new Semant.Tasks()).register(new Translate.Tasks())
                .register(new Canon.Tasks(new CanonicalizationImpl())).register(new Intel.Tasks(null, null))
                .register(new RegAlloc.Tasks(new RegAllocFactory())).parseArgs(args).execute();
    }
}