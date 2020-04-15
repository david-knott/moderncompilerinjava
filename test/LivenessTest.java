import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;


public class LivenessTest {

    @Test
    public void sameLevelTest() {
    //    String tigerCode = "let var a:int := 1 var b:int := 0  var c:int := 0 in (b := a + 1; c := c + b; a := b * 2; c ) end";
   //     String tigerCode = "let var a:int := 9 var b:int := 8  in a + b end";
        String tigerCode = "let var a:int := 9 in a end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }
}