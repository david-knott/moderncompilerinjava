package test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.junit.Assert.*;
import Main.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Chap5Test {

    /*
    @Test
    public void type_var_dec_correct_int() {
        String tigerCode = "let var badVariable:int := 123 in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        new Parse("chap5", inputStream);
    }

    @Test
    public void type_var_dec_correct_string() {
        String tigerCode = "let var badVariable:string := \"string\" in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        new Parse("chap5", inputStream);
    }

    @Test
    public void type_var_dec_incorrect_int() {
        String tigerCode = "let var badVariable:int := \"string\" in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        new Parse("chap5", inputStream);
    }*/

    @Test
    public void type_var_dec_custom_type() {
        String tigerCode = "let type t1 = int var v1:t1 := 1 in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        Main m = new Main("chap5", inputStream);
        m.buildAst();
        m.typeCheck();
        assertFalse(m.hasErrors());
    }
}