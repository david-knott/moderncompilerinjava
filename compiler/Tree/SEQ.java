package Tree;

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

    private SEQ normalise(Stm s1, Stm s2) {
        if((s1 instanceof SEQ) && !(s2 instanceof SEQ)) {
            SEQ s = (SEQ)s1;
            return new SEQ(normalise(s.left, s.right), s2);
        }
        else if(!(s1 instanceof SEQ) && (s2 instanceof SEQ)) {
            SEQ s = (SEQ)s2;
            return new SEQ(s1, normalise(s.left, s.right));
        }
        else if((s1 instanceof SEQ) && (s2 instanceof SEQ)) {
            SEQ sa = (SEQ)s1;
            SEQ sb = (SEQ)s2;
            return normalise(sa.left, normalise(sa.right, normalise(sb.left, sb.right))); // normalise(sb.left, sb.right));
        }
        else {
            return new SEQ(s1, s2);
        }
    }

    public SEQ normalise() {
        Stm left = this.left;
        Stm right = this.right;
        return this.normalise(left, right);
    }
}
