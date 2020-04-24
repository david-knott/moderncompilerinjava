package Main;

import Canon.Canonicalization;
import Translate.Frag;
import Translate.FragProcessor;
import Tree.StmList;
import Tree.StmListList;

class TreeContainer {

    private final Frag frags;
    private final Canonicalization canonicalization;
    private final FragProcessor fragProcessor;

    public TreeContainer(FragProcessor fragProcessor, Frag frags, Canonicalization canonicalization) {
        this.frags = frags;
        this.canonicalization = canonicalization;
        this.fragProcessor = fragProcessor;
    }

    /**
     * Iterate through all the fragments calling their process method. This will
     * apply canonicalization to the IR tree is the Frag is a text Frag. Data Frags,
     * which only contain String data are not affected by this. Calling this method
     * builds up a link of processed fragments which can be passed on to the code
     * generation phase.
     */
    public StmListList process() {
        StmListList stmListList = null;
        for (Frag frag = this.frags; frag != null; frag = frag.next) {
            var stmtList = frag.process(this.canonicalization, this.fragProcessor);
            if (stmListList == null) {
                stmListList = new StmListList(stmtList, null);
            } else {
                stmListList.append(stmtList);
            }
        }
        return stmListList;
    }
}


class TreeContainerExampleReturn {

    Frag frag;
    StmList tree;
    TreeContainerExampleReturn tail;
}