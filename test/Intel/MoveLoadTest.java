package Intel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import Temp.Label;
import Temp.Temp;
import Tree.BINOP;
import Tree.CONST;
import Tree.EmptyStm;
import Tree.IR;
import Tree.MEM;
import Tree.MOVE;
import Tree.SEQ;
import Tree.Stm;
import Tree.StmList;
import Tree.TEMP;
import Util.BoolList;

public class MoveLoadTest {

    @Test
    public void moveMemToTemp() throws Exception {
        IR tree = new Tree.MOVE(
            new Tree.TEMP(Temp.create()),
            new MEM(
                new Tree.TEMP(Temp.create())
            )
        );
        new Intel.CodeGen().burm(tree);
    }
}