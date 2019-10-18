package chap1.exercises;

/**
 * Tree is really a node that has a pointer to a left and right node.
 * @author msapr
 *
 */
public class BinaryTreeNode {
	 
	public static BinaryTreeNode insert(String key, BinaryTreeNode t) {
		//if the tree is null, return a new tree with no left or right links, this is a leaf node or a root node
        if (t == null)
            return new BinaryTreeNode(null, key, null);
        else if (key.compareTo(t.key) < 0) //our key is less than the current nodes key, insert key to left
            return new BinaryTreeNode(insert(key, t.left), t.key, t.right);
        else if (key.compareTo(t.key) > 0) //our key is greater than the current nodes key, insert key to right
            return new BinaryTreeNode(t.left, t.key, insert(key, t.right));
        else //our key is equal to the current tree key, return a new copy of the tree with same data t
            return new BinaryTreeNode(t.left, key, t.right); 
    }
	
    BinaryTreeNode left;
    String key;
    BinaryTreeNode right;
    
    BinaryTreeNode(BinaryTreeNode l, String k, BinaryTreeNode r) {
        left = l;
        key = k;
        right = r;
    }
    
    public BinaryTreeNode insert(String key) {
    	return BinaryTreeNode.insert(key, this);
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
    
    public int getWidth(int level) {
        return BinaryTreeNode.getWidth(this, level);
    }
    
    public int getMaxWidth(int level) {
        return BinaryTreeNode.getMaxWidth(this);
    }
    
    /**
     * The function works by recursing down to the level we are interested in and suming its nodes
     * Say we want level 3 of a tree with 3 or more levels, it starts at root
     * -> recurse to next level ( level local var now equal to 2 ),
     * -> recurse to next level ( level var now equal to 1 )
     * -> function returns 1 if node exists, or zero if null	
     * We are using level in our function call, but in decreases as the actual
     * tree level increases. 
     * By doing it this way we only collapse the recursion tree if we are at the
     * level we are interested in.
     * @param tree
     * @param level - this is used as a terminating condition and isn't the level we are at in the tree
     * @return
     */
    public static int getWidth(BinaryTreeNode tree, int level) {
    	if(tree == null)
    		return 0;
    	if(level == 1) //at root node level, only one
    		return 1;
    	//recurse to level and sum all the things
    	int l = getWidth(tree.left, level - 1);
    	int r = getWidth(tree.right, level - 1);
    	return l + r;
    }
    
    public static int getMaxWidth(BinaryTreeNode tree) {
    	var res = 0;
    	for(int i = 1; i < BinaryTreeNode.getMaxDepth(tree); i++) {
    		res = Math.max(res, BinaryTreeNode.getWidth(tree, i));
    	}
    	return res;
    }
    
    /**
     * Returns max depth of this tree. 
     * @return
     */
    public int getMaxDepth() {
    	return getMaxDepth(this);
    }
    
    /**
     * Returns min depth of this tree. 
     * @return
     */
    public int getMinDepth() {
    	return getMinDepth(this);
    }
    
    /**
     * Recursive function to find max depth of tree from this node.
     * For each node ( tree ) moves to its left node, right node
     * @param tree
     * @return
     */
	public static int getMaxDepth(BinaryTreeNode tree) {
		if(tree == null)
			return 0;
		int l = getMaxDepth(tree.left);
		int r = getMaxDepth(tree.right);
		return 1 + Math.max(l, r);
	}


    /**
     * Recursive function to find max depth of tree from this node.
     * For each node ( tree ) moves to its left node, right node
     * @param tree
     * @return
     */
	public int getMinDepth(BinaryTreeNode tree) {
		if(tree == null)
			return 0;
		int l = getMaxDepth(tree.left);
		int r = getMaxDepth(tree.right);
		return 1 + Math.min(l, r);
	}

    public String toString(){
        String s = "[ Tree ";
        s+= " key=" + key;
        if(this.left != null)
            s+= " left=" + this.left.toString();
        if(this.right != null)
            s+= " right=" + this.right.toString();
        s+= " ] ";
        return s;
    }
}