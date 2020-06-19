package Temp;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TempListTest {
    
    @Test
    public void checkOrderTrue() {
        Temp t1 = Temp.create();
        Temp t2 = Temp.create();
        Temp t3 = Temp.create();
        Temp t4 = Temp.create();
        TempList tl = null;
        tl = TempList.append(tl, t1);
        tl = TempList.append(tl, t2);
        tl = TempList.append(tl, t3);
        tl = TempList.append(tl, t4);
        TempList.checkOrder(tl);
    }

    @Test
    public void checkOrderFalse() {
        Temp t1 = Temp.create();
        Temp t2 = Temp.create();
        Temp t3 = Temp.create();
        Temp t4 = Temp.create();
        TempList tl = null;
        tl = TempList.append(tl, t2);
        tl = TempList.append(tl, t1);
        tl = TempList.append(tl, t3);
        tl = TempList.append(tl, t4);
        TempList.checkOrder(tl);
    }

    @Test
    public void sortOne() {
        Temp t1 = Temp.create();
        TempList tl = null;
        tl = TempList.append(tl, t1);
        tl = TempList.sort(tl);
        TempList.checkOrder(tl);
    }

    @Test
    public void sortTwo() {
        Temp t1 = Temp.create();
        Temp t2 = Temp.create();
        TempList tl = null;
        tl = TempList.append(tl, t2);
        tl = TempList.append(tl, t1);
        tl = TempList.sort(tl);
        TempList.checkOrder(tl);
    }

    @Test
    public void sortThree() {
        Temp t1 = Temp.create();
        Temp t2 = Temp.create();
        Temp t3 = Temp.create();
        TempList tl = null;
        tl = TempList.append(tl, t2);
        tl = TempList.append(tl, t1);
        tl = TempList.append(tl, t3);
        tl = TempList.sort(tl);
        TempList.checkOrder(tl);
    }

    @Test
    public void sortFour() {
        Temp t1 = Temp.create();
        Temp t2 = Temp.create();
        Temp t3 = Temp.create();
        Temp t4 = Temp.create();
        TempList tl = null;
        tl = TempList.append(tl, t4);
        tl = TempList.append(tl, t2);
        tl = TempList.append(tl, t1);
        tl = TempList.append(tl, t3);
        tl = TempList.sort(tl);
        TempList.checkOrder(tl);
    }









}