package Intel;

import Temp.Label;
import Temp.Temp;
import Util.BoolList;
import Frame.*;

class IntelFrame extends Frame {

    // record the access
    private int off = WORD_SIZE;
    private static final int WORD_SIZE = 8;

    IntelFrame(Label nm, BoolList frml) {
        name = nm;
        BoolList tmp = frml;
        AccessList al = null;
        AccessList prev =  null;
        while(tmp != null){
            al = new AccessList(allocLocal(tmp.head), prev);
            prev = al;
            tmp = tmp.tail;
        }
        formals = al;
    }

    @Override
    public Access allocLocal(boolean escape) {
        return escape ? new InFrame(off + WORD_SIZE) : new InReg(new Temp());
    }

    @Override
    public Frame newFrame(Label name, BoolList formals) {
        return new IntelFrame(name, formals);
    }
}

class InFrame extends Access {

    int offset;

    InFrame(int os) {
        offset = os;
    }
}

class InReg extends Access {
    Temp temp;

    InReg(Temp tmp) {
        temp = tmp;
    }
}