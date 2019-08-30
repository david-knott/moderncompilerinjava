package chap1.exercises;

import java.util.ArrayDeque;
import java.util.Deque;

public class BinaryTreeVisualiser {
	
	private BBinaryTreeNode binaryTreeNode;
	
	public BinaryTreeVisualiser(BBinaryTreeNode binaryTreeNode) {
		this.binaryTreeNode = binaryTreeNode;
		//work out the extents of the graph by examining the tree.
	}
	class NodeInformation
	{
		public BBinaryTreeNode bBinaryTreeNode;
		public int x;
		public int y;
		
		public NodeInformation(BBinaryTreeNode bBinaryTreeNode, int x, int y) {
			this.bBinaryTreeNode = bBinaryTreeNode;
			this.x = x;
			this.y = y;
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return this.x + "," + this.y + ":" + this.bBinaryTreeNode.key;
		}
	}
public void visualise() {
		
		//using max level of tree work out bounds of plot area
		var maxDepth = binaryTreeNode.getMaxDepth() ;
		var xMax = (int)(Math.pow(2, maxDepth) -2 );
		var yMax = maxDepth;
		var display = new String[yMax * 2 + 1][xMax * 5 + 1];
		//initialize the display with empty space
		var deq = new ArrayDeque<NodeInformation>();
		var ni = new NodeInformation(this.binaryTreeNode, xMax / 2, maxDepth - 1 );
		display[ni.y * 2][ni.x * 5] = ni.bBinaryTreeNode.key;
		System.out.println(ni);
		deq.addFirst(ni);
		do {
			//for each current item on the queue, get its children and store them at the end of the queue
			var size = deq.size();
			for(var i = 0; i < size; i++) {
				//get item from queue
				var nit = deq.pollFirst();
				
				var bn = nit.bBinaryTreeNode;
				//current node has left child
				if(bn.left != null)	{
					//get position of the left node x
					ni  = new NodeInformation(bn.left, nit.x - (int)Math.pow(2, nit.y - 1), 1 * (nit.y - 1));
					display[ni.y * 2][ni.x * 5] = ni.bBinaryTreeNode.key;
					display[ni.y * 2 + 1][ni.x * 5 + (nit.x - ni.x) * 5 / 2 ] = "/";		
					deq.addLast(ni);
					System.out.println("left " + ni);
				}
				//current node has a right child
				if(bn.right != null) {
					ni  = new NodeInformation(bn.right, nit.x + (int)Math.pow(2, nit.y - 1),  1 * (nit.y - 1));
					display[ni.y * 2][ni.x * 5] = ni.bBinaryTreeNode.key;
					display[ni.y * 2 + 1][nit.x * 5 + (ni.x - nit.x) * 5 / 2 ] = "\\";	
					deq.addLast(ni);
					System.out.println("right " + ni);
				}
			}

		}while(deq.size() > 0);
		
		for(int i = display.length - 1; i >= 0; i--) {
			for(int j = 0; j < display[i].length; j++) {
				System.out.print(display[i][j] != null ? display[i][j] : " ");
			}
			System.out.println();
		}
	}

	public void visualise2() {
	
	//using max level of tree work out bounds of plot area
	var maxDepth = binaryTreeNode.getMaxDepth() ;
	var xMax = (int)(Math.pow(2, maxDepth) -2 );
	var yMax = maxDepth;
	var display = new String[yMax * 2][xMax];
	//initialize the display with empty space
	var deq = new ArrayDeque<NodeInformation>();
	var ni = new NodeInformation(this.binaryTreeNode, xMax / 2, maxDepth - 1 );
	display[ni.y * 2][ni.x] = ni.bBinaryTreeNode.key;
	System.out.println(ni);
	deq.addFirst(ni);
	do {
		//for each current item on the queue, get its children and store them at the end of the queue
		var size = deq.size();
		for(var i = 0; i < size; i++) {
			//get item from queue
			var nit = deq.pollFirst();
			//System.out.println("nit = " + nit);
			var bn = nit.bBinaryTreeNode;
			//current node has left child
			if(bn.left != null)	{
				//get position of the left node x
				ni  = new NodeInformation(bn.left, nit.x - (int)Math.pow(2, nit.y - 1), 1 * (nit.y - 1));
				display[ni.y * 2][ni.x] = ni.bBinaryTreeNode.key;
				display[ni.y * 2 + 1][ni.x] = "/";		
				deq.addLast(ni);
				System.out.println("left " + ni);
			}
			//current node has a right child
			if(bn.right != null) {
				ni  = new NodeInformation(bn.right, nit.x + (int)Math.pow(2, nit.y - 1),  1 * (nit.y - 1));
				display[ni.y * 2][ni.x] = ni.bBinaryTreeNode.key;
				display[ni.y * 2 + 1][ni.x] = "\\";		
				deq.addLast(ni);
				System.out.println("right " + ni);
			}
		}

	}while(deq.size() > 0);
	
	for(int i = display.length - 1; i >= 0; i--) {
		for(int j = 0; j < display[i].length; j++) {
			System.out.print(display[i][j] != null ? display[i][j] : " ");
		}
		System.out.println();
	}
}
}
