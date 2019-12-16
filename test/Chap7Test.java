import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;

public class Chap7Test {

    @Test
    public void simple_var_translation() {
        String tigerCode = "let var a:int := 1 var b:int := 1 var c:int := 2 in (c := a + b) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        //need to get the layout and test
        assertFalse(m.hasErrors());
    }

    @Test
    public void simple_var_translation_static_link() {
        String tigerCode = "let var a:int := 1 function fa(aa:int):int = (let var b:int := 1 function fb(bb:int):int = (bb + a) in fb(b) end ) in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void subscript_var() {
        String tigerCode = "let var N := 8 type intArray = array of int var row := intArray [ N ] of 0 in ( row[2] ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void record_var() {
        String tigerCode = "let type rectype = {name:string, age:int} var rec1 := rectype {name=\"Nobody\", age=1} in (rec1.name) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }
}