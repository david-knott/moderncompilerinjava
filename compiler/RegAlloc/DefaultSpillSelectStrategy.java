package RegAlloc;

import Temp.Temp;
import Temp.TempList;

/**
 * Default strategy returns the first temp in the spill list.
 */
public class DefaultSpillSelectStrategy implements SpillSelectStrategy {

    @Override
    public Temp spill(TempList tempList) {
        return tempList.head;
    }
}