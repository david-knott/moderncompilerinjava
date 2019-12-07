import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;

public class Chap6Test {

    @Test
    public void printtest() {
        String tigerCode = "let function fa(a:int, b:int):int = fb(a, b) + b  function fb(a:int, b:int):int = a * b in fa(1,2) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }
}