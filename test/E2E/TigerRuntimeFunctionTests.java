package E2E;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;

import Main.Main;

public class TigerRuntimeFunctionTests {
    private InputStream loadFile(String name) throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(name);
        return inputStream;
    }

    @Test
    public void flush() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./test/fixtures/flush.tig"));
        m.compile();
    }

    @Test
    public void print() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./test/fixtures/print.tig"));
        m.compile();
    }
}