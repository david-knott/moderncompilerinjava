package Main;

import Canon.BasicBlocks;
import Canon.Canon;
import Canon.StmListList;
import Canon.TraceSchedule;
import Translate.DataFrag;
import Translate.Frag;
import Translate.ProcFrag;
import Tree.Stm;
import Tree.StmList;

/**
 * Encapsulates the canonicalisation phase
 */
class Canonicalizer {

    private Frag frags;

    public Canonicalizer(Frag frags) {
        this.frags = frags;
    }

    private StmList convert(Stm stm) {
        StmList stms = Canon.linearize(stm);
        BasicBlocks b = new BasicBlocks(stms);
        StmList traced = (new TraceSchedule(b)).stms;
        return traced;
    }

    private void process()  {
        StmListList stmListList = null;
        for (Frag frag = frags; frag != null; frag = frag.next) {
            StmList sl = null;
            if (frag instanceof ProcFrag) {
                var procFrag = (ProcFrag) frag;
                sl = convert(procFrag.body);
            } else {
                var dataFrag = (DataFrag)frag;
                //TODO: Data Frag Statement
            }
            if(stmListList == null) {
                stmListList  = new StmListList(sl, null);
            } else {
                stmListList.append(sl);
            }
        }
    }
}