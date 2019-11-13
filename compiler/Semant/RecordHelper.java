package Semant;

import Absyn.TypeDec;
import Translate.Exp;

public class RecordHelper {
    private final TypeDec typeDec;
    private final Env env;

    public RecordHelper(final TypeDec typeDec, final Env env){
        this.typeDec = typeDec;
        this.env = env;
    }

    public Exp translateTy(){
        //first is this type recursive ?
        if(typeDec.next != null) {
            //yes, recursive
        }
        return null;
    }
}
 