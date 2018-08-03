public class Tree {
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
