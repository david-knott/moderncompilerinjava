package Assem;

public interface FragmentVisitor {

    void visit(ProcFrag procFrag);

    void visit(DataFrag dataFrag);

}