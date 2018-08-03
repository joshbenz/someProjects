public class Driver {
	public static void main(String[] args) {
		int[] test = {50, 30, 60, 10, 80, 55, 40};
		BTree binaryTree = new BTree();

		for(int i=0; i<test.length; i++) {
			binaryTree.insert(test[i]);
		}

		binaryTree.printSortedTree();
		System.out.println("Search for 30::::" + binaryTree.search(30));
		System.out.println("Max value is " + binaryTree.max());
		System.out.println("Height of tree: " + binaryTree.height());
	}
}
