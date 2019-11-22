
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;

public class Chap5Test {

    @Test
    public void printtest() {
        String tigerCode = "let in print(\"string\") end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void flush_test() {
        String tigerCode = "let in flush() end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void type_var_dec_correct_int() {
        String tigerCode = "let var badVariable:int := 123 in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void type_var_dec_correct_string() {
        String tigerCode = "let var badVariable:string := \"string\" in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void type_var_dec_incorrect_int() {
        String tigerCode = "let var badVariable:int := \"string\" in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertTrue(m.hasErrors());
    }

    @Test
    public void type_var_dec_custom_type() {
        String tigerCode = "let type t1 = int var v1:int := 1 in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void type_var_dec_custom_type1() {
        // table init - add a type mapping from int -> INT
        // semant - add type mapping from t1 -> int
        // semant - check type of v1 is the same as int exp
        // semant - add type mapping from t2 -> type of t1 ( int )
        String tigerCode = "let type t1 = int var v1:t1 := 123 type t2 = t1 var v2:t2 := 345 type t3 = t2 var v3:t3 := 347  in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void type_var_dec_rec_invalid() {
        String tigerCode = "let type rectype = {name:string, age:int} var rec1 := rectype {name=\"Nobody\", age=\"Nobody\"} in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertTrue(m.hasErrors());
    }

    @Test
    public void type_var_dec_rec_valid() {
        String tigerCode = "let type rectype = {name:string, age:int} var rec1:rectype := rectype {name=\"Nobody\", age=2345}   in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void type_var_dec_array_valid() {
        String tigerCode = "let type arrtype1 = array of int var arr1 := arrtype1 [10] of 0 in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void type_var_dec_array_invalid() {
        String tigerCode = "let type arrtype1 = array of int var arr1 := arrtype1 [10] of \"x\" in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertTrue(m.hasErrors());
    }

    @Test
    public void sequence_empty_string() {
        String tigerCode = "(1;2;\"dsds\")";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void sequence_empty_void() {
        String tigerCode = "()";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void let_sequence_string() {
        String tigerCode = "let in (1;2;\"dsds\") end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void let_sequence_empty_void() {
        String tigerCode = "let in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void mutual_rec_type() {
        String tigerCode = "let type list = { first:int, last:list} in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void array_type() {
        String tigerCode = "let var N := 8 type intArray = array of int var row := intArray [ N ] of 0 in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void array_type_invalid_subscript() {
        String tigerCode = "let var N := \"string\" type intArray = array of int var row := intArray [ N ] of 0 in ( row[0] ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertTrue(m.hasErrors());
    }

    @Test
    public void non_recursive_function_invalid_return_type() {
        String tigerCode = "let function functionname():string = (1) in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertTrue(m.hasErrors());
    }

    @Test
    public void non_recursive_function_valid_return_type() {
        String tigerCode = "let function functionname():int = (1) in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void call_invalid_function_name() {
        String tigerCode = "let function test(a:string, b:int):int = (1) in (est()) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertTrue(m.hasErrors());
    }

    @Test
    public void call_invalid_function_no_arguments() {
        String tigerCode = "let function test(a:string):int = (1) in (test()) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertTrue(m.hasErrors());
    }

    @Test
    public void call_invalid_function_to_many_arguments() {
        String tigerCode = "let function test(a:int, b:int):int = (1) in (test(1,2,3)) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertTrue(m.hasErrors());
    }

    @Test
    public void call_invalid_function_to_few_arguments() {
        String tigerCode = "let function test(a:int, b:int):int = (1) in (test(1)) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertTrue(m.hasErrors());
    }

    @Test
    public void call_invalid_function_invalid_type_arguments() {
        String tigerCode = "let function test(a:string, b:int):int = (2) in (test(1, 1)) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertTrue(m.hasErrors());
    }

    @Test
    public void call_invalid_function_invalid_order_arguments() {
        String tigerCode = "let function test(a:string, b:int):int = (2) in (test(1, \"string\")) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertTrue(m.hasErrors());
    }

    @Test
    public void call_valid_function_two_arguments() {
        String tigerCode = "let function test(a:int, b:string):int = (2) in (test(1, \"string\")) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void call_mut_rec_function() {
        String tigerCode = "let function test(a: int): int = ( test(a) ) in (test(1)) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void call_rec_function() {
        String tigerCode = "let var a := 5 function f():int = g(a) function g(i: int):int = f() in f() end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void invalid_field_type() {
        String tigerCode = "let type myrec = {num:int} var a:myrec := myrec{num=12} in (a.num := 4) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void break_illegal_position() {
        String tigerCode = "let var a:int := 1 in a := a + 1;break end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertTrue(m.hasErrors());
    }

    @Test
    public void for_loop() {
        String tigerCode = "let var a:int := 1 in (for j := 0 to 10 do a := a + 1) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void for_loop_with_break() {
        String tigerCode = "let var a:int := 1 in (for j := 0 to 10 do break) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }


    @Test
    public void while_loop() {
        String tigerCode = "let var a:int := 1 in ( while a < 10 do a := a + 1) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void while_loop_with_break() {
        String tigerCode = "let var a:int := 1 in ( while a < 10 do ( a := a + 1; break ) ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }



}