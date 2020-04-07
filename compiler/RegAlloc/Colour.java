package RegAlloc;

import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;

class Colour implements TempMap {
    InterferenceGraph ig; TempMap initial; TempList registers;

    public Colour(InterferenceGraph ig, TempMap initial, TempList registers) {
        if(ig == null) throw new Error("ig cannot be null");
        if(initial == null) throw new Error("initial cannot be null");
        if(registers == null) throw new Error("register cannot be null");
        this.ig = ig;
        this.initial = initial;
        this.registers = registers;
    }

    public TempList spills() {
        return null;
    }

    @Override
    public String tempMap(Temp t) {
        // TODO Auto-generated method stub
        return null;
    }
}