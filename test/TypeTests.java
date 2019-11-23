
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import ErrorMsg.TypeMismatchError;
import ErrorMsg.UndefinedVariableError;
import Main.Main;
import Semant.Semant;

/**
 * A set of tests that ensure the type checking rules have been implemented
 * correctly
 */
public class TypeTests {

    @Test
    public void fundec_void_return() {
        String tigerCode = "let function test(i: string) = print(i) in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void fundec_int_return() {
        String tigerCode = "let function test(i: int):int = i in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void fundec_string_return() {
        String tigerCode = "let function test(i: string):string = i in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void vardec_dec_type_matches_initializer() {
        String tigerCode = "let type t = int var v:t := 1 in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertFalse(m.hasErrors());
    }

    @Test
    public void vardec_dec_type_does_not_match_initializer() {
        String tigerCode = "let type t = string var v:t := 1 in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertTrue(m.hasErrors());
    }

    @Test
    public void id_refer_to_variable() {
        String tigerCode = "let var v:int := 1 in ( v := b ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        m.getErrorMsg().getCompilerErrors().stream().findAny().ifPresent(a -> {
            assertTrue(a instanceof UndefinedVariableError);
            if(a instanceof UndefinedVariableError){
                assertEquals("b", ((UndefinedVariableError)a).getSymbol().toString());
            }
        });
    }

    @Test
    public void id_result_type_is_type_of_variable() {
        String tigerCode = "let var v:int := 1 var b:string := \"test-string\" in ( v := b ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        m.getErrorMsg().getCompilerErrors().stream().findAny().ifPresent(a -> {
            assertTrue(a instanceof TypeMismatchError);
            if(a instanceof TypeMismatchError){
                assertEquals(Semant.INT, ((TypeMismatchError)a).getType1());
                assertEquals(Semant.STRING, ((TypeMismatchError)a).getType2());
            }
        });
    }

    @Test
    public void subscript_base_expression_must_have_array_type() {
        String tigerCode = "let var a:= 1 var b := 1 in ( a := b[1] ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
    }

    @Test
    public void subscript_index_of_type_int() {
        String tigerCode = "let var N := \"i\" type intArray = array of int var row := intArray [ N ] of 0 in (  ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        m.getErrorMsg().getCompilerErrors().stream().findAny().ifPresent(a -> {
            assertTrue(a instanceof TypeMismatchError);
            if(a instanceof TypeMismatchError){
                assertEquals(Semant.STRING, ((TypeMismatchError)a).getType1());
                assertEquals(Semant.INT, ((TypeMismatchError)a).getType2());
            }
        });
    }

    @Test
    public void result_type_is_element_type_of_array() {
        String tigerCode = "let var c:string := \"\" var N := 8 type intArray = array of int var row := intArray [ N ] of 0 in ( c:= row[1]  ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        m.getErrorMsg().getCompilerErrors().stream().findAny().ifPresent(a -> {
            assertTrue(a instanceof TypeMismatchError);
            if(a instanceof TypeMismatchError){
                assertEquals(Semant.STRING, ((TypeMismatchError)a).getType1());
              ////  assertEquals(Semant.INT, ((TypeMismatchError)a).getType2());
            }
        });
    }

    @Test
    public void field_type_base_expression_is_record_type() {

    }

    @Test
    public void field_type_identifier_is_field_of_record() {

    }

    @Test
    public void field_type_result_type_is_type_of_field() {

    }

    @Test
    public void exp_nil_used_where_record_type_can_be_determined() {

    }

    @Test
    public void exp_int_has_type_int() {

    }

    @Test
    public void exp_string_has_type_string() {

    }

    @Test
    public void exp_seq_empty_type_is_void() {

    }

    @Test
    public void exp_seq_not_empty_type_is_last_exp_type() {

    }

    @Test
    public void exp_neg_operand_and_result_are_int() {

    }

    @Test
    public void exp_call_identifier_is_function() {

    }

    @Test
    public void exp_call_actual_and_formals_match() {

    }

    @Test
    public void exp_call_type_matches_function_type() {

    }

}