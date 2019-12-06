import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;

public class FindEscapeTest {

    @Test
    public void test_if_local_escapes() {
        String tigerCode = "let var a:int := 10 function funcA():int = (let var b:int := 20 function funcB(): int = (a + b) in (1) end  ) in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void test_no_if_local_escapes() {
        String tigerCode = "let var b:int := 200  var  a:int := 10 function name():int = (let  function name1(): int = (a + b) in (1) end  ) in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }
}