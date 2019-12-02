package Intel;

import Temp.Label;
import Temp.Temp;
import Util.BoolList;
import Frame.*;

class IntelFrame extends Frame {

    // record the access
    private int off = WORD_SIZE;
    private static final int WORD_SIZE = 8;

    IntelFrame(Label nm, AccessList frml) {
        name = nm;
        formals = frml;
    }

    @Override
    public Access allocLocal(boolean escape) {
        return escape ? new InFrame(off + WORD_SIZE) : new InReg(new Temp());
    }

    @Override
    public Frame newFrame(Label name, BoolList formals) {
        return null;
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
