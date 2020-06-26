package Codegen;

import java.util.Hashtable;

import javax.xml.catalog.Catalog;

import Tree.BINOP;
import Tree.CALL;
import Tree.CONST;
import Tree.EXP;
import Tree.Exp;
import Tree.MEM;
import Tree.MOVE;
import Tree.TEMP;

public class TilePatterns {

    //for testing only.
    public static TilePattern MOVE_TEMP_TO_TEMP = new MOVET(
        new TEMPT("dst"),
        new TEMPT("src")
    );

    public static TilePattern LOAD = new MOVET(
        new ExpT("dst"),
        new MEMT(
            new ExpT("src")
        )
    );

    public static TilePattern LOAD_2 = new MOVET(
        new ExpT("dst"),
        new MEMT(
            new BINOPT(
                BINOP.PLUS, 
                new CONSTT("offset"), 
                new ExpT("src"))
        )
    );

    public static TilePattern LOAD_3 = new MOVET(
        new ExpT("dst"),
        new MEMT(
            new BINOPT(
                BINOP.PLUS, 
                new ExpT("src"),
                new CONSTT("offset")
            )
        )
    );

    public static TilePattern STORE = new MOVET(
        new MEMT(
            new ExpT("dst")
        ),
        new ExpT("src")
    );

    public static TilePattern STORE_2 = new MOVET(
        new MEMT(
            new BINOPT(
                BINOP.PLUS, 
                new CONSTT("offset"),
                new ExpT("dst")
            )
        ),
        new ExpT("src")
    );

    public static TilePattern STORE_3 = new MOVET(
        new MEMT(
            new BINOPT(
                BINOP.PLUS, 
                new ExpT("dst"),
                new CONSTT("offset")
            )
        ),
        new ExpT("src")
    );


    public static TilePattern STORE_ARRAY = new MOVET(
        new MEMT(
            new BINOPT(
                BINOP.PLUS,
                new ExpT("base"), //base address exp
                new BINOPT(
                    BINOP.MUL, 
                    new ExpT("index"), //index exp
                    new CONSTT("wordSize") //word size
                )
            )
        ),
        new ExpT("src")
    );

    public static TilePattern LOAD_ARRAY = new MOVET(
        new ExpT("dst"),
        new MEMT(
            new BINOPT(
                BINOP.PLUS,
                new ExpT("base"), //base address exp
                new BINOPT(
                    BINOP.MUL, 
                    new ExpT("index"), //index exp
                    new CONSTT("wordSize") //word size
                )
            )
        )
    );

    public static TilePattern MOVE_CALL = new MOVET(
        new ExpT("dst"),
        new CALLT("call")
    );

    public static TilePattern EXP_CALL = new EXPT(
        new CALLT("call")
    );

}

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
            if (binop.binop == binopt.op) {
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

interface TilePatternVisitor {

    void visit(CONSTT constt);

    void visit(MOVET movet);

    void visit(MEMT memt);

    void visit(BINOPT binopt);

    void visit(TEMPT tempt);

    void visit(CALLT callt);

    void visit(ExpT expT);

    void visit(EXPT exptt); //arghh !!!

}

abstract class TilePattern {

    public String name;

    abstract void accept(TilePatternVisitor patternVisitor);
}

abstract class StatementT extends TilePattern {
}

class ExpT extends TilePattern {

    public ExpT() {
        this.name = null;
    }

    public ExpT(String name) {
        this.name = name;
    }

    @Override
    void accept(TilePatternVisitor patternVisitor) {
        patternVisitor.visit(this);
    }
}

class MEMT extends ExpT {

    public ExpT expT;

    public MEMT(ExpT expT) {
        this.expT = expT;
    }

    public void accept(TilePatternVisitor patternVisitor) {

        patternVisitor.visit(this);
    }
}

class MOVET extends StatementT {
    public ExpT dst;
    public ExpT src;

    public MOVET(ExpT dst, ExpT src) {
        this.dst = dst;
        this.src = src;
    }

    public void accept(TilePatternVisitor patternVisitor) {
        patternVisitor.visit(this);
    }
}

class EXPT extends StatementT {

    public ExpT expT;

    public EXPT(ExpT expT) {
        this.expT = expT;
    }

    public void accept(TilePatternVisitor patternVisitor) {

        patternVisitor.visit(this);
    }
}

class BINOPT extends ExpT {
    public int op;
    public ExpT left;
    public ExpT right;

    public BINOPT(int op, ExpT left, ExpT right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public void accept(TilePatternVisitor patternVisitor) {

        patternVisitor.visit(this);
    }
}

class CONSTT extends ExpT {

    public CONSTT(String name) {
        super(name);
    }

    public void accept(TilePatternVisitor patternVisitor) {

        patternVisitor.visit(this);
    }
}

class TEMPT extends ExpT {

    public TEMPT(String name) {
        super(name);
    }

    public void accept(TilePatternVisitor patternVisitor) {

        patternVisitor.visit(this);
    }
}

class CALLT extends ExpT {

    public CALLT(String name) {
        super(name);
    }

    public void accept(TilePatternVisitor patternVisitor) {
        patternVisitor.visit(this);
    }
}