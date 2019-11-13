
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;

public class Chap5Test {

    @Test
    public void type_var_dec_correct_int() {
        String tigerCode = "let var badVariable:int := 123 in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertFalse(m.hasErrors());
    }

    @Test
    public void type_var_dec_correct_string() {
        String tigerCode = "let var badVariable:string := \"string\" in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertFalse(m.hasErrors());
    }

    @Test
    public void type_var_dec_incorrect_int() {
        String tigerCode = "let var badVariable:int := \"string\" in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertTrue(m.hasErrors());
    }

    @Test
    public void type_var_dec_custom_type() {
        String tigerCode = "let type t1 = int var v1:int := 1 in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertFalse(m.hasErrors());
    }

    @Test
    public void type_var_dec_custom_type1() {
        //table init - add a type mapping from int -> INT
        //semant - add type mapping from t1 -> int
        //semant - check type of v1 is the same as int exp 
        //semant - add type mapping from t2 -> type of t1 ( int )
        String tigerCode = "let type t1 = int var v1:t1 := 123 type t2 = t1 var v2:t2 := 345 type t3 = t2 var v3:t3 := 347  in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertFalse(m.hasErrors());
    }

    @Test
    public void type_var_dec_rec_invalid() {
        String tigerCode = "let type rectype = {name:string, age:int} var rec1 := rectype {name=\"Nobody\", age=\"Nobody\"} in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertTrue(m.hasErrors());
    }

    @Test
    public void type_var_dec_rec_valid() {
        String tigerCode = "let type rectype = {name:string, age:int} var rec1:rectype := rectype {name=\"Nobody\", age=2345}   in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertFalse(m.hasErrors());
    }


    @Test
    public void type_var_dec_array_valid() {
        String tigerCode = "let type arrtype1 = array of int var arr1 := arrtype1 [10] of 0 in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertFalse(m.hasErrors());
    }


    @Test
    public void type_var_dec_array_invalid() {
        String tigerCode = "let type arrtype1 = array of int var arr1 := arrtype1 [10] of \"x\" in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertTrue(m.hasErrors());
    }

    @Test
    public void sequence_empty_string() {
        String tigerCode = "(1;2;\"dsds\")";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertFalse(m.hasErrors());
    }

    @Test
    public void sequence_empty_void() {
        String tigerCode = "()";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertFalse(m.hasErrors());
    }

    @Test
    public void let_sequence_string() {
        String tigerCode = "let in (1;2;\"dsds\") end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertFalse(m.hasErrors());
    }

    @Test
    public void let_sequence_empty_void() {
        String tigerCode = "let in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertFalse(m.hasErrors());
    }

   @Test
    public void mutual_rec_type() {
        String tigerCode = "let type list = { first:int, last:list} in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        //m.getTypeSymbolTable().
        assertFalse(m.hasErrors());
    }


    @Test
    public void array_type() {
        String tigerCode = "let var N := 8 type intArray = array of int var row := intArray [ N ] of 0 in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertFalse(m.hasErrors());
    }

    @Test
    public void array_type_invalid_subscript() {
        String tigerCode = "let var N := \"string\" type intArray = array of int var row := intArray [ N ] of 0 in ( row[0] ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertTrue(m.hasErrors());
    }

}