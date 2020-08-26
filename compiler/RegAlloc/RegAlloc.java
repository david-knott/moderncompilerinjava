package RegAlloc;

import Assem.InstrList;
import Temp.TempMap;

public interface RegAlloc extends TempMap {

    InstrList getInstrList();

}