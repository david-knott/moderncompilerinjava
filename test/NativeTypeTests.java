
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import ErrorMsg.ArgumentMismatchError;
import ErrorMsg.ErrorMsg;
import ErrorMsg.FieldNotDefinedError;
import ErrorMsg.FunctionNotDefinedError;
import ErrorMsg.TypeMismatchError;
import ErrorMsg.UndefinedVariableError;
import Main.Main;
import Semant.Semant;

/**
 * A set of tests that ensure the type checking rules have been implemented
 * correctly
 */
public class NativeTypeTests {

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
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
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
            if (a instanceof UndefinedVariableError) {
                assertEquals("b", ((UndefinedVariableError) a).getSymbol().toString());
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
            if (a instanceof TypeMismatchError) {
                assertEquals(Semant.INT, ((TypeMismatchError) a).getLeft());
                assertEquals(Semant.STRING, ((TypeMismatchError) a).getRight());
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
            if (a instanceof TypeMismatchError) {
                assertEquals(Semant.STRING, ((TypeMismatchError) a).getLeft());
                assertEquals(Semant.INT, ((TypeMismatchError) a).getRight());
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
            if (a instanceof TypeMismatchError) {
                assertEquals(Semant.STRING, ((TypeMismatchError) a).getLeft());
            }
        });
    }

    @Test
    public void field_type_base_expression_is_record_type() {
        String tigerCode = "let type list = {first: int, rest: list}  var a:list := list{ first = 1} var b := 1 in ( b := a ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        m.getErrorMsg().getCompilerErrors().stream().findAny().ifPresent(a -> {
            assertTrue(a instanceof TypeMismatchError);
            if (a instanceof TypeMismatchError) {
                assertEquals(Semant.INT, ((TypeMismatchError) a).getLeft());
            }
        });
    }

    @Test
    public void field_type_identifier_is_field_of_record() {
        String tigerCode = "let type atype = {first: int}  var a:atype := atype{ first = 1} in ( a.firsty := 1 ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        var typeErrors = extractFieldNotDefinedError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals("firsty", a.getField().toString());
        });
    }

    @Test
    public void field_type_result_type_is_type_of_field() {
        String tigerCode = "let type atype = {first: int}  var a:atype := atype{ first = 1} var b:string := \"\" in ( b := a.first ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        var typeErrors = extractTypeMismatchError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getRight(), Semant.INT);
            assertEquals(a.getLeft(), Semant.STRING);
        });
    }

    @Test
    public void exp_nil_used_where_record_type_can_be_determined() {
        String tigerCode = "let type rectype = {name:string, id:int} var a:= nil in a end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        var typeErrors = extractTypeMismatchError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getRight(), Semant.NIL);
        });
    }

    @Test
    public void exp_int_has_type_int() {
        String tigerCode = "let var a:int := \"s\" in a end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        var typeErrors = extractTypeMismatchError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getLeft(), Semant.INT);
            assertEquals(a.getRight(), Semant.STRING);
        });
    }

    @Test
    public void exp_string_has_type_string() {
        String tigerCode = "let var a:string:= 1 in a end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        var typeErrors = extractTypeMismatchError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getLeft(), Semant.STRING);
            assertEquals(a.getRight(), Semant.INT);
        });
    }

    @Test
    public void exp_seq_empty_type_is_void() {
        String tigerCode = "let var a:= () var b:int := 1 in (b := a) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        var typeErrors = extractTypeMismatchError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getLeft(), Semant.INT);
            assertEquals(a.getRight(), Semant.VOID);
        });
    }

    @Test
    public void exp_seq_not_empty_type_is_last_exp_type() {
        String tigerCode = "let var a:= (1;2;\"string\") var b:int := 1 in (b := a) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        var typeErrors = extractTypeMismatchError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getLeft(), Semant.INT);
            assertEquals(a.getRight(), Semant.STRING);
        });
    }

    @Test
    public void exp_neg_operand_and_result_are_int() {
        String tigerCode = "let var a:= 1 var b:= \"string\" in (b := -a) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        var typeErrors = extractTypeMismatchError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getLeft(), Semant.STRING);
            assertEquals(a.getRight(), Semant.INT);
        });
    }

    @Test
    public void exp_call_identifier_is_function() {
        String tigerCode = "let in a() end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        var typeErrors = extractFunctionNotDefinedError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getName().toString(), "a");
        });
    }

    @Test
    public void exp_call_actual_and_formals_match() {
        String tigerCode = "let function a(a:int, b:string):string = (a; b) in a(1, 2) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        var typeErrors = extractArgumentMismatchErrors(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getArgument(), Semant.INT);
            assertEquals(a.getFormal(), Semant.STRING);
        });

    }

    @Test
    public void exp_call_type_matches_function_type() {
        String tigerCode = "let function a(a:int, b:int):int= (a; b) var res:string:= \"\" in res := a(1, 2) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        var typeErrors = extractTypeMismatchError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getLeft(), Semant.STRING);
            assertEquals(a.getRight(), Semant.INT);
        });
    }

    @Test
    public void exp_infix_plus_operands_type_int() {
        String tigerCode = "let var a := 1 + \"a\" in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        var typeErrors = extractTypeMismatchError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getRight(), Semant.STRING);
        });
    }

    @Test
    public void exp_infix_plus_result_type_int() {
        String tigerCode = "let var a := 1 + 2 var b:string := \"\" in (b := a) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        var typeErrors = extractTypeMismatchError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getLeft(), Semant.STRING);
            assertEquals(a.getRight(), Semant.INT);
        });
    }

    @Test
    public void exp_infix_equals_operand_types_match() {
        assertFalse("to be implemented", true);
    }

    @Test
    public void exp_infix_equals_result_type_int() {
        assertFalse("to be implemented", true);
    }

    @Test
    public void exp_infix_comp_operand_types_match() {

        assertFalse("to be implemented", true);
    }

    @Test
    public void exp_infix_comp_result_int() {

        assertFalse("to be implemented", true);
    }

    @Test
    public void exp_arr_create_tyid_is_array_type() {
        String tigerCode = "let var a := 1 type arrtype1 = int var arr1 := arrtype1 [10] of 0 in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
    }

    @Test
    public void exp_arr_create_sq_brket_exp_type_int() {
        String tigerCode = "let var a := 1 type arrtype1 = array of int var arr1 := arrtype1 [\"a\"] of 0 in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
    }

    @Test
    public void exp_arr_create_init_exp_matches_array_type() {
        String tigerCode = "let var a := 1 type arrtype1 = array of int var arr1 := arrtype1 [10] of \"a\" in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
    }

    @Test
    public void exp_arr_create_result_type_is_array_type() {
        String tigerCode = "let type arrtype1 = array of int var arr1 := arrtype1 [10] of 10 var a:int := 0 in ( a:= arr1 ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
    }

    @Test
    public void exp_rec_create_tyid_is_record_type() {
        String tigerCode = "let type rectype = {name:string, age:int} var rec1 := int {name=\"Nobody\", age=\"Nobody\"} in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
    }

    @Test
    public void exp_rec_create_field_order_matches() {
        String tigerCode = "let type rectype = {name:string} var rec1 := rectype {age=1} in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(2, m.getErrorMsg().getCompilerErrors().size());
    }

    @Test
    public void exp_rec_create_field_name_matches() {

        assertFalse("to be implemented", true);
    }

    @Test
    public void exp_rec_create_field_type_matches() {

        assertFalse("to be implemented", true);
    }

    @Test
    public void exp_rec_create_result_type_is_record_type() {

        assertFalse("to be implemented", true);
    }

    @Test
    public void exp_assign_lvalue_exp_type_match() {

        assertFalse("to be implemented", true);
    }

    @Test
    public void exp_assign_result_type_void() {

        assertFalse("to be implemented", true);
    }

    @Test
    public void exp_ifthenelse_condition_type_int() {

        assertFalse("to be implemented", true);
    }

    @Test
    public void exp_ifthenelse_thenclause_elseclause_types_match() {

        assertFalse("to be implemented", true);
    }

    @Test
    public void exp_ifthenelse_return_type_matches_then() {
        assertFalse("to be implemented", true);
    }

    @Test
    public void exp_ifthenelse_return_type_matches_else() {
        assertFalse("to be implemented", true);
    }

    @Test
    public void exp_while_condition_type_is_int_type() {
        assertFalse("to be implemented", true);

    }

    @Test
    public void exp_while_body_type_is_void() {
        assertFalse("to be implemented", true);

    }

    @Test
    public void exp_while_result_type_is_void() {
        assertFalse("to be implemented", true);

    }

    @Test
    public void exp_for_start_index_is_int_type() {
        String tigerCode = "let var a:int := 1 in (for j := \"a\" to 10 do a := a + 1) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        var typeErrors = extractTypeMismatchError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getLeft(), Semant.STRING);
            assertEquals(a.getRight(), Semant.INT);
        });
    }

    @Test
    public void exp_for_end_index_is_int_type() {
        String tigerCode = "let var a:int := 1 in (for j := 0 to \"a\" do a := a + 1) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
        var typeErrors = extractTypeMismatchError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getLeft(), Semant.STRING);
            assertEquals(a.getRight(), Semant.INT);
        });
    }

    @Test
    public void exp_for_variable_is_not_assigned_to() {
        String tigerCode = "let var a:int := 1 in (for j := 0 to 10 do (a := a + j; j := 2)) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertEquals(1, m.getErrorMsg().getCompilerErrors().size());
    }

    @Test
    public void exp_for_body_type_is_void() {
        assertFalse("to be implemented", true);

    }

    @Test
    public void exp_for_result_type_is_void() {
        assertFalse("to be implemented", true);

    }

    @Test
    public void exp_break_only_valid_in_for() {
        String tigerCode = "let var a:int := 1 in a := a + 1;break end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertTrue(m.hasErrors());
    }

    @Test
    public void exp_break_only_valid_in_while() {
String tigerCode = "let var a:int := 1 in a := a + 1;break end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        assertTrue(m.hasErrors());
    }

    @Test
    public void exp_break_return_type_is_void() {
        String tigerCode = "break";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
    }

    @Test
    public void exp_let_empty_body_return_type_is_void() {
        String tigerCode = "let var a:int := 0 function b() = let in () end in (a := b() ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        var typeErrors = extractTypeMismatchError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getLeft(), Semant.INT);
            assertEquals(a.getRight(), Semant.VOID);
        });
    }

    @Test
    public void exp_let_body_return_type_is_last_body_exp() {
        String tigerCode = "let var a:string := \"\" function b() = let in (1) end in (a := b() ) end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.compile();
        var typeErrors = extractTypeMismatchError(m.getErrorMsg());
        typeErrors.stream().findAny().ifPresent(a -> {
            assertEquals(a.getLeft(), Semant.INT);
            assertEquals(a.getRight(), Semant.STRING);
        });

    }

    private static List<TypeMismatchError> extractTypeMismatchError(ErrorMsg errorMsg) {
        return errorMsg.getCompilerErrors().stream().filter(x -> x instanceof TypeMismatchError)
                .map(m -> (TypeMismatchError) m).collect(Collectors.toList());
    }

    private static List<FieldNotDefinedError> extractFieldNotDefinedError(ErrorMsg errorMsg) {
        return errorMsg.getCompilerErrors().stream().filter(x -> x instanceof FieldNotDefinedError)
                .map(m -> (FieldNotDefinedError) m).collect(Collectors.toList());
    }

    private static List<FunctionNotDefinedError> extractFunctionNotDefinedError(ErrorMsg errorMsg) {
        return errorMsg.getCompilerErrors().stream().filter(x -> x instanceof FunctionNotDefinedError)
                .map(m -> (FunctionNotDefinedError) m).collect(Collectors.toList());
    }

    private static List<ArgumentMismatchError> extractArgumentMismatchErrors(ErrorMsg errorMsg) {
        return errorMsg.getCompilerErrors().stream().filter(x -> x instanceof ArgumentMismatchError)
                .map(m -> (ArgumentMismatchError) m).collect(Collectors.toList());
    }

}