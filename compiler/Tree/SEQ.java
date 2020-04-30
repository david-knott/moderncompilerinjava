package Tree;

import java.util.ArrayList;
import java.util.List;

public class SEQ extends Stm {
    public Stm left, right;

    public SEQ(Stm l, Stm r) {
        left = l;
        right = r;
    }

    public ExpList kids() {
        throw new Error("kids() not applicable to SEQ");
    }

    public Stm build(ExpList kids) {
        throw new Error("build() not applicable to SEQ");
    }

    @Override
    public void accept(TreeVisitor treeVisitor) {
        treeVisitor.visit(this);
    }

    private class Normalizer {

        public Stm result;

        public Normalizer(SEQ seq) {
            tranverse(seq);
        }

        public void addOrCreate(Stm smt) {
            if(smt == null) return;
            if (result == null) {
                result = smt;
            } else {
                result = new SEQ(smt, result);
            }
        }

        public void tranverse(SEQ seq) {
            if (seq.right instanceof SEQ) {
                this.tranverse(((SEQ) seq.right));
            } else {
                this.addOrCreate(seq.right);
            }
            if (seq.left instanceof SEQ) {
                this.tranverse(((SEQ) seq.left));
            } else {
                this.addOrCreate(seq.left);
            }

        }
    }

    /**
     * Returns a normalized version of this SEQ in the form s (1, s(2, s(3, s(4,
     * s(5, 6))))) This is generated from a list of the terminal nodes, listed from
     * left to right.
     * 
     * @return
     */
    public SEQ normalise() {
        return (SEQ) (new Normalizer(this).result);
    }
}
