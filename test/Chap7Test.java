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
        // need to get the layout and test
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
        String tigerCode = "let type intype = int type rectype = {name:string, age:int} var rec1 := rectype {name=\"Nobody\", age=1} in (rec1.name := \"Nobody\" ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void int_ir() {
        String tigerCode = "10";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void binop_plus_ir() {
        String tigerCode = "10 + 1";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void binop_minus_minus_ir() {
        String tigerCode = "10 + 8 - 3 - 1";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void int_seq_ir() {
        String tigerCode = "(1;4;9;\"string\")";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void if_then_else() {
        String tigerCode = "if (1) then (2) else (3)";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void if_then_else_and() {
        String tigerCode = "if 1 & 2 then (3) else (4)";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void if_then_else_and_seq() {
        String tigerCode = "if (10 < 11 & 20 > 23) then 3 else 4";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap7", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void while_stm() {
        String tigerCode = "let var a:int := 1 in while a = 2 do a := 1 end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap7", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void while_break_stm() {
        String tigerCode = "let var a:int := 1 in while a = 2 do ( 11; 22; break; a := 1 ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap7", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void int_assign(){
        String tigerCode = "let var a:int := 1 in (a := a - 10) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap7", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void record_assign(){
        String tigerCode = "let type rectype = {name:string, age:int} var rec1 := rectype {name=\"Nobody\", age=999} in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap7", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void array_assign(){

        assertFalse(true);
    }
}