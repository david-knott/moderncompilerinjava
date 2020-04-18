package Util;


//enum NodeWorkList { PRECOLOURED, INITIAL, SIMPLIFY, FREEZE, SPILL, SPILLED, COALESCED, COLOURED};
//enum MoveWorkList { COALESCED, CONSTRAINED, FROZEN, WORKLIST, ACTIVE};

/**
 * Double linked list implementation of a sequence
 */
public class DoubleLinkedList<T> {

    //TODO - these should be private
    Item HEADER = new Item();
    Item TRAILER = new Item();

    class Item {
        T t;
        //TODO - these should be private
        Item next;
        Item prev;

        private Item() {}

        public Item(T t, Item prev, Item next) {
            this.prev = prev;
            this.next = next;
            this.t = t;
        }
    }

    public DoubleLinkedList() {
        HEADER.next = TRAILER;
        TRAILER.prev = HEADER;
    }

    public Item first() {
        return HEADER.next;
    }

    public void addToStart(T t) {
        Item i = new Item(t, HEADER, HEADER.next);
        HEADER.next = i;
    }

    public void addToEnd(T t) {
        Item i = new Item(t, TRAILER, TRAILER.prev);
        TRAILER.prev = i;
    }
    
    public T removeFromStart() {
        return null;
    }

    public T removeFromEnd() {
        return null;
    }
}