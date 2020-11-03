package Absyn;

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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import ErrorMsg.ErrorMsg;
import Parse.CupParser;
import Parse.Parser;

@RunWith(Theories.class)
public class PrettyPrinterRegTest {
    @DataPoints
    public static String[] paths() {
        try (Stream<Path> walk = Files.walk(Paths.get("./test/E2E/good/"))) {
            return walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList()).toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Theory
    public void good(String fileName) {
        System.out.println("Testing " + fileName);
        ErrorMsg errorMsg = new ErrorMsg("f", System.out);
        try (FileInputStream fin=new FileInputStream(fileName)) {
            Parser parser = new CupParser(fin, errorMsg);
            Absyn program = parser.parse();
            // program being null would indicate a failure.
            assertNotNull(program);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrettyPrinter prettyPrinter = new PrettyPrinter(new PrintStream(outputStream));
            program.accept(prettyPrinter);
            System.out.println(new String(outputStream.toByteArray(), StandardCharsets.UTF_8));

            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            Parser parser2 = new CupParser(inputStream, errorMsg);
            Absyn program2 = parser2.parse();
            // program being null would indicate a failure.
            assertNotNull(program2);
        }   catch(IOException e) {

        } 
                // should parse without errors.
    }
}
