package Types;

import Absyn.DefaultVisitor;

import java.util.LinkedList;
import java.util.Collection;

import Absyn.Absyn;
import Absyn.ArrayExp;
import Absyn.ArrayTy;
import Absyn.AssignExp;
import Absyn.BreakExp;
import Absyn.CallExp;
import Absyn.DecList;
import Absyn.ExpList;
import Absyn.FieldExpList;
import Absyn.FieldList;
import Absyn.FieldVar;
import Absyn.ForExp;
import Absyn.FunctionDec;
import Absyn.IfExp;
import Absyn.IntExp;
import Absyn.LetExp;
import Absyn.NameTy;
import Absyn.NilExp;
import Absyn.OpExp;
import Absyn.RecordExp;
import Absyn.RecordTy;
import Absyn.SeqExp;
import Absyn.SimpleVar;
import Absyn.StringExp;
import Absyn.SubscriptVar;
import Absyn.Typable;
import Absyn.TypeDec;
import Absyn.Var;
import Absyn.VarDec;
import Absyn.VarExp;
import Absyn.WhileExp;
import Core.LL;
import ErrorMsg.Error;
import ErrorMsg.ErrorMsg;

/**
 * A TypeChecker visitor is used to check the <see ref="Absyn.Exp"> againts the
 * defined language specification, also know as the semantic rules.
 * 
 * https://www.lrde.epita.fr/~tiger/assignments.split/TC_002d4.html#TC_002d4
 * 
 * https://gitlab.lrde.epita.fr/tiger/tc-base/-/blob/2022/src/type/type-checker.cc
 */
public class TypeChecker extends DefaultVisitor {

    Type expType;
    final ErrorMsg errorMsg;
    Collection<Absyn> readonlyVarDecs = null;

    public TypeChecker(ErrorMsg errorMsg) {
        this.errorMsg = errorMsg;
        this.readonlyVarDecs = new LinkedList<Absyn>();
    }

    private void checkTypes(Absyn loc, String synCat1, Type first, String synCat2, Type second) {
        if (!first.coerceTo(second)) {
            this.errorMsg.error(loc.pos, "checkTypes error " + first + "!=" + second);
        }
    }

    private void checkTypes(Absyn loc, String synCat1, Typable first, String synCat2, Typable second) {
       // if (!first.coerceTo(second)) {
          //  this.errorMsg.error(loc.pos, "checkTypes error " + first + "!=" + second);
       //S }
    }

    private Type getExpType() {
        return this.expType;
    }


    /** ============== AST Expressions ============================== **/

    /**
     * Sets current visited type to Int.
     */
    @Override
    public void visit(IntExp exp) {
        this.expType = Constants.INT;
    }

    /**
     * Sets current visisted type to String.
     */
    @Override
    public void visit(StringExp exp) {
        this.expType = Constants.STRING;
    }

    /**
     * Sets current visisted type to Nil.
     */
    @Override
    public void visit(NilExp exp) {
        this.expType = Constants.NIL;
    }

    /**
     * Visits a call node. The implementation checks that the actual arguments match
     * the formal arguments in the function definition. It sets the current visited
     * type to the return type of the function.
     */
    @Override
    public void visit(CallExp exp) {
        FunctionDec functionDec = (FunctionDec)exp.def;
        ExpList actuals = exp.args;
        FieldList formals = functionDec.params;
        for (;;) {
            // visit each actual parameter
            actuals.head.accept(this);
            Type actualType = this.getExpType();
            // get the type of the formal from its types def.
            NameTy formalTypeExp = formals.typ;
            Type formalType = formalTypeExp.getType();
            this.checkTypes(actuals.head, "", actualType, "", formalType);
            if(actuals.tail == null || formals.tail != null) {
                break;
            }
            actuals = actuals.tail;
            formals = formals.tail;
        }
        if(actuals.tail != null) {
            this.errorMsg.error(actuals.head.pos, "more actuals than expected");

        }
        if(formals.tail != null) {
            this.errorMsg.error(exp.pos, "less actuals than expected");
        }
    }

    /**
     * Type check implementation for simple var. This sets the current visited type
     * to its own type.
     */
    @Override
    public void visit(SimpleVar exp) {
        this.expType = exp.getType();
    }

    /**
     * Visit a NameTy node. This in turn visits its definition to retreive its type.
     * Currently there is a problem if the NameTy refers to an int or string. Native types
     * do not have a definition and because of the the NameTy def property is null.
     */
    @Override
    public void visit(NameTy exp) {
        if(exp.def != null) {
            exp.def.accept(this);
        }
    }

    /**
     * Type check implementation for ForExp. Checks that the hi and low values are
     * integers and marks the loop low variable declaration as readonly.
     */
    @Override
    public void visit(ForExp exp) {
        exp.hi.accept(this);
        Type hiType = this.expType;
        this.checkTypes(exp, "", hiType, "", Constants.INT);
        exp.var.init.accept(this);
        Type varType = this.expType;
        this.readonlyVarDecs.add(exp.var);
        this.checkTypes(exp, "", varType, "", Constants.INT);
        exp.body.accept(this);
    }

    /**
     * Visit a binary operator and check the types are compatible.
     */
    @Override
    public void visit(OpExp exp) {
        exp.left.accept(this);
        Type leftType = this.expType;
        exp.right.accept(this);
        Type rightType = this.expType;
        if (!leftType.coerceTo(rightType)) {
            this.errorMsg.error(exp.pos,
                    String.format("type mismatch\nright operand type:%s\nexpected type:%s", rightType, leftType));
        }
    }

    /**
     * Type checks an AssignExp. Ensures that the lvalue and rvalue types are
     * compatible. If they are not, an error message is generated.
     */
    @Override
    public void visit(AssignExp exp) {
        // check lvaue type is same type as rvalue
        exp.var.accept(this);
        Type leftType = this.expType;
        exp.exp.accept(this);
        Type rightType = this.expType;
        if (!leftType.coerceTo(rightType)) {
            this.errorMsg.error(exp.pos,
                    String.format("type mismatch\nright operand type:%s\nexpected type:%s", rightType, leftType));
        }
        if (this.readonlyVarDecs.contains(exp.var.def)) {
            this.errorMsg.error(exp.pos, "variable is read only");
        }
    }


    /** ============== AST Declarations ============================== **/

    /**
     * Represents the declaration of a variable which may include the type for
     * example: var myVar:int = 1
     */
    @Override
    public void visit(VarDec exp) {
        // visit initializer.
        exp.init.accept(this);
        Type initType = this.expType;
        // visit the type if defined.
        if (exp.typ != null) {
            exp.typ.accept(this);
            Type declaredType = this.expType;
            this.checkTypes(exp, "", declaredType, "", initType);
        }
    }

    @Override
    public void visit(TypeDec exp) {
        this.expType = exp.getType();
    }

    /**
     * 
     */
    @Override
    public void visit(FunctionDec exp) {
        this.expType = exp.getType();
    }
}