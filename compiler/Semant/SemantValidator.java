package Semant;

import Absyn.Absyn;
import Codegen.Assert;
import Symbol.Symbol;
import Translate.ExpTy;
import Types.ARRAY;
import Types.INT;
import Types.NIL;
import Types.RECORD;
import Types.STRING;
import Types.VOID;

/**
 * Validation methods for semantic type checking.
 */
class SemantValidator {
    private boolean errors = false;

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
}