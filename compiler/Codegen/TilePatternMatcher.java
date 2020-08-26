package Codegen;

import java.util.Hashtable;

import Tree.BINOP;
import Tree.CALL;
import Tree.CONST;
import Tree.EXP;
import Tree.Exp;
import Tree.ExpList;
import Tree.MEM;
import Tree.MOVE;
import Tree.TEMP;

class TilePatternMatcher implements TilePatternVisitor {
    private Object exp;
    private Object originalRef;
    private Hashtable<String, Object> captures = new Hashtable<String, Object>();
    private boolean matches = true;

    public TilePatternMatcher(Object exp) { /* TODO: is this a bad idea ? */
        this.originalRef = exp;
    }

    /**
     * Returns the captured item and removes it from the internal hashtable.
     * If item is not present an exception is thrown/
     * @param key the key
     * @return the captured object.
     */
    public Object getCapture(String key) {
        if(this.captures.containsKey(key)) {
            return this.captures.remove(key);
        } else {
            throw new Error("Key " + key + " does not exist as capture");
        }
    }


    public boolean isMatch(TilePattern tilePattern) {
        this.exp = this.originalRef;
        this.matches = true;
        tilePattern.accept(this);
        return this.matches;
    }

    @Override
    public void visit(CONSTT constt) {
        if (exp instanceof CONST) {
            CONST cnt = (CONST) exp;
            captures.put(constt.name, cnt.value);
            return;
        }
        this.matches = false;
    }

    @Override
    public void visit(MEMT memt) {
        if (this.exp instanceof MEM) {
            MEM mem = (MEM) this.exp;
            this.exp = mem.exp;
            memt.expT.accept(this);
            return;
        }
       this.matches = false;
    }

    @Override
    public void visit(BINOPT binopt) {
        if (this.exp instanceof BINOP) {
            BINOP binop = (BINOP) this.exp;
            if (binopt.op == -1 || binop.binop == binopt.op) {
                this.exp = binop.left;
                binopt.left.accept(this);
                this.exp = binop.right;
                binopt.right.accept(this);
                return;
            }
        }
        this.matches = false;
    }

    @Override
    public void visit(TEMPT tempt) {
        if (this.exp instanceof TEMP) {
            TEMP temp = (TEMP)this.exp;
            this.captures.put(tempt.name, temp);
            return;
        }
        this.matches = false;
    }

    @Override
    public void visit(CALLT callt) {
        if (this.exp instanceof CALL) {
            CALL call = (CALL)this.exp;
            this.captures.put(callt.name, call);
            return;
        }
        this.matches = false;
    }

    @Override
    public void visit(MOVET movet) {
        if(this.exp instanceof MOVE) {
            MOVE move = (MOVE)this.exp;
            this.exp = move.dst;
            movet.dst.accept(this);
            this.exp = move.src;
            movet.src.accept(this);
            return;
        }
        this.matches = false;
    }

    @Override
    public void visit(ExpT expT) {
        if (this.exp instanceof Exp) {
            this.captures.put(expT.name, (Exp) (this.exp));
            return;
        }
        this.matches = false;
    }

    @Override
    public void visit(EXPT exptt) {
        if(this.exp instanceof EXP) {
            EXP e = (EXP)this.exp;
            this.exp = e.exp;
            exptt.expT.accept(this);
            return;
        }
        this.matches = false;
    }
}