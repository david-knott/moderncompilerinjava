package Translate;

import Canon.Canonicalization;
import Frame.Frame;
import Tree.StmList;

/**
 * An abstract code fragment. This contains either a function body IR tree or a
 * string literal. This class also functions as a singly linked list.
 */
public abstract class Frag {

    public Frag next;

    public abstract ProcessedFrag process(Canonicalization canonicalization);

    public abstract Frame getFrame();

    public ProcessedFrag processAll(Canonicalization canonicalization) {
        return null;
    }

}





