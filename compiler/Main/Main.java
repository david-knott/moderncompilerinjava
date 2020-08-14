package Main;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

import Absyn.Exp;
import Absyn.PrettyPrinter;
import Core.Listener;
import ErrorMsg.ErrorMsg;
import Intel.IntelFrame;
import Parse.Grm;
import Parse.CupParser;
import Parse.Program;
import Parse.Yylex;
import Temp.Label;
import Translate.Frag;
import Translate.FragList;
import Translate.Level;
import Translate.ProcFrag;
import Translate.Translator;
import Tree.Stm;
import Tree.StmList;
import Util.TaskRegister;

class Options {

}

/**
 * Main class that executes the compiler.
 */
public class Main {

    public static void main(final String[] args) throws FileNotFoundException {
        // new Main(args[0]);
        PrintStream out = new PrintStream(new java.io.FileOutputStream(args[0] + ".s"));
        InputStream in = new java.io.FileInputStream(args[0]);
        PrintStream err = System.err;
        ErrorMsg errorMsg = new ErrorMsg(args[0], err);
        TaskRegister.instance
                .setErrorHandler(errorMsg)
                .setIn(in)
                .setOut(out)
                .register(new Parse.Tasks(new CupParser(in, errorMsg)))
                .register(new FindEscape.Tasks())
                .register(new Absyn.Tasks())
          //      .register(new Frame.Tasks())
          //      .register(new Semant.Tasks())
                .register(new Translate.Tasks())
                .register(new Intel.Tasks())
                .parseArgs(args)
                .execute();
    }
}