package Absyn;

import Types.Type;

abstract public class Var extends Absyn implements Typable {

    Type type;
    /**
     * Pointer to the definition of this variable.
     */
    public Absyn def;

    public Type getType() {
        return this.type;
    }

	public void setType(Type type) {
        this.type = type;
	}
}
