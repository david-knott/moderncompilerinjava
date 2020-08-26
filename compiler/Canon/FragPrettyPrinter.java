package Canon;

import Translate.DataFrag;
import Translate.FragmentVisitor;
import Translate.ProcFrag;
import Tree.TreeVisitor;

class FragPrettyPrinter implements FragmentVisitor {
    final TreeVisitor treeVisitor;

    public FragPrettyPrinter(TreeVisitor treeVisitor) {
        this.treeVisitor = treeVisitor;
    }

    @Override
    public void visit(ProcFrag procFrag) {
        procFrag.body.accept(this.treeVisitor);
    }

    @Override
    public void visit(DataFrag dataFrag) {
        //do nothing.
    }

}