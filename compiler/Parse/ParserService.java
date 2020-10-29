package Parse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import Absyn.DecList;
import ErrorMsg.ErrorMsg;

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
        DecList program = parser.parse().getDecList();
        if(parserServiceConfiguration.isNoPrelude()) {
            return program;
        }
        // parse the prelude, external functions defined in the runtime.
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Parser prelude = parserFactory.getParser(new FileInputStream("./data/prelude.tih"), errorMsg);
        DecList preludeList = prelude.parse().getDecList();
        // append the user code to end of prelude declarations.
        DecList end;
        for (end = preludeList; end != null && end.tail != null; end = end.tail)
            ;
        end.tail = program;
        return preludeList;
    }
}