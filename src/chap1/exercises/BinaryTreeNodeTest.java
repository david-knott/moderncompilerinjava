package chap1.exercises;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class BinaryTreeNodeTest {

	public BinaryTreeNode getTree() {
		/*
        var tree = new Tree(null, "d", null);
        tree = Tree.insert("a", tree);
        tree = Tree.insert("v", tree);
        tree = Tree.insert("i", tree);
        tree = Tree.insert("k", tree);
        
        return tree;
        */
         	return new BinaryTreeNode(null, "d", null)
        		.insert("a")
        		.insert("v")
        		.insert("i")
        		.insert("k")
        		.insert("n")
        		.insert("o")
        		.insert("t");
	}	

	@Test
	void test() {
		var t7 = getTree();
		final SimpleDisplay simpleDisplay = new SimpleDisplay();
    	simpleDisplay.insertRoot(t7.key);

        t7.accept(new TreeVisitor() {
        	//what level of recursion are we at ?
        	int level = 0;
			@Override
			public void visit(BinaryTreeNode tree) {
				var currentNodeValue = tree.key;
				//if present, insert left node value at level n + 1
				var leftNodeValue = tree.left != null ? tree.left.key : null;
				if(leftNodeValue != null) {
					simpleDisplay.insertLeft(currentNodeValue, leftNodeValue, level + 1);
				}
				
				//if present, insert right node value at level n + 1
				var rightNodeValue = tree.right!= null ? tree.right.key : null;
				if(rightNodeValue != null)
					simpleDisplay.insertRight(currentNodeValue, rightNodeValue, level + 1);
				
			}
			@Override
			public void decLevel() {
				level--;	
			}
			@Override
			public void incLevel() {
				level++;		
			}
        });   
     //   simpleDisplay.display();
	}
	
	@Test
	void testWidth() {
		var t7 = getTree();
		System.out.println(t7);
		System.out.println(t7.getMinDepth());
		//starting at the root node t7, get its  left and right items
		//var left = t7.left;
		//var right = t7.right;
		
		//so at this level we have 2 items
	
	}

}
