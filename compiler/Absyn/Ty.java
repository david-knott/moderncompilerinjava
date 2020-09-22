package Absyn;

import Types.Type;

abstract public class Ty extends Absyn implements Typable {
    Type type;

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
