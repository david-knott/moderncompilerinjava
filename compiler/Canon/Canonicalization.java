package Canon;

import Tree.Stm;
import Tree.StmList;

/**
 * Produces a canonicalization of the IR Tree
 */
public interface Canonicalization {

    StmList canon(Stm stm);
}