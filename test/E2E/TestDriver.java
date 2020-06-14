package E2E;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;

import org.junit.Test;

import Main.Main;

public class TestDriver {
    @Test
    public void test() throws FileNotFoundException {
        Main.main(new String[]{"./test/E2E/tests/function_seven_arg.tig"});
    }

}