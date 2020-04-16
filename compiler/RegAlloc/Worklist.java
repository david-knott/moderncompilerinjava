package RegAlloc;


public class Worklist {

	public Worklist(InterferenceGraph ig) {
       //initial - temporary registers, not precoloured + not yet processed
       
       //for each element in initial, check their degree and add to appropriate

       //list, setting the current enum to indicate which set they were assigned to
	}

    /**
     * Gets the spill work list
     * @return
     */
	public Object getSpillWorklist() {
		return null;
	}

    /**
     * Gets the freeze work list
     * @return
     */
	public Object getFreezeWorklist() {
		return null;
	}

    /**
     * Gets the simlify work list
     * @return
     */
	public Object getimplifyWorklist() {
		return null;
	}

}