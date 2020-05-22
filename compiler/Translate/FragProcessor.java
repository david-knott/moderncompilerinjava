package Translate;

import java.io.PrintStream;

public class FragProcessor {
    private PrintStream printStream;

    public FragProcessor(PrintStream printStream) {
        this.printStream = printStream;
    }

    public PrintStream getOut() {
        return this.printStream;
    }


}