package E2E;

import java.io.FileNotFoundException;

import org.junit.Test;

import Main.Main;

public class TestDriver {
    @Test
    public void intDec() throws FileNotFoundException {
        Main.main(new String[]{"-A", "--clone", "./test/E2E/good/merge_simple.tig"});
        //Main.main(new String[]{"-A", "./test/E2E/good/merge_simple.tig"});
    }

    @Test
    public void test() throws FileNotFoundException {
        Main.main(new String[]{"-AB", "./test/E2E/good/linked_list_size.tig"});
    }

    @Test
    public void test4() throws FileNotFoundException {
        Main.main(new String[]{"--lir-simplify-display", "./test/E2E/good/queens.tig"});
    }
}