package Absyn;

import java.io.PrintStream;

import Codegen.Assert;

enum IndentationType {
    Tabs,
    Spaces,
    Fib
}
/**
 * Returns a formatted version of the AST. This formatted version can be feed
 * back into the compiler. See
 * https://www.lrde.epita.fr/~tiger/assignments.split/TC_002d2-Pretty_002dPrinting-Samples.html#TC_002d2-Pretty_002dPrinting-Samples
 */
class PrettyPrinter implements AbsynVisitor {
    private PrintStream out;
    private boolean spaces = true;
    private int indentation = 2;
    private int currentIndentation = 0;

    public PrettyPrinter(PrintStream o) {
        Assert.assertNotNull(o);
        this.out = o;
    }

    public void print(Exp exp) {
        line("/* == Abstract Syntax Tree. == */");
        lineBreak();
        exp.accept(this);
    }

    private void indent(int d) {
    }

    private void indent() {
        currentIndentation++;
    }

    private void outdent() {
        currentIndentation--;
    }

    private void lineBreak() {
        say(System.lineSeparator());
    }

    private void line(String line) {
        say(line);
        lineBreak();
    }

    private void say(String str) {
        for (int i = 0; i < this.currentIndentation * this.indentation; i++)
            out.print(this.spaces ? " " : "\t");
        out.print(str);
    }

    @Override
    public void visit(ArrayExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ArrayTy exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(AssignExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(BreakExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CallExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(Dec exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(DecList exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ExpList exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(FieldExpList exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(FieldList exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(FieldVar exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ForExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(FunctionDec functionDec) {
        // TODO Auto-generated method stub
        say("function ");
        say(functionDec.name.toString());
        indent();
        line("(");
        if (functionDec.params != null)
            functionDec.params.accept(this);
        if (functionDec.result != null)
            functionDec.result.accept(this);
        functionDec.body.accept(this);
        outdent();
        line(")");

    }

    @Override
    public void visit(IfExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(IntExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(LetExp letExp) {
        // TODO Auto-generated method stub
        line("let");
        if (letExp.decs != null)
            letExp.decs.accept(this);
        line("in");
        if (letExp.body != null)
            letExp.body.accept(this);
        line("end");

    }

    @Override
    public void visit(NameTy exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(NilExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(OpExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(RecordExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(RecordTy exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(SeqExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(SimpleVar exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(StringExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(SubscriptVar exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(TypeDec exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(Var exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(VarDec exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(VarExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(WhileExp exp) {
        // TODO Auto-generated method stub

    }
}
