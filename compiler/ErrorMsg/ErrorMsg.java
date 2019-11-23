package ErrorMsg;
import java.util.ArrayList;
import java.util.List;

public class ErrorMsg {
    private LineList linePos = new LineList(-1, null);
    private int lineNum = 1;
    private String filename;
    public boolean anyErrors;
    public List<String> errors;
    public List<CompilerError> compilerErrors;

    public ErrorMsg(String f) {
        filename = f;
        errors = new ArrayList<String>();
        compilerErrors = new ArrayList<CompilerError>();
    }

    public void newline(int pos) {
        lineNum++;
        linePos = new LineList(pos, linePos);
    }

    public void add(CompilerError compilerError){
        compilerErrors.add(compilerError);
    }

    public List<CompilerError> getCompilerErrors(){
        return compilerErrors;
    }

    public void error(int pos, String msg) {
        int n = lineNum;
        LineList p = linePos;
        String sayPos = "0.0";
        anyErrors = true;
        while (p != null) {
            if (p.head < pos) {
                sayPos = ":" + String.valueOf(n) + "." + String.valueOf(pos - p.head);
                break;
            }
            p = p.tail;
            n--;
        }
        errors.add(filename + ":" + sayPos + ": " + msg);
        System.out.println(filename + ":" + sayPos + ": " + msg);
    }
}

class LineList {
    int head;
    LineList tail;

    LineList(int h, LineList t) {
        head = h;
        tail = t;
    }
}
