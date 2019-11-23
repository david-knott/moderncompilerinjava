package ErrorMsg;

public abstract class CompilerError {

    private final int pos;
    public CompilerError(int p){
        pos = p;
    }

    public int getPos(){
        return pos;
    }
}