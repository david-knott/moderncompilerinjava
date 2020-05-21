import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;

public class WhileTest {


    @Test
    public void basic_two() {
        String tigerCode = "let var a:int := 555 in a:= a + 111; a end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }


    @Test
    public void basic_three() {
        String tigerCode = "let var a:int := 555 var b:int := 666 in a := a + 1; b := a + b; b := b + 1 end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void basic_one() {
        String tigerCode = "let var b:int := 999 var a:int := 99 in while a < 1 do (a := a - b); a := a + 111; a  end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }
}