package chap1.exercises;

/**
 * Tree is really a node that has a pointer to a left and right node.
 * @author msapr
 *
 */
public class BBinaryTreeNode {
	 
	public static BBinaryTreeNode insert(String key, Object o, BBinaryTreeNode t) {
		//if the tree is null, return a new tree with no left or right links, this is a leaf node or a root node
        if (t == null)
            return new BBinaryTreeNode(null, key, o, null);
        else if (key.compareTo(t.key) < 0) //our key is less than the current nodes key, insert key to left
            return new BBinaryTreeNode(insert(key, o, t.left), t.key, t.obj, t.right);
        else if (key.compareTo(t.key) > 0) //our key is greater than the current nodes key, insert key to right
            return new BBinaryTreeNode(t.left, t.key, t.obj, insert(key, o, t.right));
        else //our key is equal to the current tree key, return a new copy of the tree with same data t
            return new BBinaryTreeNode(t.left, key, o, t.right); 
    }
	
    BBinaryTreeNode left;
    String key;
    Object obj;
    BBinaryTreeNode right;
    
    BBinaryTreeNode(BBinaryTreeNode l, String k, Object o, BBinaryTreeNode r) {
        left = l;
        key = k;
        obj = o;
        right = r;
    }
    
    public BBinaryTreeNode insert(String key, Object o) {
    	return BBinaryTreeNode.insert(key, o, this);
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
    
    public Object lookup(String k) {
    	if(k.equals(key))
            return obj;
        //if tree has a right node, check it for the key
        if(right != null && right.contains(k))
            return right.lookup(k);
        //if the tree has a left node, check if for the key
        if(left != null && left.contains(k))
            return left.lookup(k);
        //no key return null
        return null;
    }
    
    public static int getWidth(BBinaryTreeNode tree, int level) {
    	//recurse to level and sum all the things
    	int l = 1 + getWidth(tree.left, level);
    	int r = 1 + getWidth(tree.right, level);
    	return l + r;
    }
    
    public static int getMaxWidth(BBinaryTreeNode tree) {
    	return 0;
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
	public int getMaxDepth(BBinaryTreeNode tree) {
		if(tree == null)
			return 0;
		int l = 1 + getMaxDepth(tree.left);
		int r = 1 + getMaxDepth(tree.right);
		return Math.max(l, r);
	}


    /**
     * Recursive function to find max depth of tree from this node.
     * For each node ( tree ) moves to its left node, right node
     * @param tree
     * @return
     */
	public int getMinDepth(BBinaryTreeNode tree) {
		if(tree == null)
			return 0;
		int l = 1 + getMaxDepth(tree.left);
		int r = 1 + getMaxDepth(tree.right);
		return Math.min(l, r);
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