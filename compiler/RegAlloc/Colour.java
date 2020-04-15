package RegAlloc;

import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;

class Colour implements TempMap {
    InterferenceGraph ig;
    TempMap initial;
    TempList registers;

    /**
     * Constructor for a Colour. This class implements the TempMap
     * interface, which provides a TempMap that contains all the
     * colour for the programs temporaries. 
     * @param ig the temporary inteference graph for the program
     * @param initial the pre coloured temp map
     * @param registers the list of temporarie that are to be coloured
     */
    public Colour(InterferenceGraph ig, TempMap initial, TempList registers) {
        if (ig == null)
            throw new Error("ig cannot be null");
        if (initial == null)
            throw new Error("initial cannot be null");
        if (registers == null)
            throw new Error("register cannot be null");
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