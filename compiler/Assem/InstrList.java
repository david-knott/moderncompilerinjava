package Assem;

public class InstrList {
    public Instr head;
    public InstrList tail;

    public InstrList(Instr h, InstrList t) {
        head = h;
        tail = t;
    }

    public InstrList reverse() {
        InstrList reversed = null;
        for (InstrList il = this; il != null; il = il.tail) {
            if (reversed == null) {
                reversed = new InstrList(il.head, null);
            } else {
                reversed = new InstrList(il.head, reversed);
            }
        }
        return reversed;
    }

    public InstrList append(Instr instr) {
        InstrList end = this;
        for(; end.tail != null; end = end.tail);
        end.tail = new InstrList(instr, null);
        return this;
    }
}
