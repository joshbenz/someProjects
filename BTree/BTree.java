public class BTree {
	public Tree root;

	public BTree() {
		root = new Tree();
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

	public Tree search(Tree node, int x) {
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

	public void printTree(Tree tree) {
		if(tree != null) {
			printTree(tree.left);
			System.out.println(tree.data);
			printTree(tree.right);
		}
	}

}


