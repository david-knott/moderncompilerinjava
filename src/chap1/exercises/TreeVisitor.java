package chap1.exercises;

/**
 * Tree is really a node that has a pointer to a left and right node.
 * @author msapr
 *
 */
class Tree {
	 
	public static Tree insert(String key, Tree t) {
		//if the tree is null, return a new tree with no left or right links, this is a leaf node or a root node
        if (t == null)
            return new Tree(null, key, null);
        else if (key.compareTo(t.key) < 0) //our key is less than the current nodes key, insert key to left
            return new Tree(insert(key, t.left), t.key, t.right);
        else if (key.compareTo(t.key) > 0) //our key is greater than the current nodes key, insert key to right
            return new Tree(t.left, t.key, insert(key, t.right));
        else //our key is equal to the current tree key, return a new copy of the tree with same data t
            return new Tree(t.left, key, t.right); 
    }
	
    Tree left;
    String key;
    Tree right;
    
    Tree(Tree l, String k, Tree r) {
        left = l;
        key = k;
        right = r;
    }
    
    public Tree insert(String key) {
    	return Tree.insert(key, this);
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
    
    public static int getWidth(Tree tree, int level) {
    	//recurse to level and sum all the things
    	int l = 1 + getWidth(tree.left, level);
    	int r = 1 + getWidth(tree.right, level);
    	return l + r;
    }
    
    public static int getMaxWidth(Tree tree) {
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
	public int getMaxDepth(Tree tree) {
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
	public int getMinDepth(Tree tree) {
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

public interface TreeVisitor{
	void visit(Tree tree);
	void decLevel();
	void incLevel();
}

class SimpleDisplay
{
	private String[][] display;
	
	public SimpleDisplay() {
		display = new String[20][20];
		for(int i = 0; i < display.length; i++) {
			for(int j = 0; j < display[i].length; j++) {
				display[i][j] = " ";
			}
		}
	}
	
	public void insertLeft(String currentNodeValue, String leftNodeValue, int level) {
		var index = java.util.Arrays.asList(display[level * 2 - 2]).indexOf(currentNodeValue);
		//width needs to adjust based on the total number of items at a particular level
	//	display[level * 2 - 1][index - 2] = "/";
		//display[level * 2][index - 4] = leftNodeValue;	
	}

	public void insertRight(String currentNodeValue, String rightNodeValue, int level) {
		var index = java.util.Arrays.asList(display[level * 2 - 2]).indexOf(currentNodeValue);
		//width needs to adjust based on the total number of items at a particular level
//		display[level * 2 - 1][index + 2] = "\\";
//		display[level * 2][index + 4] = rightNodeValue;
	}
	
	public void display() {
		for(int i = 0; i < display.length; i++) {
			for(int j = 0; j < display[i].length; j++) {
				System.out.print(display[i][j]);
			}
			System.out.println();
		}
	}

	public void insertRoot(String key) {
		display[0][4] = key;
		// TODO Auto-generated method stub
	}
}

class Ex1_1 {
    
    
}