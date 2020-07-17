package Types;

import Absyn.AbsynVisitor;
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

/**
 * A TypeChecker visitor is used to check the <see ref="Absyn.Exp"> againts
 * the defined language specification, also know as the semantic rules.
 * 
 * https://www.lrde.epita.fr/~tiger/assignments.split/TC_002d4.html#TC_002d4
 * 
 * https://gitlab.lrde.epita.fr/tiger/tc-base/-/blob/2022/src/type/type-checker.cc
 */
class TypeChecker implements AbsynVisitor {

	@Override
	public void visit(ArrayExp exp) {
		
	}

	@Override
	public void visit(ArrayTy exp) {
        
		
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
	public void visit(IfExp exp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IntExp exp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LetExp exp) {
		// TODO Auto-generated method stub
		
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
	public void visit(Var exp) {
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

	/* Declarations */

    private Type expType;

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
			if(this.expType.coerceTo(initType)) {
				//type mismatch error.
				this.logError();
			}
		}
	}

	@Override
	public void visit(TypeDec exp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(FunctionDec exp) {
		// TODO Auto-generated method stub
		
	}


	private void logError() {

	}

}

class PrettyPrinter {

}

