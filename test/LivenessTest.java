import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;


public class LivenessTest {


    @Test
    public void empty() {
        String tigerCode = "()";
     //   String tigerCode = "let var z:int := 11 in (z := z + 3; z ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }


    @Test
    public void single() {
        String tigerCode = "(3)";
     //   String tigerCode = "let var z:int := 11 in (z := z + 3; z ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void two() {
        String tigerCode = "(1; 3)";
     //   String tigerCode = "let var z:int := 11 in (z := z + 3; z ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }



    @Test
    public void letSingle() {
        String tigerCode = "let in 3 end";
     //   String tigerCode = "let var z:int := 11 in (z := z + 3; z ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void letTwo() {
        String tigerCode = "let in (5; 3) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void letThree() {
        String tigerCode = "let var a:int := 3 in a end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void sameLevelTest() {
        String tigerCode = "let var a:int := 3 var b:int := 5 var c:int := 7 in (b := a + 3; c := c / b; c := b * 5;  c ) end";
     //   String tigerCode = "let var z:int := 11 in (z := z + 3; z ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void moveFunctionArguments() {
        String tigerCode = "let var c:int := 3 function add(a:int, b:int):int = a + b in (c := add(1,2) + c; c) end";
     //   String tigerCode = "let var z:int := 11 in (z := z + 3; z ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }
}