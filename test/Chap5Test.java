package test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import Parse.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Chap5Test {

    @Test
    public void test() {
        String tigerCode = "let type rectype = {name:int, age:int} type t2 = {name: int} in () end";
        InputStream inputStream = new ByteArrayInputStream(tigerCode.getBytes(Charset.forName("UTF-8")));
        new Parse("chap5", inputStream);
    }
}