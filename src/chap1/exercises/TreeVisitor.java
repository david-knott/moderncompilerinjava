package chap1.exercises;

class Tree {
    Tree left;
    String key;
    Tree right;
    
    Tree(Tree l, String k, Tree r) {
        left = l;
        key = k;
        right = r;
    }

    public boolean contains(String k){
        //does current tree have key ?
        if(k.equals(key))
            return true;
        //if tree has a right node, check it for the key
        if(right != null && right.contains(k))
            return true;
        //if the tree has a left node, check if for the key
        if(left != null && left.contains(k))
            return true;
        //no key.
        return false;
    }
    
    public void accept(TreeVisitor treeVisitor){
    	treeVisitor.visit(this);
        if(right != null) {
        	treeVisitor.incLevel();
            right.accept(treeVisitor);
            treeVisitor.decLevel();
        }
        if(left != null) {
        	treeVisitor.incLevel();
            left.accept(treeVisitor);
            treeVisitor.decLevel();
        }
    }

    public String toString(){
        String s = "[Tree ";
        s+= " key=" + key;
        if(this.left != null)
            s+= this.left.toString();
        if(this.right != null)
            s+= this.right.toString();
        s+= "]";
        return s;
    }
}

public interface TreeVisitor{
	void visit(Tree tree);

	void decLevel();

	void incLevel();
}

class Ex1_1 {
    
    public static Tree insert(String key, Tree t) {
        if (t == null)
            return new Tree(null, key, null);
        else if (key.compareTo(t.key) < 0)
            return new Tree(insert(key, t.left), t.key, t.right);
        else if (key.compareTo(t.key) > 0)
            return new Tree(t.left, t.key, insert(key, t.right));
        else
            return new Tree(t.left, key, t.right);
    }
}