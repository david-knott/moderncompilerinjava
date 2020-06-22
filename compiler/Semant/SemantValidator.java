package Semant;

import Absyn.Absyn;
import Absyn.Exp;
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

    public String format() {
        // ../../compiler/Translate/Translator.java:188: error: incompatible types: int
        // cannot be converted to String
        // String s = 2
        String path = "";
        int lineNumber = 0;
        String level = "error";
        String category = "incompatible types";
        String message = "int cannot be converted to a string";
        String line = "\t\tString s = 2";
        return String.format("%1$s:%2$d: $3$s: $4$s: $5$s\n$6$s", path, lineNumber, level, category, message, line);
    }

    public boolean isInt(ExpTy expTy, int pos) {
        return capture(expTy.ty.actual() instanceof INT);
    }

    public boolean isString(ExpTy expTy) {
        return capture(expTy.ty.actual() instanceof STRING);
    }

    public boolean isArray(ExpTy expTy) {
        return capture(expTy.ty.actual() instanceof ARRAY);
    }

    public boolean isRecord(ExpTy expTy, int pos) {
        return capture(expTy.ty.actual() instanceof RECORD);
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

    public void sameType(ExpTy initExpTy, Type transTy, int pos) {
        if (!initExpTy.ty.coerceTo(transTy)) {
            this.errorMsg.error(pos, String.format("%1$s cannot be converted to a %2$s.", initExpTy.ty, transTy));
        }
    }

    public void nilAssignedToRecord(ExpTy initExpTy, Type other, int pos) {
        if (initExpTy.ty.actual() == Semant.NIL && !(other.actual() instanceof RECORD)) {
            this.errorMsg.error(pos, String.format("%1$s is not a record type.", other));
        }
    }

    public void illegalBreak(Label breakScopeLabel, int pos) {
        if(breakScopeLabel == null) {
            this.errorMsg.error(pos, "illegal break position.");
        }
    }

    public void checkVariable(Symbol sym, int pos) {
        if( null == (VarEntry) env.venv.get(sym)) {
            this.errorMsg.error(pos, String.format("Undefined variable %1$s.", sym));
        }
    }

	public void checkType(Symbol sym, int pos) {
        if( null == (Types.Type) env.tenv.get(sym)) {
            this.errorMsg.error(pos, String.format("Undefined type %1$s.", sym));
        }
	}
}