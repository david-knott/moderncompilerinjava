package RegAlloc;

import Frame.Frame;
import Temp.DefaultMap;
import Temp.TempMap;

public class RegisterAllocator {

    public RegisterAllocatorResult getAllocatedAssembly(Frame frame) {

//    RegisterAllocator regAlloc = new RegisterAllocator(frame, instrs);
      //  regAlloc.allocate();
        TempMap tempmap2 = new Temp.CombineMap(frame, new DefaultMap());
    //    for (Assem.InstrList p = instrs; p != null; p = p.tail)
    //        out.print(p.head.format(tempmap2));
        // buildInterferenceGraph(instrs);
        // var procs = this.frame.procEntryExit3(instrs);*/


        return null;
    }

	public void allocate() {
        System.out.println("Allocating registers.");
	}
}