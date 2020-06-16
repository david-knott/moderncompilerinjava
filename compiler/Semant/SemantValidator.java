package Semant;

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
    
    public static boolean isInt(ExpTy expTy) {
        return expTy.ty.actual() instanceof INT;   
    }
    
    public static boolean isString(ExpTy expTy) {
        return expTy.ty.actual() instanceof STRING;   
    }
    
    public static boolean isArray(ExpTy expTy) {
        return expTy.ty.actual() instanceof ARRAY;   
    }
    
    public static boolean isRecord(ExpTy expTy) {
        return (expTy.ty.actual() instanceof RECORD);
    }
    
    public static boolean isNil(ExpTy expTy) {
        return expTy.ty.actual() instanceof NIL;   
    }
    
    public static boolean sameType(ExpTy expTy1, ExpTy expTy2) {
        return expTy1.ty.coerceTo(expTy2.ty);   
    }
    
    public static boolean isVoid(ExpTy expTy) {
        return expTy.ty.actual() instanceof VOID;   
    }    
}