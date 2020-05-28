package Helpers;

import java.util.Hashtable;

import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;

public class TempMapHelper {

    public Hashtable<Temp, String> colours = new Hashtable<Temp, String>();

    public TempMapHelper(TempMap tempMap, TempList registers) {
        for (; registers != null; registers = registers.tail) {
            if (tempMap.tempMap(registers.head) != null)
                this.colours.put(registers.head, tempMap.tempMap(registers.head));
        }
    }
}