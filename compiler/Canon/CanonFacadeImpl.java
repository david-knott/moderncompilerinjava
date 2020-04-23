package Canon;

import Tree.Stm;
import Tree.StmList;

public class CanonFacadeImpl implements Canonicalization {

    public StmList linearize(Stm stm) {
        StmList stms = Canon.linearize(stm);
        return stms;
    }

    public StmList traces(StmList stmList) {
        BasicBlocks b = new BasicBlocks(stmList);
        // print.prStm(new Tree.LABEL(b.done));
        StmList traced = (new TraceSchedule(b)).stms;
        return traced;
    }

    @Override
    public StmList canon(Stm stm) {
        return traces(linearize(stm));
    }
}