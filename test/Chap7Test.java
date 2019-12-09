import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;

public class Chap7Test {

    @Test
    public void formals_six_or_less_in_temporaries() {
        String tigerCode = "let function fa(aa:int, bb:int, cc:int, dd:int):int = (1) in (1) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        //need to get the layout and test
        assertFalse(m.hasErrors());
    }

}