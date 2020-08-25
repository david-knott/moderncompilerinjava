package E2E;

import java.io.FileNotFoundException;

import org.junit.Test;

import Main.Main;

public class TestDriver {
    @Test
    public void intDec() throws FileNotFoundException {
        Main.main(new String[]{"reg-alloc", "escapes-compute", "./test/E2E/good/function_six_arg.tig"});
    }

    @Test
    public void test() throws FileNotFoundException {
        Main.main(new String[]{"./test/E2E/good/linked_list_size.tig"});
    }

    @Test
    public void test4() throws FileNotFoundException {
        Main.main(new String[]{"./test/E2E/good/array_dec.tig"});
    }
         static boolean testing(boolean[] bools) {
         boolean result = true;
         for(int i = 0; i < bools.length; i++) {
             result &= bools[i];
         }
         return result;
     }

     @Test
    public void testingd() {
        System.out.println("" + testing(new boolean[]{false, false, true}));
    
    }
}