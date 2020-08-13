package Intel;

import org.junit.Test;

import Temp.Temp;
import Tree.BINOP;
import Tree.IR;

public class MoveTest {

    @Test
    public void moveTempToTemp() throws Exception {
        IR tree = new Tree.MOVE(
            new Tree.TEMP(Temp.create()),
            new Tree.TEMP(Temp.create())
        );
        new Intel.CodeGen().burm(tree);
    }

    @Test
    public void moveConstToTemp() throws Exception {
        IR tree = new Tree.MOVE(
            new Tree.TEMP(Temp.create()),
            new Tree.CONST(3)
        );

        new Intel.CodeGen().burm(tree);
    }

    @Test
    public void moveBinopToTemp() throws Exception {
        IR tree = new Tree.MOVE(
            new Tree.TEMP(Temp.create()),
            new Tree.BINOP(
                BINOP.PLUS,
                new Tree.CONST(3),
                new Tree.CONST(3)
            )
        );
        new Intel.CodeGen().burm(tree);
    }




}