package Intel;

import Frame.Access;
import Temp.Label;
import Temp.Temp;
import Util.BoolList;
import Frame.*;

class IntelFrame extends Frame{

    @Override
    public Frame newFrame(Label name, BoolList formals) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Access allocLocal(boolean escape) {
        // TODO Auto-generated method stub
        return null;
    }

}

class InFrame extends Access{

    int offset;
}

class InReg extends Access {
    Temp temp;
}