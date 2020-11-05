package Absyn;

import Types.Type;
import Util.Assert;

abstract public class Exp extends Absyn implements Typable {
    Type type;

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        Assert.assertNotNull(type);
        this.type = type;
    }
}
