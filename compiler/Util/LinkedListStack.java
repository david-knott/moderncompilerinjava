package Util;

/**
 * Single linked list implementation of a stack
 * @param <T>
 */
public class LinkedListStack<T> {
    
    private Item head;
    private class Item {
        T t;
        Item tail;

        public Item(T t, Item tail) {
            this.tail = tail;
            this.t = t;
        }
    }

    public boolean isEmpty() {
        return this.head == null;
    }

    public T pop() {
        if(this.isEmpty()) {
            throw new Error("Stack is empty");
        }
        Item last = this.head;
        this.head = this.head.tail;
        return last.t;
    }

    public void push(T t) {
        if(head == null) {
            head = new Item(t, null);
        } else {
            head = new Item(t, head);
        }
    }
}