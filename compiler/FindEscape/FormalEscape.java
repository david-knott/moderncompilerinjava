package FindEscape;

import Absyn.VarDec;

class FormalEscape extends Escape {
    Absyn.VarDec fl;

    FormalEscape(int d, VarDec f) {
        depth = d;
        fl = f;
        fl.escape = false;
    }

    void setEscape() {
        fl.escape = true;
    }
}