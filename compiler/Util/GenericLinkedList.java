package Util;

public class GenericLinkedList<T> {

    public final T head;
    public final GenericLinkedList<T> tail;

    public GenericLinkedList(T head, GenericLinkedList<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    public GenericLinkedList(T head) {
        this.head = head;
        this.tail = null;
    }

    

    /**
     * Check if item n is contained in this linked list. This method runs in O(n)
     * time in the worst case.
     * 
     * @param n
     * @return
     */
    public boolean contains(T n) {
        for (GenericLinkedList<T> s = this; s != null; s = s.tail) {
            if (s.head == n) {
                return true;
            }
        }
        return false;
    }

    /**
     * Difference between two sets
     * @param other
     * @return
     */
    public GenericLinkedList<T> difference(GenericLinkedList<T> other) {
        return null;
    }

    private GenericLinkedList<T> addCreate(GenericLinkedList<T> list, T item) {
        if(list == null) {
            return new GenericLinkedList<T>(item);
        } else {
            return list.append(item);
        }
    }

    /**
     * Returns a new linked list that is the union of this linked list and them
     * passed in as a parameter. This method assumes both linked lists are sorted
     * using the same ordering.
     * 
     * @param other
     * @return a new linked list that is the union of this and other.
     */
    public GenericLinkedList<T> union(GenericLinkedList<T> other) {
        GenericLinkedList<T> first = this;
        GenericLinkedList<T> union = null;
        do {

            System.out.println("Comparing " + first.head + " with " + other.head);
            if(first.head.hashCode() ==  other.head.hashCode()) {
                union = this.addCreate(union, first.head);
            } else if(first.head.hashCode() > other.head.hashCode()) {
                union = this.addCreate(union, other.head);
                union = this.addCreate(union, first.head);
            } else {
                union = this.addCreate(union, first.head);
                union = this.addCreate(union, other.head);
            }
            first = first.tail;
            other = other.tail;
        } while (first != null && other != null);
        if(first != null) {
            for(;first != null; first = first.tail)
            union = union.append(first.head);
        }
        if(other != null) {
            for(;other != null; other = other.tail)
            union = union.append(other.head);
        }
        return union;
    }

    public int size() {
        int size = 0;
        for (GenericLinkedList<T> s = this; s != null; s = s.tail) {
            size++;
        }
        return size;
    }

    public GenericLinkedList<T> append(T t) {
        if (this.tail == null) {
            return new GenericLinkedList<T>(this.head, new GenericLinkedList<T>(t));
        }
        return new GenericLinkedList<T>(this.head, this.tail.append(t));
    }

    /**
     * Return this set in reverse.
     * @return a new linked list with this lists elements in reverse.
     */
    public GenericLinkedList<T> reverse() {
        if (this.tail == null) {
            return new GenericLinkedList<T>(this.head);
        }
        return this.tail.reverse().append(this.head);
    }

    /**
     * Returns the first item in this linked list This operation runs in O(1) time.
     * 
     * @return this first item in this list.
     */
    public T first() {
        return this.head;
    }

    /**
     * Returns the last items in this linked list. This operation runs in O(n) time.
     * 
     * @return the last item in this list
     */
    public T last() {
        GenericLinkedList<T> last = this;
        for (; last.tail != null; last = last.tail)
            ;
        return last.head;
    }
}