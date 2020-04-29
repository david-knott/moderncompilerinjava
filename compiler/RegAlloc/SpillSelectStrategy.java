package RegAlloc;

import Temp.Temp;
import Temp.TempList;

/**
 * Interface that all spill select strategies must implement.
 */
public interface SpillSelectStrategy {

    Temp spill(TempList tempList);
}