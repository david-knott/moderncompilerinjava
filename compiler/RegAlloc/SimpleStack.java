package RegAlloc;

/**
 * Single linked list implementation of a stack
 * @param <T>
 */
class SimpleStack<T> {
    
    private Item head;
    private class Item {
        T t;
        Item tail;

        public Item(T t, Item tail) {
            this.tail = tail;
            this.t = t;
        }
    }

    boolean isEmpty() {
        return this.head == null;
    }

    T pop() {
        if(this.isEmpty()) {
            throw new Error("Stack is empty");
        }
        Item last = this.head;
        this.head = this.head.tail;
        return last.t;
    }

    void push(T t) {
        if(head == null) {
            head = new Item(t, null);
        } else {
            head = new Item(t, head);
        }
    }
}