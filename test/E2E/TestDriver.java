package E2E;

import java.io.FileNotFoundException;

import org.junit.Test;

import Main.Main;

public class TestDriver {


    @Test
    public void stringEquals() throws FileNotFoundException {

        //String[] args = new String[] {"--reg-alloc", "--lir-display", "--escapes-compute", "--demove", "./test/E2E/good/tbi.tig"};
        String[] args = new String[] {"--lir-display", "--escapes-compute", "./test/E2E/good/string_equals.tig"};
        Main.main(args);
        //Main.main(new String[]{"-A", "./test/E2E/good/merge_simple.tig"});
    }


    @Test
    public void functionOne() throws FileNotFoundException {

        //String[] args = new String[] {"--reg-alloc", "--lir-display", "--escapes-compute", "--demove", "./test/E2E/good/tbi.tig"};
        String[] args = new String[] {"--lir-display", "--escapes-compute", "./test/E2E/good/function_one_arg.tig"};
        Main.main(args);
        //Main.main(new String[]{"-A", "./test/E2E/good/merge_simple.tig"});
    }

    @Test
    public void binSearch() throws FileNotFoundException {

        //String[] args = new String[] {"--reg-alloc", "--lir-display", "--escapes-compute", "--demove", "./test/E2E/good/tbi.tig"};
        String[] args = new String[] {"--hir-display", "--escapes-compute", "./test/E2E/good/binsearch.tig"};
        Main.main(args);
        //Main.main(new String[]{"-A", "./test/E2E/good/merge_simple.tig"});
    }

    @Test
    public void prime() throws FileNotFoundException {
        String[] args = new String[] {"--hir-display", "--escapes-compute", "./test/E2E/good/prime.tig"};
        Main.main(args);
    }


    @Test
    public void dec2Bin() throws FileNotFoundException {
        String[] args = new String[] {"--hir-display", "--escapes-compute", "./test/E2E/good/dec2bin.tig"};
        Main.main(args);
    }


    @Test
    public void linkedListSize() throws FileNotFoundException {
        Main.main(new String[]{"-AB", "./test/E2E/good/linked_list_size.tig"});
    }

    @Test
    public void test4() throws FileNotFoundException {
        Main.main(new String[]{"--lir-simplify-display", "./test/E2E/good/queens.tig"});
    }
}