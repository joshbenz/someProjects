public class BTree {
	private Tree root;

	public BTree() {
		root = null;
	}

	/**
	 * node: pointer to the link connecting the subtree to the binary tree
	 * data: data to insert
	 * parent: the parent node that contains link 
	 */
	public Tree insert(Tree node, int x) {

		if(node == null) { //reached end of tree
			node = new Tree(x); //set data and link into parent
		} else {

			if(x < node.data) {
				node.left = insert(node.left, x);
			} else {
				node.right = insert(node.right, x);
			}
		}

		return node;
	}

	public void insert(int data) {
		root = insert(root, data);
	}

	private Tree search(Tree node, int x) {
		if(node == null) 		return null;
		if(node.data == x) 		return node;

		if(x < node.data) {
			return search(node.left, x);
		} else {
			return search(node.right, x);
		}
	}

	public int search(int x) {
		return search(root, x).data;
	}

	public int max() {
		return max(root).data;
	}

	private Tree max(Tree tree) {
		if(tree.right == null) {
			return tree;
		} else {
			return max(tree.right);
		}
	}

	private int sum(Tree tree) {
		if(tree == null) return 0;
		return(tree.data + sum(tree.left) + sum(tree.right));
	}

	public int sum() {
		return sum(root);
	}

	public int height() {
		return height(root);
	}

	private int height(Tree tree) {
		if(tree == null) { 
			return 0;
		} else {
			int leftHeight = height(tree.left);
			int rightHeight = height(tree.right);

			if(leftHeight > rightHeight) {
				return leftHeight + 1;
			} else {
				return rightHeight + 1;
			}
		}
	}

	private int numNodes(Tree tree) {
		int count = 1;
		if(tree == null) return 0;
		if(tree.left != null) count += numNodes(tree.left);
		if(tree.right != null) count += numNodes(tree.right);
		
		return count;
	}

	public double average() {
		return (double) sum() / (double) numNodes(root);
	}

	public void printSortedTree() {
		if(root != null)
			printSortedTree(root);
	}

	private void printSortedTree(Tree tree) {
		if(tree != null) {
			printSortedTree(tree.left);
			System.out.println(tree.data);
			printSortedTree(tree.right);
			
		}
	}

	private class Tree {
		int data;
		Tree parent, right, left;

		Tree() {
			data = -1;
			parent = null;
			right = null;
			left = null;
		}
	
		Tree(int data, Tree p) { //set data and link into parent
			parent = p;
			this.data = data;
			right = null;
			left = null;
		}
	
		Tree(int data) {
			this.data = data;
			right = null;
			left = null;
			parent = null;
		}
	}


}


