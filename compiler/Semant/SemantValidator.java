package Semant;

import Translate.ExpTy;
import Types.INT;
import Types.RECORD;

/**
 * Validation methods for semantic type checking.
 */
class SemantValidator {
    
    public static boolean isInt(ExpTy expTy) {
        return expTy.ty.actual() instanceof INT;   
    }
    
    public static boolean isString(ExpTy expTy) {
        return false;   
    }
    
    public static boolean isArray(ExpTy expTy) {
        return false;   
    }
    
    public static boolean isRecord(ExpTy expTy) {
        return (expTy.ty.actual() instanceof RECORD);
    }
    
    public static boolean isNil(ExpTy expTy) {
        return false;   
    }
    
    public static boolean sameType(ExpTy expTy1, ExpTy expTy2) {
        return false;   
    }
    
    public static boolean isVoid(ExpTy expTy) {
        return false;   
    }    
}