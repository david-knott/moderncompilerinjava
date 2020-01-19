package Tree;

public class SEQ extends Stm {
    public Stm left, right;

    public SEQ(Stm l, Stm r) {
        left = l;
        right = r;
    }

    /**
     * Creates a new version of this SEQ
     * @param stm
     * @return
     */
    public SEQ append(Stm stm){
        //go to right most item that is a SEQ element

        //add to it

        //if right most element is not a SEQ, convert it to a SEQ

        return null;

    }

    public ExpList kids() {
        throw new Error("kids() not applicable to SEQ");
    }

    public Stm build(ExpList kids) {
        throw new Error("build() not applicable to SEQ");
    }
}
