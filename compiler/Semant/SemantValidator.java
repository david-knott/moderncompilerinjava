package Semant;

import Absyn.Absyn;
import Absyn.Exp;
import Absyn.SimpleVar;
import Absyn.Var;
import Codegen.Assert;
import ErrorMsg.ErrorMsg;
import Symbol.Symbol;
import Temp.Label;
import Translate.ExpTy;
import Types.ARRAY;
import Types.INT;
import Types.NIL;
import Types.RECORD;
import Types.STRING;
import Types.Type;
import Types.VOID;

/**
 * Validation methods for semantic type checking.
 */
class SemantValidator {
    private boolean errors = false;
    private ErrorMsg errorMsg;
    private Env env;

    public SemantValidator(Env e) {
        Assert.assertNotNull(e);
        this.errorMsg = e.errorMsg;
        this.env = e;
    }

    public boolean hasErrors() {
        return errors;
    }

    private boolean capture(boolean ok) {
        if (!ok) {
            errors = true;
        }

        return ok;
    }

    public boolean isInt(ExpTy expTy, int pos) {
        if (!(expTy.ty.actual() instanceof INT)) {
            this.errorMsg.error(pos, String.format("%1$s cannot be converted to an int.", expTy.ty));
            return false;
        }
        return true;
    }

    public boolean isString(ExpTy expTy, int pos) {
        if (!(expTy.ty.actual() instanceof STRING)) {
            this.errorMsg.error(pos, String.format("%1$s cannot be converted to an string.", expTy.ty));
            return false;
        }
        return true;
    }

    public boolean isArray(ExpTy expTy) {
        return capture(expTy.ty.actual() instanceof ARRAY);
    }

    public boolean isRecord(ExpTy expTy, int pos) {
        return expTy.ty != null && expTy.ty.actual() instanceof RECORD;
    }

    public void isRecord(Type tigerType, int pos) {
        capture(tigerType.actual() instanceof RECORD);
    }

    public boolean isNil(ExpTy expTy) {
        return capture(expTy.ty.actual() instanceof NIL);
    }

    public boolean sameType(ExpTy expTy1, ExpTy expTy2, int pos) {
        return capture(expTy1.ty.coerceTo(expTy2.ty));
    }

    public boolean isVoid(ExpTy expTy) {
        return capture(expTy.ty.actual() instanceof VOID);
    }

    public void undefinedField(Symbol field, int pos) {
    }

    public boolean sameType(ExpTy initExpTy, Type transTy, int pos) {
        if (!initExpTy.ty.coerceTo(transTy)) {
            this.errorMsg.error(pos, String.format("%1$s cannot be converted to a %2$s.", initExpTy.ty, transTy));
            return false;
        }
        return true;
    }

    public boolean nilAssignedToRecord(ExpTy initExpTy, Type other, int pos) {
        if (initExpTy.ty.actual() == Semant.NIL && !(other.actual() instanceof RECORD)) {
            this.errorMsg.error(pos, String.format("%1$s is not a record type.", other));
            return false;
        }
        return true;
    }

    public boolean illegalBreak(Label breakScopeLabel, int pos) {
        if (breakScopeLabel == null) {
            this.errorMsg.error(pos, "illegal break position.");
            return false;
        }
        return true;
    }

    public boolean checkVariableDefined(Symbol sym, int pos) {
        if (null == (VarEntry) env.venv.get(sym)) {
            this.errorMsg.error(pos, String.format("Undefined variable %1$s.", sym));
            return false;
        }
        return true;
    }

    public void checkType(Symbol sym, int pos) {
        if (null == (Types.Type) env.tenv.get(sym)) {
            this.errorMsg.error(pos, String.format("Undefined type %1$s.", sym));
        }
    }

    public void isReadonly(Var var, int pos) {
        if (var instanceof SimpleVar) {
            SimpleVar simpleVar = (SimpleVar) var;
            VarEntry varEntry = null;
            if ((varEntry = (VarEntry) env.venv.get(simpleVar.name)) != null && varEntry.readonly) {
                this.errorMsg.error(pos, String.format("Assign to readonly variable %1$s.", simpleVar.name));
            }
        }
    }

}