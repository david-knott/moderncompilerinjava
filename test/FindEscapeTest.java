import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
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
        String tigerCode = "let var b:int := 200 var a:int := 10 function fa(c: int):int = (let var d:int := 1 function fb(): int = (d := (a + b) + c; d) in (fb()) end ) in (fa(0)) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void test_more_than_6_arguments() {
        String tigerCode = "let function fa(a: int, b:int, c:int, d:int, e:int, f:int, g:int, h:int):int = (1) in (fa(0,1,2,3,4,5,6,7)) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void simple_test() {
        String tigerCode = "let var a:int := 1 var b:int := 2 function fa(a:int, b:int):int = (1) in (fa(a, b)) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void merge_test() throws FileNotFoundException {
        InputStream inputStream = new java.io.FileInputStream("reference/tiger/testcases/merge.tig"); 
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void queens_test() throws FileNotFoundException {
        InputStream inputStream = new java.io.FileInputStream("reference/tiger/testcases/queens.tig"); 
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }



}