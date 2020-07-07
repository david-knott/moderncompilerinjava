package Absyn;

import java.io.PrintStream;

import Codegen.Assert;
import Symbol.Symbol;

enum IndentationType {
    Tabs, Spaces, Fib
}

/**
 * Returns a formatted version of the AST. This formatted version can be feed
 * back into the compiler. See
 * https://www.lrde.epita.fr/~tiger/assignments.split/TC_002d2-Pretty_002dPrinting-Samples.html#TC_002d2-Pretty_002dPrinting-Samples
 */
public class PrettyPrinter implements AbsynVisitor {
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

    private void say(Symbol symbol) {
        say(symbol.toString());
    }

    private void say(String str) {
        for (int i = 0; i < this.currentIndentation * this.indentation; i++)
            out.print(this.spaces ? " " : "\t");
        out.print(str);
    }

    @Override
    public void visit(ArrayExp exp) {
        say("array");
        exp.init.accept(this);
        exp.size.accept(this);
    }

    @Override
    public void visit(ArrayTy exp) {
        say("arrayTy");
    }

    @Override
    public void visit(AssignExp exp) {
        // TODO Auto-generated method stub
        say("assignExp");

    }

    @Override
    public void visit(BreakExp exp) {
        say("break");
    }

    @Override
    public void visit(CallExp exp) {
        // TODO Auto-generated method stub
        say(exp.func.toString());
        say("(");
        for (ExpList expList = exp.args; expList != null; expList = expList.tail) {
            expList.head.accept(this);
            if (expList.tail != null)
                say(",");
        }
        say(")");

    }

    @Override
    public void visit(Dec exp) {
        // TODO Auto-generated method stub
        say("dec");

    }

    @Override
    public void visit(DecList exp) {
        for(;exp != null; exp = exp.tail) {
            exp.head.accept(this);
        }
    }

    @Override
    public void visit(ExpList exp) {
        // TODO Auto-generated method stub
        say("expList");

    }

    @Override
    public void visit(FieldExpList exp) {
        // TODO Auto-generated method stub
        say("fieldExpList");

    }

    @Override
    public void visit(FieldList exp) {
        if(exp != null) {
            do
            {
                say(exp.name);
                say(": ");
                say(exp.typ);
                if(exp.escape) {
                    say("/* escapes */");
                }
                if(exp.tail != null) {
                    say(",");
                }
                exp = exp.tail;
            }while(exp != null);
        }
    }

    @Override
    public void visit(FieldVar exp) {
        // TODO Auto-generated method stub
        say("fieldVar");

    }

    @Override
    public void visit(ForExp exp) {
        // TODO Auto-generated method stub
        say("forExp");

    }

    @Override
    public void visit(FunctionDec functionDec) {
        say("function ");
        say(functionDec.name.toString());
        say("(");
        if (functionDec.params != null)
            functionDec.params.accept(this);
        if (functionDec.result != null)
            functionDec.result.accept(this);
        functionDec.body.accept(this);
        say(")");

    }

    @Override
    public void visit(IfExp exp) {
        say("if ");
        say("(");
        exp.test.accept(this);
        say(")");
        say(" then ");
        say("(");
        exp.thenclause.accept(this);
        say(")");
        if (exp.elseclause != null) {
            say(" else ");
            say("(");
            exp.elseclause.accept(this);
            say(")");
        }
    }

    @Override
    public void visit(IntExp exp) {
        say(Integer.toString(exp.value));
    }

    @Override
    public void visit(LetExp letExp) {
        say("let");
        if (letExp.decs != null)
            letExp.decs.accept(this);
        say("in");
        if (letExp.body != null)
            letExp.body.accept(this);
        say("end");
    }

    @Override
    public void visit(NameTy exp) {
        // TODO Auto-generated method stub

        say("NameTyp");
    }

    @Override
    public void visit(NilExp exp) {
        say("nil");
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
        line("(");
        exp.list.accept(this);
        line("(");
    }

    @Override
    public void visit(SimpleVar exp) {

    }

    @Override
    public void visit(StringExp exp) {
        say("\"" + exp.value + "\"");
    }

    @Override
    public void visit(SubscriptVar exp) {
        exp.var.accept(this);
        say("[");
        exp.index.accept(this);
        say("]");
    }

    @Override
    public void visit(TypeDec exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(Var exp) {

    }

    @Override
    public void visit(VarDec exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(VarExp exp) {

        say("varExp");
        exp.var.accept(this);

    }

    @Override
    public void visit(WhileExp exp) {
        say("while ");
        exp.test.accept(this);
        say("do");
        say("(");
        exp.body.accept(this);
        say(")");
    }
}
