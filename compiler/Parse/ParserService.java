package Parse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import Absyn.Absyn;
import Absyn.DecList;
import Absyn.Exp;
import Absyn.FunctionDec;
import Absyn.PrettyPrinter;
import ErrorMsg.ErrorMsg;
import Symbol.Symbol;
import java_cup.parser;

public class ParserService {

    private final ParserFactory parserFactory;
    private final ParserServiceConfiguration parserServiceConfiguration;

    public ParserService(ParserFactory parserFactory) {
        this.parserFactory = parserFactory;
        this.parserServiceConfiguration = new ParserServiceConfiguration();
    }

    public void configure(ParserServiceConfigurator parserServiceConfigurator) {
        parserServiceConfigurator.configure(parserServiceConfiguration);
    }

    public DecList parse(InputStream inputStream, ErrorMsg errorMsg) throws FileNotFoundException {
        // parse the program. This is one top level function that contains user code.
        Parser parser = parserFactory.getParser(inputStream, errorMsg);
        Absyn tree = parser.parse();
        DecList program = null;
        if(tree instanceof DecList) {
            program = (DecList)tree;
        }
        if(tree instanceof Exp) {
            program = new DecList(
                new FunctionDec(0, Symbol.symbol("_main"), null, null, (Exp)tree, null), 
                null
            );
        }
        if(parserServiceConfiguration.isNoPrelude()) {
            return program;
        }
        // parse the prelude file which contains the references to 
        // the functions defined in the runtime.
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Parser prelude = parserFactory.getParser(new FileInputStream("./data/prelude.tih"), errorMsg);
        DecList preludeList = (DecList)prelude.parse();
        // append the user code to end of prelude declarations.
        DecList end;
        for (end = preludeList; end != null && end.tail != null; end = end.tail)
            ;
        end.tail = program;
        return preludeList;
    }
}