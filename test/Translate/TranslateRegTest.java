package Translate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import Absyn.Absyn;
import Absyn.PrettyPrinter;
import Bind.Binder;
import ErrorMsg.ErrorMsg;
import FindEscape.EscapeVisitor;
import Parse.ParserFactory;
import Parse.ParserService;


@RunWith(Theories.class)
public class TranslateRegTest {
    
    @DataPoints
    public static String[] paths() {
        try (Stream<Path> walk = Files.walk(Paths.get("./test/E2E/good/"))) {
            return walk.filter(Files::isRegularFile).map(x -> x.toString())
            .filter(f -> f.endsWith(".tig"))
            .collect(Collectors.toList()).toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Theory
    public void good(String fileName) {
        System.out.println("Testing Translator" + fileName);
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        ParserService parserService = new ParserService(new ParserFactory());
        parserService.configure(config -> config.setNoPrelude(false));
        parserService.configure(config -> config.setParserTrace(false));
        try (FileInputStream fin = new FileInputStream(fileName)) {
            Absyn program = parserService.parse(fin, errorMsg);
            program.accept(new EscapeVisitor(errorMsg));
            TranslatorVisitor translator = new TranslatorVisitor();
            program.accept(new Binder(errorMsg));
            program.accept(translator);
            FragList fragList = translator.getFragList();
            assertNotNull(fragList);
            fragList.accept(new FragmentPrinter(System.out));
        }   catch(IOException e) {
            e.printStackTrace();
        } 
    }
}