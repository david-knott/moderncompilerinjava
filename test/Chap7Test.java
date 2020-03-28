import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import Main.Main;

public class Chap7Test {

    @Test
    public void flush() {
        String tigerCode = "let in flush() end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void getchar() {
        String tigerCode = "let var a:string := \"dabid test\" in a := getchar() end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void int_dec_translation() {
        String tigerCode = "let var a:int := 1 in a end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void int_2dec_translation() {
        String tigerCode = "let var a:int := 1 var b:int := 2 in a + b end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void int_3dec_translation() {
        String tigerCode = "let var a:int := 1 var b:int := 2 var c:int := 3 in a + b + c end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }



    @Test
    public void string_dec_translation() {
        String tigerCode = "let var a:string := \"david\" in a end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void int_array_dec_translation() {
        String tigerCode = "let type intArray = array of int var row := intArray [10] of 2 in row[1] end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void string_array_dec_translation() {
        String tigerCode = "let type stringArray = array of string var row := stringArray [2] of \"david\" in 1 end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void record_dec() {
        String tigerCode = "let type rectype = {name:string, age:int} var rec1 := rectype {name=\"Nobody\", age=999} in rec1.name  end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void record_in_record_dec() {
        String tigerCode = "let type recInnerType = {id:int} var inner := recInnerType{id=1} type rectype = {name:string, age:int, inner: recInnerType} var rec1 := rectype {name=\"Nobody\", age=1, inner=inner} in rec1.age end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void record_in_record_read() {
        String tigerCode = "let type recInnerType = {id:int} var inner := recInnerType{id=999} type rectype = {name:string, age:int, inner: recInnerType} var rec1 := rectype {name=\"Nobody\", age=46, inner=inner} in rec1.inner.id end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void static_link_first_argument_in_callee() {
        String tigerCode = "let function fa(id:int):int = id in fa(2) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void static_link_variable_in_parent() {
        String tigerCode = "let var a:int := 1 function fa(id:int, id2:int, id3:int, id4:int, id5:int):int = (a + id) in fa(2, 3, 4, 5, 6) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        System.out.println();
        assertFalse(m.hasErrors());
    }

    @Test
    public void access_var_by_static_link() {
        String tigerCode = "let function fp(a:int,b:int,c:int,d:int,e:int,f:int):int = let function fc(): int = f * f in fc() end  in fp(1,2,3,4,5,99) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void access_var_by_static_link_local() {
        String tigerCode = "let function fa(i:int, j:int):int = let var a:int := 10 var b:int := 20 in a + b end in fa(1, 2) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void read_simple_var() {
        String tigerCode = "let var a:int := 5 in a end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void write_simple_var() {
        String tigerCode = "let var a:int := 5 in (a := 6; a) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void read_subscript_var() {
        String tigerCode = "let type intArray = array of int var row := intArray [10] of 9 in row[0] end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void write_subscript_var() {
        String tigerCode = "let type intArray = array of int var row := intArray [10] of 9 in row[0] := 222 end";
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
    public void binop_mult() {
        String tigerCode = "10 * 10";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void binop_div() {
        String tigerCode = "let in 14 / 13 end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void binop_less_eq() {
        String tigerCode = "let in 25 <= 26 end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void binop_greater_eq() {
        String tigerCode = "26 >= 25";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void binop_less() {
        String tigerCode = "25 < 26";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void binop_greater() {
        String tigerCode = "26 > 25";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void binop_and() {
        String tigerCode = "let in if 2 < 3 & 4 < 5 then 100 else 101 end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void binop_or() {
        String tigerCode = "let in if 2 < 3 | 4 < 5 then 100 else 101 end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }



    @Test
    public void binop_minus_minus_ir() {
        String tigerCode = "let in 10 + 8 - 3 - 1 end";
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
        String tigerCode = "let in if 1 & 2 then (3) else (4) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void if_then_else_and_seq() {
        String tigerCode = "let in if (10 < 11 & 20 > 23) then 3 else 4 end";
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
    public void int_assign() {
        String tigerCode = "let var a:int := 1 in (a := a - 10) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap7", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void record_assign() {
        String tigerCode = "let type rectype = {age:int} var rec1 := rectype {age=999} in rec1.age / 1 end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap7", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void array_assign() {
        String tigerCode = "let type arrtype1 = array of int var arr1 := arrtype1 [4] of 44 in arr1[0] end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void type_def_cycle() {
        String tigerCode = "let type a = b type b = d type c = a type d = a in  end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        System.out.println();
        assertFalse(m.hasErrors());
    }

    @Test
    public void type_def_legal_cycle() {
        String tigerCode = "let type rectype = {age:int,next:rectype} var rec1 := rectype {age=999,next=nil} in rec1.age end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        System.out.println();
        assertFalse(m.hasErrors());
    }

    @Test
    public void translated_for() {
        String tigerCode = "for i := 1 to 9 do i";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        System.out.println();
        assertFalse(m.hasErrors());
    }

    @Test
    public void translated_while() {
        String tigerCode = "while(1) do ()";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        System.out.println();
        assertFalse(m.hasErrors());
    }

    @Test
    public void array_bounds_check() {
        String tigerCode = "let type intArray = array of int var row := intArray [2] of 2 in row[10] end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void record_nil_check() {
        String tigerCode = "let type rectype = {name:string, age:int} var rec := rectype {name=\"Nobody\", age=1} in rec.age end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void def_func() {
        String tigerCode = "exit(1)";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap6", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }
}