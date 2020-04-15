package RegAlloc;

import RegAlloc.DoubleLinkedList.Item;

/**
 * Double linked list implementation of a sequence
 */
class DoubleLinkedList<T> {

    Item HEADER = new Item();
    Item TRAILER = new Item();

    class Item {
        T t;
        Item next;
        Item prev;

        private Item() {
        }

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

    Item first() {
        return HEADER.next;
    }

    void addToStart(T t) {
        Item i = new Item(t, HEADER, HEADER.next);
        HEADER.next = i;
    }

    void addToEnd(T t) {
        Item i = new Item(t, TRAILER, TRAILER.prev);
        TRAILER.prev = i;
    }
    
    T removeFromStart() {
        return null;
    }

    T removeFromEnd() {
        return null;
    }
}