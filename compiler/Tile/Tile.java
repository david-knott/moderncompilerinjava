package Tile;

interface TilePatternVisitor {

    void visit(CONSTT constt);

    void visit(MOVET movet);

    void visit(MEMT memt);

    void visit(BINOPT binopt);

    void visit(TEMPT tempt);

    void visit(CALLT callt);

    void visit(ExpT expT);

    void visit(EXPT exptt); // arghh !!!

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

public class Tile {

}