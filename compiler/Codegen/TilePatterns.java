package Codegen;

import Tree.BINOP;

public class TilePatterns {

    //for testing only.
    public static TilePattern MOVE_TEMP_TO_TEMP = new MOVET(
        new TEMPT("dst"),
        new TEMPT("src")
    );

    public static TilePattern MOVE_MEM_TO_MEM = new MOVET(
        new MEMT(
            new ExpT("dst")
        ),
        new MEMT(
            new ExpT("src")
        )
    );

    public static TilePattern LOAD_0 = new MOVET(
        new ExpT("dst"),
        new ExpT("src")
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

    public static TilePattern BINOP_1 = new BINOPT(
        -1,
        new ExpT("left"),
        new MEMT(
            new BINOPT(
                BINOP.PLUS, 
                new CONSTT("offset"), 
                new ExpT("right"))
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