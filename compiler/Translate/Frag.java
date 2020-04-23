package Translate;

import Canon.Canonicalization;

/**
 * An abstract code fragment. This contains
 * either a function body IR tree or a string 
 * literal. This class also functions as a singly
 * linked list.
 */
public abstract class Frag {

    public Frag next;

    public abstract void process(Canonicalization canonicalization, FragProcessor fragProcessor);

}
