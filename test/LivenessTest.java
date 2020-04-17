import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;


public class LivenessTest {

    @Test
    public void sameLevelTest() {
        String tigerCode = "let var z:int := 11 var a:int := 7 var b:int := 66  var c:int := 22 in (b := a + 3; c := c + b; c := b * 5; z := c; z ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }
}