package Translate;

import java.io.PrintStream;

import Tree.PrettyPrinter;

class FragmentPrinter implements FragmentVisitor {

    private PrintStream out;

    public FragmentPrinter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void visit(ProcFrag procFrag) {
        procFrag.body.accept(new PrettyPrinter(this.out));
        this.out.println("Frame:");
        this.out.println("Wordsize:" + procFrag.frame.wordSize());
        //show layout, locals, formals, arguments
    }

    @Override
    public void visit(DataFrag dataFrag) {
        // TODO Auto-generated method stub
        this.out.println("Data:");
        this.out.println(dataFrag.data);
    }

}