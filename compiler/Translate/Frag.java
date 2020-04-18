package Translate;

public abstract class Frag {
    public Frag next;

    public abstract void process(FragProcessor fragProcessor);

    /**
     * Process all fragments
     * 
     * @param out PrintStream to write to
     */
    public void processAll(FragProcessor fragProcessor) {
        for (Frag frag = this; frag != null; frag = frag.next) {
            frag.process(fragProcessor);
        }
    }
}
