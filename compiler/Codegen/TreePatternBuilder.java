package Codegen;


import Tree.BINOP;
import Tree.CALL;
import Tree.TEMP;


public class TreePatternBuilder {

    private TreeNode tree;

    TreePatternBuilder addRoot(TreeNode tilePart) {
        tree = null;
        tree = tilePart;
        return this;
    }

    TreePatternBuilder addChild(TreeNode tilePart) {
        tree = tree.addChild(tilePart);
        return this;
    }

    TreePatternBuilder getParent() {
        tree = tree.parent;
        return this;
    }

    TreePatternBuilder addSibling(TreeNode tilePart) {
        tree = tree.parent;
        tree = tree.addChild(tilePart);
        return this;
    }

    TreePattern build() {
        for(;tree.parent != null; tree = tree.parent);
        TreePattern tp = new TreePattern(tree);
        return tp;
    }
}