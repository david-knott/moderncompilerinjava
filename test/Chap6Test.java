import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;

public class Chap6Test {

    @Test
    public void formals_six_or_less_in_temporaries() {
        String tigerCode = "let function fa(aa:int, bb:int, cc:int, dd:int):int = (1) in (1) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        //need to get the layout and test
        assertFalse(m.hasErrors());
    }


    @Test
    public void formals_greater_than_six_in_() {
        String tigerCode = "let function fa(aa:int, bb:int, cc:int, dd:int, ee:int, ff:int, gg:int, hh:int):int = (1) in (1) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void locals() {
        String tigerCode = "let function fa(aa:int, bb:int, cc:int, dd:int, ee:int, ff:int, gg:int, hh:int):int = (let var a:= 1 var b := 2 var c := 4 in (gg * hh + a + b) end) in (3) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }
}