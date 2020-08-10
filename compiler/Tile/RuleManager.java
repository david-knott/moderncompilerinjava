package Tile;

import Assem.Instr;
import Core.LL;

/**
 * This class manages a list list of rules used to generate instructions.
 */
public class RuleManager {

    public static RuleManager instance = new RuleManager();
    public LL<Rule> rules;

    /**
     * Adds a new rule to the collection of rules in the rule manager. The rule
     * is inserted at the top the list. During instruction select these rules
     * are used to determine which instruction to use for a given tile pattern.
     * @param tilePattern
     * @param cost
     * @param action
     */
    public void addRule(TilePattern tilePattern, int cost, Action action) {
        this.rules = LL.<Rule>insertRear(this.rules, new Rule(tilePattern, cost, action));
    }

    static void emit(Instr instr) {
    }

    static {
        instance.addRule(
            new MOVET(
                new ExpT("dst"),
                new ExpT("src")
            ), 
            1, 
            tilePatternMatcher -> {
                /*
                var dst = (Temp)tilePatternMatcher.getCapture("dest");
                var src = (Temp)tilePatternMatcher.getCapture("src");
                emit(new Assem.MOVE("movq %`s0, %`d0 # load 0", dst, src));*/
                return null; //Temp.create();
            }
        );
        instance.addRule(
            new MEMT(
                new ExpT("exp")
            ), 
            1, 
            tilePatternMatcher -> {
                /*
                var dst = (Temp)tilePatternMatcher.getCapture("dest");
                var src = (Temp)tilePatternMatcher.getCapture("src");
                emit(new Assem.MOVE("movq %`s0, %`d0 # load 0", dst, src));*/
                return null; //Temp.create();
            }
        );
        instance.addRule(
            new CALLT(
                ""
            ), 
            1, 
            tilePatternMatcher -> {

                /*
                var dst = (Temp)tilePatternMatcher.getCapture("dest");
                var src = (Temp)tilePatternMatcher.getCapture("src");
                emit(new Assem.MOVE("movq %`s0, %`d0 # load 0", dst, src));*/
                return null; //Temp.create();
            }
        );




    }
}