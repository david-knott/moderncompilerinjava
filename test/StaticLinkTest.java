import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;

public class StaticLinkTest {

    @Test
    public void sameLevelTest() {
        String tigerCode = "let var a:int := 1 in a end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void oneLevelTest() {
        //accesses are in different activation records
        String tigerCode = "let var a:int := 1 function d():int = a in d() end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void formal_inframe_acesss_local_inframe_access() {
        //accesses are in the save activation record
        //formal inframe access is static link
        //local is inside functon d
        String tigerCode = "let function d():int = let var a:int := 1 in a end in d() end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }
}