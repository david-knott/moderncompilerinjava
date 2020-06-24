package E2E;

import java.io.FileNotFoundException;

import org.junit.Test;

import Main.Main;

public class TestDriver {
    @Test
    public void test() throws FileNotFoundException {
        Main.main(new String[]{"./test/E2E/good/for_function_in_loop.tig"});
    }

    @Test
    public void forAssign() throws FileNotFoundException {
        Main.main(new String[]{"./test/E2E/good/for_assign_indexer.tig"});
    }


    @Test
    public void test2() throws FileNotFoundException {
        Main.main(new String[]{"./test/E2E/good/string_substr.tig"});
    }
}