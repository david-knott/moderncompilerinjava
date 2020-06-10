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
    public void plus() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./test/fixtures/plus.tig"));
        m.compile();
    }

    @Test
    public void minus() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./test/fixtures/minus.tig"));
        m.compile();
    }

    @Test
    public void mult() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./test/fixtures/mult.tig"));
        m.compile();
    }

    @Test
    public void div() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./test/fixtures/div.tig"));
        m.compile();
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

    @Test
    public void arraySet() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./test/fixtures/arraySet.tig"));
        m.compile();
    }
}
