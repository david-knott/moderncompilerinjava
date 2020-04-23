import org.junit.Test;

import RegAlloc.BitArraySet;
import Temp.Temp;
import Temp.TempList;

public class BitArraySetTest {

    @Test
    public void test1() {
        Temp t1 = new Temp();
        Temp t2 = new Temp();
        Temp t3 = new Temp();
        Temp t4 = new Temp();
        Temp t5 = new Temp();
        Temp t6 = new Temp();

        BitArraySet set1 = new BitArraySet(new TempList(t1, new TempList(t2, new TempList(t3, null))), 7);
        BitArraySet set2 = new BitArraySet(new TempList(t4, new TempList(t5, new TempList(t6, null))), 7);
        System.out.println(set1.union(set2));

        set1 = new BitArraySet(new TempList(t1, new TempList(t2, new TempList(t3, null))), 7);
        set2 = new BitArraySet(new TempList(t1, new TempList(t2, new TempList(t6, null))), 7);
        System.out.println(set1.union(set2));
    }
}