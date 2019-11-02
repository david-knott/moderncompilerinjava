package test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import Parse.*;

public class Chap5Test {

    @Test
    public void test() {
        String filename = "./test/fixtures/chap5.tig";
        new Parse(filename);
    }
}