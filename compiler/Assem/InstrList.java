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

    public void append(Instr instr) {
        InstrList end = this;
        for (; end.tail != null; end = end.tail)
            ;
        end.tail = new InstrList(instr, null);
    }

    public void append(InstrList instrList) {
        for (; instrList != null; instrList = instrList.tail) {
            this.append(instrList.head);
        }
    }

    public int size() {
        int i = 0;
        InstrList end = this;
        for (; end != null; end = end.tail)
            i++;
        return i;

    }

    public void insertAfter(InstrList prev, Instr memoryToTemp) {
        // insert at start of list
        if (prev == null) {
            throw new Error("Insert at start of list not supported");
        } else {
            var old = prev.tail;
            prev.tail = new InstrList(memoryToTemp, old);
        }
    }
}
