package Temp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;

import Assem.Instr;
import Assem.InstrList;
import Assem.TEST;
import Canon.CanonFacadeImpl;
import Frame.Frame;
import Intel.IntelFrame;
import Semant.Semant;
import Temp.Label;
import Translate.Exp;
import Tree.CALL;
import Tree.EXP;
import Tree.MEM;
import Tree.MOVE;
import Tree.TEMP;
import Types.ARRAY;
import Util.GenericLinkedList;

class TestItem {
    public String s;
    public TestItem(String s) {
        this.s = s;
    }

    public String toString() {
        return this.s;
    }
}

public class TempTest {

    @Test
    public void append() {
        TestItem first = new TestItem("one");
        TestItem last = new TestItem("four");
        GenericLinkedList<TestItem> list2 = new GenericLinkedList<TestItem>(first)
            .append(new TestItem("two"))
            .append(new TestItem("three"))
            .append(last)
            ;
        assertNotNull(list2);
        assertEquals(first, list2.first());
        assertEquals(last, list2.last());
    }

    @Test
    public void reverse() {
        TestItem first = new TestItem("one");
        TestItem last = new TestItem("four");
        GenericLinkedList<TestItem> list2 = new GenericLinkedList<TestItem>(first)
            .append(new TestItem("two"))
            .append(new TestItem("three"))
            .append(last)
            .reverse()
            ;
        assertNotNull(list2);
        assertEquals(first, list2.last());
        assertEquals(last, list2.first());
    }
    
    @Test
    public void union() {
        TestItem one = new TestItem("one");
        TestItem two = new TestItem("two");
        TestItem three = new TestItem("three");
        TestItem four = new TestItem("four");
        TestItem five = new TestItem("five");
        TestItem six = new TestItem("six");
        GenericLinkedList<TestItem> list1 = new GenericLinkedList<TestItem>(one)
            .append(two)
            .append(three)
            .append(four)
            ;
        GenericLinkedList<TestItem> list2 = new GenericLinkedList<TestItem>(one)
        .append(two).append(six);
        GenericLinkedList<TestItem> list3 = list1.union(list2);
        assertNotNull(list3);

    }
    



}