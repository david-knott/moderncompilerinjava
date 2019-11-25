package Intel;

import Frame.Access;
import Temp.Label;
import Temp.Temp;
import Util.BoolList;
import Frame.*;

    class IntelFrame extends Frame{

        IntelFrame(Label name, AccessList formals){
            base.name = name;
            base.formals = formals;
        }
    @Override
    public Frame newFrame(Label name, AccessList formals) {
        // TODO Auto-generated method stub
        return new IntelFrame(name, formals);
    }

    @Override
    public Access allocLocal(boolean escape) {
        // TODO Auto-generated method stub
        return escape ? InFrame(off) : InReg(new Temp());
    }

}

class InFrame extends Access{

    int offset;
    
    InFrame(int os){
        offset = os;   
    }
}

class InReg extends Access {
    Temp temp;
    
    
    InReg(Temp tmp){
        temp = tmp;   
    }
}
