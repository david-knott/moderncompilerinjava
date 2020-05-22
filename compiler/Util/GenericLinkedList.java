package Util;

public class GenericLinkedList<T> {

    public T head;
    public GenericLinkedList<T> tail;

    public GenericLinkedList(T head, GenericLinkedList<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    public GenericLinkedList<T> append(T head) {
        var me = this;
        var list = new GenericLinkedList<T>(head, null);
        while (me.tail != null) {
            me = me.tail;
            list = new GenericLinkedList<T>(me.head, list);
        }
        return list;
    }

    public GenericLinkedList<T> reverse() {
        return null;
    }

    public GenericLinkedList<T> prepend() {
        return null;
    }

    public T last() {
        return null;
    }

    public T first() {  
        return null;
    }
}