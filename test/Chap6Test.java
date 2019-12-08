import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;

public class Chap6Test {

    @Test
    public void printtest() {
        String tigerCode = "let var a:int := 1 function fa(aa:int, bb:int):int = fb(aa, bb) + bb + a  function fb(a:int, b:int):int = a * b in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }
}