import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;

public class Chap3PrecedenceTest {

    @Test
    public void printtest() {
        String tigerCode = "(1 * 2 * -3;4;10)";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }
}