package Codegen;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import Temp.Label;
import Temp.Temp;
import Tree.BINOP;
import Tree.CONST;
import Tree.EmptyStm;
import Tree.Exp;
import Tree.MEM;
import Tree.MOVE;
import Tree.SEQ;
import Tree.Stm;
import Tree.StmList;
import Tree.TEMP;
import Util.BoolList;


public class TilePatternTest {

    @Test
    public void moveTempToTemp() {
        Temp t1 = Temp.create();
        Temp t2 = Temp.create();
        MOVE move = new MOVE(new TEMP(t1), new TEMP(t2)); 
        TilePatternMatcher tilePatternMatcher = new TilePatternMatcher(move);
        assertTrue(tilePatternMatcher.isMatch(TilePatterns.MOVE_TEMP_TO_TEMP));
        assertEquals(t1, ((TEMP)tilePatternMatcher.getCapture("temp1")).temp);
        assertEquals(t2, ((TEMP)tilePatternMatcher.getCapture("temp2")).temp);
    }

    @Test
    public void moveTempToTempFail() {
        MOVE move = new MOVE(new TEMP(Temp.create()), new CONST(1)); 
        TilePatternMatcher tilePatternMatcher = new TilePatternMatcher(move);
        assertFalse(tilePatternMatcher.isMatch(TilePatterns.MOVE_TEMP_TO_TEMP));
    }
}