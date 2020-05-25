package E2E;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;

import Main.Main;

public class TigerTests {

    private InputStream loadFile(String name) throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(name);
        return inputStream;
    }

    /**
     * Tiger program that defines an array type 
     * and an array variable.
     * @throws FileNotFoundException
     */
    @Test
    public void example1() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./reference/tiger/testcases/test1.tig"));
        m.compile();
    }

    @Test
    public void example2() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./reference/tiger/testcases/test2.tig"));
        m.compile();
    }

    @Test
    public void example3() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./reference/tiger/testcases/test3.tig"));
        m.compile();
    }

    @Test
    public void example4() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./reference/tiger/testcases/test4.tig"));
        m.compile();
    }

    @Test
    public void example5() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./reference/tiger/testcases/test5.tig"));
        m.compile();
    }

    @Test
    public void example6() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./reference/tiger/testcases/test6.tig"));
        m.compile();
    }

    @Test
    public void example10() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./reference/tiger/testcases/test10.tig"));
        m.compile();
        //should be an error
    }


    @Test
    public void example36() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./reference/tiger/testcases/test36.tig"));
        m.compile();
        //should be an error
    }

    @Test
    public void example41() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./reference/tiger/testcases/test41.tig"));
        m.compile();
        //should be an error
    }

    @Test
    public void assign() throws FileNotFoundException {
        Main m = new Main("chap5", loadFile("./reference/tests/assign.tig"));
        m.compile();
    }
}