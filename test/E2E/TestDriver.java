package E2E;

import java.io.FileNotFoundException;

import org.junit.Test;

import Main.Main;

public class TestDriver {
    @Test
    public void test() throws FileNotFoundException {
        Main.main(new String[]{"./test/E2E/tests/for_function_in_loop.tig"});
    }

    @Test
    public void test2() throws FileNotFoundException {
        Main.main(new String[]{"./test/E2E/tests/while_function_in_loop.tig"});
    }

    @Test
    public void test3() throws FileNotFoundException {
        Main.main(new String[]{"./test/E2E/tests/function_two_arg_cc.tig"});
    }
}