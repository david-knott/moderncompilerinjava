package Canon;

import Tree.Stm;
import Tree.StmList;

public interface Canonicalization {

    StmList canon(Stm stm);
}