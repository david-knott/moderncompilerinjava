package Absyn;

import java.io.PrintStream;

import Util.Assert;
import Symbol.Symbol;

/**
 * Returns a formatted version of the AST. This formatted version can be feed
 * back into the compiler. See
 * https://www.lrde.epita.fr/~tiger/assignments.split/TC_002d2-Pretty_002dPrinting-Samples.html#TC_002d2-Pretty_002dPrinting-Samples
 */
public class PrettyPrinter implements AbsynVisitor {
    private PrintStream out;
    private boolean spaces = true;
    private final int indentation = 2;
    private int currentIndentation = 0;
    public boolean escapesDisplay = false;
    public boolean bindingsDisplay = false;

    public PrettyPrinter(PrintStream o) {
        Assert.assertNotNull(o);
        this.out = o;
    }

    public PrettyPrinter(PrintStream printStream, boolean escapesDisplay, boolean bindingsDisplay) {
        Assert.assertNotNull(printStream);
        this.out = printStream;
        this.escapesDisplay = escapesDisplay;
        this.bindingsDisplay = bindingsDisplay;
	}

	public void print(Exp exp) {
        lineBreak();
        exp.accept(this);
    }

    private void lineBreak() {
        say(System.lineSeparator());
        for (int i = 0; i < this.currentIndentation * this.indentation; i++)
            out.print(this.spaces ? " " : "\t");

    }

    private void say(Symbol symbol) {
        say(symbol.toString());
    }

    private void space() {
        say(" ");
    }

    private void say(String str) {
        out.print(str);
    }

    @Override
    public void visit(ArrayExp exp) {
        say(exp.typ);
        say("[");
        exp.size.accept(this);
        say("]");
        space();
        say("of");
        space();
        exp.init.accept(this);
    }

    @Override
    public void visit(ArrayTy exp) {
        say("array");
        space();
        say("of");
        space();
        say(exp.typ);
    }

    @Override
    public void visit(AssignExp exp) {
        exp.var.accept(this);
        space();
        say(":=");
        space();
        exp.exp.accept(this);
    }

    @Override
    public void visit(BreakExp exp) {
        space();
        say("break");
    }

    @Override
    public void visit(CallExp exp) {
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
    public void visit(DecList exp) {
        for(;exp != null; exp = exp.tail) {
            exp.head.accept(this);
            lineBreak();
        }
    }

    @Override
    public void visit(ExpList exp) {
        for(;exp != null; exp = exp.tail) {
            exp.head.accept(this);
            if(exp.tail != null) {
                say(";");
            }
            lineBreak();
        }
    }

    @Override
    public void visit(FieldExpList exp) {
        for(;exp != null; exp = exp.tail) {
            say(exp.name);
            say("=");
            exp.init.accept(this);
            if(exp.tail != null)
                say(",");
        }
    }

    @Override
    public void visit(FieldList exp) {
        if(exp != null) {
            do
            {
                say(exp.name);
                space();
                say(":");
                space();
                say(exp.typ);
                if(this.escapesDisplay && exp.escape) {
                    space();
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
        exp.var.accept(this);
        say(".");
        say(exp.field);
    }

    @Override
    public void visit(ForExp exp) {
        say("for");  
        space();
        exp.var.accept(this);
        space();
        say("to");
        space();
        exp.hi.accept(this);
        space();
        say("do");
        space();
        exp.body.accept(this);
    }

    @Override
    public void visit(FunctionDec functionDec) {
        say("function");
        space();
        say(functionDec.name.toString());
        say("(");
        if (functionDec.params != null)
            functionDec.params.accept(this);
        say(")");
        if (functionDec.result != null) {
            space();
            say(":");
            space();
            functionDec.result.accept(this);
        }
        space();
        say("=");
        space();
        currentIndentation++;
        lineBreak();
        functionDec.body.accept(this);
        currentIndentation--;
    }

    @Override
    public void visit(IfExp exp) {
        say("if");
        space();
        say("(");
        exp.test.accept(this);
        say(")");
        say("then");
        say("(");
        exp.thenclause.accept(this);
        say(")");
        if (exp.elseclause != null) {
            lineBreak();
            say("else");
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
        currentIndentation++;
        lineBreak();
        if (letExp.decs != null)
            letExp.decs.accept(this);
        currentIndentation--;
        lineBreak();
        say("in");
        currentIndentation++;
        lineBreak();
        if (letExp.body != null)
            letExp.body.accept(this);
        currentIndentation--;
        lineBreak();
        say("end");
        lineBreak();
    }

    @Override
    public void visit(NameTy exp) {
        say(exp.name);
    }

    @Override
    public void visit(NilExp exp) {
        space();
        say("nil");
    }

    @Override
    public void visit(OpExp exp) {
        exp.left.accept(this);
        space();
        switch(exp.oper) {
            case OpExp.PLUS: say("+"); break;
            case OpExp.MINUS: say("-"); break;
            case OpExp.MUL: say("*"); break;
            case OpExp.DIV: say("/"); break;
            case OpExp.EQ: say("="); break;
            case OpExp.NE: say("!="); break;
            case OpExp.LT: say("<"); break;
            case OpExp.LE: say("<="); break;
            case OpExp.GT: say(">"); break;
            case OpExp.GE: say(">="); break;
        }
        space();
        exp.right.accept(this);
    }

    @Override
    public void visit(RecordExp exp) {
        say(exp.typ);
        say("{");
        exp.fields.accept(this);
        say("}");
    }

    @Override
    public void visit(RecordTy exp) {
        exp.fields.accept(this);
    }

    @Override
    public void visit(SeqExp exp) {
        say("(");
        exp.list.accept(this);
        say(")");
    }

    @Override
    public void visit(SimpleVar exp) {
        say(exp.name);
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
        say("type");
        space();
        say(exp.name);
        space();
        say("=");
        space();
        say("{");
        space();
        exp.ty.accept(this);
        space();
        say("}");
    }

    @Override
    public void visit(Var exp) {

    }

    @Override
    public void visit(VarDec exp) {
        say("var");
        space();
        say(exp.name);
        space();
        if(exp.typ != null) {
            say(":");
            space();
            say(exp.typ.name);
            space();
        }
        say(":=");
        space();
        exp.init.accept(this);
        if(this.escapesDisplay && exp.escape) {
            say("/* escapes */");
            space();
        }
    }

    @Override
    public void visit(VarExp exp) {
        exp.var.accept(this);
    }

    @Override
    public void visit(WhileExp exp) {
        say("while");
        exp.test.accept(this);
        say("do");
        say("(");
        exp.body.accept(this);
        say(")");
    }
}
