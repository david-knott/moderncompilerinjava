package Codegen;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import Tree.BINOP;
import Tree.Exp;
import Tree.MOVE;
import Tree.MEM;
import Tree.Stm;
import Tree.TEMP;

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

    public String getNamed(){
        return this.named;
    }

    public TreeNode addChild(TreeNode tilePart) {
        var c = child;
        while (c != null) {
            c = c.next;
        }
        c = tilePart;
        c.parent = this;
        return c;
    }

    public abstract boolean isMatch(Exp exp);

    public abstract boolean isMatch(Stm stm);

    public void captureNamedMatch(Hashtable<String, Object>matches, Object match) {
        matches.put(named, match);
    }
}

interface TreeNodeValueFunction<T> {

    boolean compareValue(T t);
}

class ExpNode extends TreeNode {

    public ExpNode() {
    }

    @Override
    public boolean isMatch(Exp exp) {
        return true;
    }

    @Override
    public boolean isMatch(Stm stm) {
        return false;
    }
}

class TempNode extends TreeNode {

    public TempNode(String named) {
        super(named);
    }

    private TreeNodeValueFunction<TEMP> treeNodeValueFunction;

    public TempNode(TreeNodeValueFunction<TEMP> treeNodeValueFunction) {
        this.treeNodeValueFunction = treeNodeValueFunction;
    }

    @Override
    public boolean isMatch(Exp exp) {
        if (exp instanceof TEMP) {
            if (this.treeNodeValueFunction.compareValue((TEMP) exp)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMatch(Stm stm) {
        return false;
    }
}

class MemNode extends TreeNode {

    private TreeNodeValueFunction<MEM> treeNodeValueFunction;

    public MemNode(TreeNodeValueFunction<MEM> treeNodeValueFunction) {
        this.treeNodeValueFunction = treeNodeValueFunction;
    }

    @Override
    public boolean isMatch(Exp exp) {
        if (exp instanceof MEM) {
            if (this.treeNodeValueFunction.compareValue((MEM) exp)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMatch(Stm stm) {
        // TODO Auto-generated method stub
        return false;
    }
}

class MoveNode extends TreeNode {

    public MoveNode(String named) {
        super(named);
    }

    @Override
    public boolean isMatch(Exp exp) {
        return false;
    }

    @Override
    public boolean isMatch(Stm stm) {
        if (stm instanceof MOVE) {
            return true;
        }
        return false;
    }
}

class TreePattern {

    private TreeNode treeNode;
    private Hashtable<String, Object> matches;

    TreePattern(TreeNode treeNode) {
        this.treeNode = treeNode;
        matches = new Hashtable<String, Object>();
    }

    boolean traverse(Tree.Exp exp, TreeNode treeNode) {
        if (treeNode.isMatch(exp)) {
            var expChildren = exp.kids();
            for (TreeNode pt : this.getChildren(treeNode)) {
                if (!traverse(expChildren.head, pt))
                    return false;
                pt.captureNamedMatch(matches, exp);
            }
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
            var expChildren = exp.kids();
            for (TreeNode pt : this.getChildren(treeNode)) {
                if (!traverse(expChildren.head, pt))
                    return false;
                pt.captureNamedMatch(matches, exp);
            }
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
        while (parent.child != null) {
            var c = parent.child;
            list.add(c);
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

interface TreePatternMatchAction {
    void execute(TreePattern treePatter);
}

class TreePatternList {

    private Item items;

    class Item {
        Item next;
        TreePattern treePattern;
        TreePatternMatchAction action;

        public Item(TreePattern treePattern, TreePatternMatchAction action, Item next) {
            this.treePattern = treePattern;
            this.action = action;
            this.next = next;
        }
    }

    public TreePatternList add(TreePattern treePattern, TreePatternMatchAction action) {
        if(items == null){
            items = new Item(treePattern, action, null);
        } else {
            items.next = new Item(treePattern, action, null);
        }
        return this;
    }

    void match(MOVE move) {
        for(var item = items; item != null; item = item.next){
            if(item.treePattern.isMatch(move)){
                item.action.execute(item.treePattern);
            }
        }
    }

}
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
        this.getParent().addChild(tilePart);
        return this;
    }

    TreePattern build() {
        TreePattern tp = new TreePattern(tree);
        return tp;
    }
}