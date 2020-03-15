package Codegen;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import Tree.Stm;

class TreePattern {

    private TreeNode treeNode;
    private Hashtable<String, Object> matches;

    TreePattern(TreeNode treeNode) {
        this.treeNode = treeNode;
        matches = new Hashtable<String, Object>();
    }

    boolean traverse(Tree.Exp exp, TreeNode treeNode) {
        if (treeNode.isMatch(exp)) {
            var expChildren = treeNode.kids(exp);
            for (TreeNode pt : this.getChildren(treeNode)) {
                if (expChildren == null || !traverse(expChildren.head, pt))
                    return false;
                expChildren = expChildren.tail;
            }
            treeNode.captureNamedMatch(matches, exp);
            return true;
        }
        return false;
    }

    boolean traverse(Tree.Exp exp) {
        TreeNode root = this.getRoot();
        return traverse(exp, root);
    }

    boolean traverse(Stm exp, TreeNode treeNode) {
        if (treeNode.isMatch(exp)) {
            var expChildren = treeNode.kids(exp);
            for (TreeNode pt : this.getChildren(treeNode)) {
                if (expChildren == null || !traverse(expChildren.head, pt))
                    return false;
                expChildren = expChildren.tail;
            }
            treeNode.captureNamedMatch(matches, exp);
            return true;
        }
        return false;
    }

    boolean traverse(Stm exp) {
        TreeNode root = this.getRoot();
        return traverse(exp, root);
    }

    Object getNamedMatch(String named) {
        return matches.get(named);
    }

    List<TreeNode> getChildren(TreeNode parent) {
        var list = new ArrayList<TreeNode>();
        if(parent.child != null) {
            for(var c = parent.child; c != null; c = c.next) {
                list.add(c);
            }
        }
        return list;
    }

    TreeNode getRoot() {
        return treeNode;
    }

    boolean isMatch(Tree.Exp exp) {
        return traverse(exp);
    }

    boolean isMatch(Stm exp) {
        return traverse(exp);
    }
}