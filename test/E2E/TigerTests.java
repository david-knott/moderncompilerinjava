package E2E;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;

import Main.Main;

public class TigerTests {

    private InputStream loadFile(String name) throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(name);
        return inputStream;
    }

    @Test
    public void assign() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./test/fixtures/assign.tig"));
        m.compile();
    }

    @Test
    public void ifCondition() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./test/fixtures/ifCondition.tig"));
        m.compile();
    }

    @Test
    public void whileLoop() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./test/fixtures/whileLoop.tig"));
        m.compile();
    }

    @Test
    public void whileBreakLoop() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./test/fixtures/whileBreakLoop.tig"));
        m.compile();
    }

    @Test
    public void forLoop() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./test/fixtures/forLoop.tig"));
        m.compile();
    }

    @Test
    public void functionAdd() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./test/fixtures/functionAdd.tig"));
        m.compile();
    }

    @Test
    public void arrayDec() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./test/fixtures/arrayDec.tig"));
        m.compile();
    }
}