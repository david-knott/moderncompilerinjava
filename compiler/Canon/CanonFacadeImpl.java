package Canon;

import Tree.Stm;
import Tree.StmList;

public class CanonFacadeImpl implements Canonicalization {

    private StmList linearize(Stm stm) {
        StmList stms = Canon.linearize(stm);
        return stms;
    }

    private StmList traces(StmList stmList) {
        BasicBlocks b = new BasicBlocks(stmList);
        StmList traced = (new TraceSchedule(b)).stms;
        //append done label to trace
        var end = traced;
        while(end.tail != null) end = end.tail;
        end.tail = new StmList(new Tree.LABEL(b.done), null);
        return traced;
    }

    @Override
    public StmList canon(Stm stm) {
        return traces(linearize(stm));
    }
}