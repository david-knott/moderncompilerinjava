package Translate;

import java.io.PrintStream;

public abstract class Frag {
    public Frag next;

    public abstract void process(PrintStream out);

    /**
     * Process all fragments
     * 
     * @param out PrintStream to write to
     */
    public void processAll(PrintStream out) {
        for (Frag frag = this; frag != null; frag = frag.next) {
            frag.process(out);
        }
    }
}
