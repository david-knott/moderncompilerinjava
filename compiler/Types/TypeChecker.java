package Types;

import Absyn.DefaultVisitor;
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
class TypeChecker extends DefaultVisitor {
    
    Type expType;
    private final ErrorMsg errorMsg;

    public TypeChecker(ErrorMsg errorMsg) {
        this.errorMsg = errorMsg;
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
        //check the argument types
    }

    /**
     * Type checks an AssignExp. 
     * Ensures that the lvalue and rvalue types are compatible.
     * If they are not, an error message is generated.
     */
    @Override
    public void visit(AssignExp exp) {
        //check lvaue type is same as rvalue
        exp.var.accept(this);
        Type lValueType = this.expType;
        exp.exp.accept(this);
        Type rValueType = this.expType;
        if (!lValueType.coerceTo(rValueType)) {
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
			if(!this.expType.coerceTo(initType)) {
                this.errorMsg.error(exp.pos, "");
			}
		}
	}


}