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
import Absyn.TypeDec;
import Absyn.Var;
import Absyn.VarDec;
import Absyn.VarExp;
import Absyn.WhileExp;
import Core.LL;
import ErrorMsg.Error;
import ErrorMsg.ErrorMsg;

/**
 * A TypeChecker visitor is used to check the <see ref="Absyn.Exp"> againts
 * the defined language specification, also know as the semantic rules.
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

    @Override
    public void visit(IntExp exp) {
        this.expType = Constants.INT;
    }

    @Override
    public void visit(StringExp exp) {
        this.expType = Constants.STRING;
    }

    @Override
    public void visit(NilExp exp) {
        this.expType = Constants.NIL;
    }

    @Override
    public void visit(CallExp exp) {
        // check the formal argument types against the actual argument types.
    }

    @Override
    public void visit(SimpleVar exp) {
        throw new java.lang.Error("TBD: Calculate type for simplevar.");
    }

    @Override
    public void visit(NameTy exp) {
        // what type is namety ??
        throw new java.lang.Error("TBD: Calculate NameTy type.");
    }
    /**
     * Type check ForExp.
     */
    @Override
    public void visit(ForExp exp) {
        this.readonlyVarDecs.add(exp.var);
        exp.body.accept(this);
        throw new java.lang.Error("TBD: Ensure index is not assigned too.");
    }

    @Override
    public void visit(OpExp exp) {
        exp.left.accept(this);
        Type leftType = this.expType;
        exp.right.accept(this);
        Type rightType = this.expType;
        if(!leftType.coerceTo(rightType)) {
            this.errorMsg.error(exp.pos, String.format("type mismatch\nright operand type:%s\nexpected type:%s", rightType, leftType));
        }
    }

    /**
     * Type checks an AssignExp. 
     * Ensures that the lvalue and rvalue types are compatible.
     * If they are not, an error message is generated.
     */
    @Override
    public void visit(AssignExp exp) {
        // check lvaue type is same type as rvalue
        exp.var.accept(this);
        Type lValueType = this.expType;
        exp.exp.accept(this);
        Type rValueType = this.expType;
        if (!lValueType.coerceTo(rValueType)) {
            this.errorMsg.error(exp.pos, "");
        }
        if(this.readonlyVarDecs.contains(exp.var.def)) {
            this.errorMsg.error(exp.pos, "");
        }
    }

	/**
	 * Represents the declaration of a variable with an optional type
	 * for example: var myVar:int = 1
	 */
	@Override
	public void visit(VarDec exp) {
        // visit initializer.
        exp.init.accept(this);
		Type initType = this.expType;
		// visit the type if defined.
		if(exp.typ != null) {
            exp.typ.accept(this);
            Type declaredType = this.expType;
			if(!declaredType.coerceTo(initType)) {
                this.errorMsg.error(exp.pos, "");
			}
		}
	}
}