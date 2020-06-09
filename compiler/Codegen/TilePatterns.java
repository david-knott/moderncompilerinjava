package Codegen;

import java.util.Hashtable;

import Tree.BINOP;
import Tree.CALL;
import Tree.CONST;
import Tree.EXP;
import Tree.Exp;
import Tree.MEM;
import Tree.MOVE;
import Tree.TEMP;

public class TilePatterns {
    public static TilePattern MOVE_OFFSET_MEM_TO_TEMP_1 = new MOVET(new TEMPT("temp1"),
            new MEMT(new BINOPT(1 /* add */, new CONSTT("const1"), new TEMPT("temp2"))));
    public static TilePattern MOVE_OFFSET_MEM_EXP_TO_TEMP = new MOVET(new TEMPT("temp1"),
            new MEMT(new BINOPT(1 /* add */, new CONSTT("const1"), new ExpT("exp"))));
    public static TilePattern MOVE_OFFSET_MEM_TO_TEMP_2 = new MOVET(new TEMPT("temp1"),
            new MEMT(new BINOPT(1 /* add */, new TEMPT("temp2"), new CONSTT("const1"))));
    public static TilePattern MOVE_TEMP_TO_OFFSET_MEM_1 = new MOVET(
            new MEMT(new BINOPT(1 /* add */, new CONSTT("const1"), new TEMPT("temp2"))), new TEMPT("temp1"));
    public static TilePattern MOVE_TEMP_TO_OFFSET_MEM_2 = new MOVET(
            new MEMT(new BINOPT(1 /* add */, new TEMPT("temp2"), new CONSTT("const1"))), new TEMPT("temp1"));
    public static TilePattern MOVE_TEMP_TO_MEM = new MOVET(new MEMT(new TEMPT("temp2")), new TEMPT("temp1"));

    //for testing only.
    public static TilePattern MOVE_TEMP_TO_TEMP = new MOVET(
        new TEMPT("dst"),
        new TEMPT("src")
    );

    public static TilePattern MOVE_CONST_TO_ARRAY_INDEX = new MOVET(
        new MEMT(
            new BINOPT(
                BINOP.PLUS,
                new TEMPT("temp1"), //base address
                new BINOPT(
                    BINOP.MUL, 
                    new CONSTT("const1"), //index
                    new CONSTT("const2") //word size
                )
            )
        ),
        new CONSTT("const3")
    );

    public static TilePattern MOVE_TEMP_TO_ARRAY_INDEX_EXP = new MOVET(
        new MEMT(
            new BINOPT(
                BINOP.PLUS,
                new ExpT("base"), //base address
                new BINOPT(
                    BINOP.MUL, 
                    new ExpT("index"), //index
                    new CONSTT("wordSize") //word size
                )
            )
        ),
        new ExpT("value")
    );

    public static TilePattern MOVE_ARRAY_INDEX_EXP_TO_TEMP = new MOVET(
        new ExpT("temp"),
        new MEMT(
            new BINOPT(
                BINOP.PLUS,
                new ExpT("base"), //base address
                new BINOPT(
                    BINOP.MUL, 
                    new ExpT("index"), //index
                    new CONSTT("wordSize") //word size
                )
            )
        )
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

    public Object getCapture(String key) {
        return this.captures.get(key);
    }

    public boolean isMatch(TilePattern tilePattern) {
        this.exp = this.originalRef;
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