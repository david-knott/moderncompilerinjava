package Parse;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;

import Absyn.Absyn;
import Absyn.DecList;
import Absyn.Exp;
import Absyn.FunctionDec;
import ErrorMsg.ErrorMsg;
import Symbol.Symbol;

public class ParserService {

    private final ParserFactory parserFactory;
    private final ParserServiceConfiguration parserServiceConfiguration;

    public ParserService(ParserFactory parserFactory) {
        this.parserFactory = parserFactory;
        this.parserServiceConfiguration = new ParserServiceConfiguration();
    }

    public void configure(ParserServiceConfigurator parserServiceConfigurator) {
        parserServiceConfigurator.configure(parserServiceConfiguration);
        parserFactory.setParserTrace(parserServiceConfiguration.isParserTrace());
    }

    public DecList parse(String code, ErrorMsg errorMsg) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(code.getBytes(Charset.defaultCharset()));
        return parse(byteArrayInputStream, errorMsg);
    }

    public DecList parse(InputStream inputStream, ErrorMsg errorMsg) {
        // parse the program. This is one top level function that contains user code.
        Parser parser = parserFactory.getParser(inputStream, errorMsg);
        Absyn tree = parser.parse();
        DecList program = null;
        // TODO: Checking for DecList might not be neccessary.
        if(tree instanceof DecList) {
            program = (DecList)tree;
        }
        if(tree instanceof Exp) {
            program = new DecList(
                new FunctionDec(0, 
                    Symbol.symbol("tigermain"), 
                    null, 
                    null, 
                    (Exp)tree, 
                    null
                ), 
                null
            );
        }
        if(parserServiceConfiguration.isNoPrelude()) {
            return program;
        }
        Parser prelude;
        try {
            prelude = parserFactory.getParser(new FileInputStream("./data/prelude.tih"), errorMsg);
        } catch (FileNotFoundException e) {
            //   System.out.println("Working Directory = " + System.getProperty("user.dir"));
            throw new PreludeFileNotFoundException();
        }
        DecList preludeList = (DecList)prelude.parse();
        // append the user code to end of prelude declarations.
        DecList end;
        for (end = preludeList; end != null && end.tail != null; end = end.tail)
            ;
        end.tail = program;
        return preludeList;
    }
}