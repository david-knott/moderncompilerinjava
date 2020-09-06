package E2E;

import java.io.FileNotFoundException;

import org.junit.Test;

import Main.Main;

public class TestDriver {
    @Test
    public void intDec() throws FileNotFoundException {
        Main.main(new String[]{"--bind", "./test/E2E/good/pplus.tig"});
    }

    @Test
    public void test() throws FileNotFoundException {
        Main.main(new String[]{"--bind", "./test/E2E/good/linked_list_size.tig"});
    }

    @Test
    public void test4() throws FileNotFoundException {
        Main.main(new String[]{"--temp-expand", "./test/E2E/good/array_dec.tig"});
    }
}