package Tree;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Prints out the LIR and HIR trees.
 */
public class PrettyPrinter implements TreeVisitor {

    final PrintStream printStream;

    public PrettyPrinter(OutputStream out) {
        this.printStream = new PrintStream(out);
    }
    
    private void write(String s) {
        this.printStream.println(s);
    }

	@Override
    public void visit(BINOP op) {
        this.write("binop(" + op.binop);
    }

    @Override
    public void visit(CALL op) {
        // TODO Auto-generated method stub
        this.write("call(" + op.func + ")");

    }

    @Override
    public void visit(CONST op) {
        this.write("const(" + op.value + ")");
    }

    @Override
    public void visit(ESEQ op) {
        // TODO Auto-generated method stub
        this.write("eseq");

    }

    @Override
    public void visit(EXP op) {
        // TODO Auto-generated method stub
        this.write("sxp(");

    }

    @Override
    public void visit(JUMP op) {
        // TODO Auto-generated method stub
        this.write("jump");

    }

    @Override
    public void visit(LABEL op) {
        // TODO Auto-generated method stub
        this.write("label");

    }

    @Override
    public void visit(MEM op) {
        // TODO Auto-generated method stub
        this.write("mem");

    }

    @Override
    public void visit(MOVE op) {
        // TODO Auto-generated method stub
        
        this.write("move");

    }

    @Override
    public void visit(NAME op) {
        // TODO Auto-generated method stub
        this.write("name");

    }

    @Override
    public void visit(SEQ op) {
        // TODO Auto-generated method stub
        this.write("seq");

    }

    @Override
    public void visit(TEMP op) {
        // TODO Auto-generated method stub
        this.write("temp");

    }

    @Override
    public void visit(CJUMP cjump) {
        // TODO Auto-generated method stub
        this.write("cjump");

    }

}