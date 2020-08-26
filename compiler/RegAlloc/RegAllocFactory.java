package RegAlloc;

import Assem.InstrList;
import Frame.Frame;

public class RegAllocFactory {
    
    public RegAlloc getRegAlloc(String name, Frame frame, InstrList instrList) {
        return new IterativeCoalescing(frame, instrList);
    }
}