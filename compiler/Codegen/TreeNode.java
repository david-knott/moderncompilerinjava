package Codegen;

import java.util.Hashtable;

import Tree.Exp;
import Tree.Stm;

abstract class TreeNode {

    private String named;
    public TreeNode child;
    public TreeNode next;
    public TreeNode parent;

    public TreeNode() {
    }

    public TreeNode(String named) {
        this.named = named;
    }

    public String getNamed() {
        return this.named;
    }

    public TreeNode addChild(TreeNode tilePart) {
        if (child == null) {
            child = tilePart;
            child.parent = this;
            return child;
        } else {
            var c = child;
            for (; c.next != null; c = c.next)
                ;
            c.next = tilePart;
            c.next.parent = this;
            return c.next;
        }
    }

    public abstract boolean isMatch(Exp exp);

    public abstract boolean isMatch(Stm stm);

    public abstract Tree.ExpList kids(Stm stm);

    public abstract Tree.ExpList kids(Exp exp);

    public void captureNamedMatch(Hashtable<String, Object> matches, Object match) {
        matches.put(named, match);
    }
}