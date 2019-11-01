package test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import Parse.*;

public class Chap3PrecedenceTest {

    @Test
    public void associativeTest() {
        String filename = "./test/fixtures/associative.tig";
        new Parse(filename);
    }
}