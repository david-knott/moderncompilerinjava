package Codegen;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import Core.LL;
import Tile.Match;
import Tile.MatchThing;
import Tile.Rule;
import Tile.RuleManager;
import Tile.TilePatternMatcher;
import Tree.BINOP;
import Tree.CALL;
import Tree.CJUMP;
import Tree.CONST;
import Tree.ESEQ;
import Tree.EXP;
import Tree.Exp;
import Tree.ExpList;
import Tree.IR;
import Tree.JUMP;
import Tree.LABEL;
import Tree.MEM;
import Tree.MOVE;
import Tree.NAME;
import Tree.SEQ;
import Tree.TEMP;
import Tree.TreeVisitor;

class TileCost implements TreeVisitor {

    private RuleManager ruleManager = RuleManager.instance;

    private void calculate(IR op) {
        int minCost = -1;
        for(MatchThing matchThing : this.matches(op)) {
            int cost = this.cost(op, matchThing);
            if(minCost == -1 || cost < minCost) {
                minCost = cost;
                set(op, matchThing); //sets the minimum cost tile for this node.
            }
        }
    }

    private List<MatchThing> matches(IR exp) {
        //for all the rules in the rule list find what matches this exp.
        TilePatternMatcher tilePatternMatcher = new TilePatternMatcher(exp);
        List<MatchThing> matchThings = new ArrayList<MatchThing>();
        for(LL<Rule> rules = ruleManager.rules; rules != null; rules = rules.tail) {
            if(tilePatternMatcher.isMatch(rules.head.tilePattern)) {
                Match match = new Match(tilePatternMatcher.exps());
                matchThings.add(new MatchThing(rules.head, match, 0));
            }
        }
        return matchThings;
    }

    private int cost(IR exp, MatchThing matchThing) {
        int cost = matchThing.rule.cost;
        for(ExpList leafs = matchThing.match.expList; leafs != null; leafs = leafs.tail) {
            cost = cost + get(leafs.head).rule.cost;
        }
        return cost;
    }

    private Hashtable<IR, MatchThing> matchThings = new Hashtable<IR, MatchThing>();
    
    private void set(IR op, MatchThing matchThing) {
        this.matchThings.put(op, matchThing);
        System.out.println("adding match " + op + " " + matchThing);
    }

    private MatchThing get(IR op) {
        return this.matchThings.get(op);
    }

    @Override
    public void visit(BINOP op) {
        op.left.accept(this);
        op.right.accept(this);
        this.calculate(op);
    }

    @Override
    public void visit(CALL op) {
        op.func.accept(this);
        this.calculate(op);
        for(ExpList el = op.args; el != null; el = el.tail) {
            el.head.accept(this);
            this.calculate(el.head);
        }
    }

    @Override
    public void visit(CONST op) {
        this.calculate(op);
    }

    @Override
    public void visit(ESEQ op) {
        throw new Error("Not handled");
    }

    @Override
    public void visit(EXP op) {
        op.exp.accept(this);
        this.calculate(op);
    }

    @Override
    public void visit(JUMP op) {
        this.calculate(op);
    }

    @Override
    public void visit(LABEL op) {
        this.calculate(op);
    }

   

    @Override
    public void visit(MEM op) {
        op.exp.accept(this);
        this.calculate(op);
    }

    @Override
    public void visit(MOVE op) {
        op.dst.accept(this);
        op.src.accept(this);
        this.calculate(op);
    }

    @Override
    public void visit(NAME op) {
        this.calculate(op);
    }

    @Override
    public void visit(SEQ op) {
        op.left.accept(this);
        op.right.accept(this);
    }

    @Override
    public void visit(TEMP op) {
        this.calculate(op);
    }

    @Override
    public void visit(CJUMP cjump) {
        cjump.left.accept(this);
        cjump.right.accept(this);
        this.calculate(cjump);
    }
}