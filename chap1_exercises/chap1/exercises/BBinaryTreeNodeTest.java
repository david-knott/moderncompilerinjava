package chap1.exercises;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BBinaryTreeNodeTest {

	public BBinaryTreeNode getTree() {
     	return new BBinaryTreeNode(null, "d", "one", null)
    		.insert("a", "two")
    		.insert("v", "three")
    		.insert("i", "four")
    		.insert("k", "five")
    		.insert("n", "six")
    		.insert("o", "seven")
    		.insert("t", "eight");
	}	

	@Test
	void testLookup() {
		var tree = getTree();
		assertEquals(tree.lookup("i"), "four");
		assertEquals(tree.lookup("a"), "two");
		assertEquals(tree.lookup("t"), "eight");
		assertEquals(tree.lookup("xx"), null);
	}

}
