package ErrorMsg;

import java.io.PrintStream;

public class ErrorMsg {
    private LineList linePos = new LineList(-1, null);
    private int lineNum = 1;
    private String filename;
    public boolean anyErrors;
    public PrintStream out;

    public ErrorMsg(String f, PrintStream out) {
        filename = f;
        this.out = out;
    }

    public void newline(int pos) {
        lineNum++;
        linePos = new LineList(pos, linePos);
    }

    public void add(CompilerError compilerError){
        anyErrors = true;
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
        this.out.println(filename + ":" + sayPos + ": " + msg);
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
