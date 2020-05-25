package E2E;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import Main.Main;
import Temp.Label;
import Temp.Temp;
import Tree.BINOP;
import Tree.CONST;
import Tree.EmptyStm;
import Tree.MEM;
import Tree.MOVE;
import Tree.SEQ;
import Tree.Stm;
import Tree.StmList;
import Tree.TEMP;
import Util.BoolList;

public class TigerTests {

    private InputStream loadFile(String name) throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(name);
        return inputStream;
    }

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
}