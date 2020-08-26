package Canon;

import Tree.Stm;
import Tree.StmList;

public class CanonicalizationImpl implements Canonicalization {

    private StmList linearize(Stm stm) {
        StmList stms = Canon.linearize(stm);
        return stms;
    }

    private StmList traces(StmList stmList) {
        BasicBlocks b = new BasicBlocks(stmList);
        StmList traced = (new TraceSchedule(b)).stms;
        return traced;
    }

    @Override
    public StmList canon(Stm stm) {
        return traces(linearize(stm));
    }


}