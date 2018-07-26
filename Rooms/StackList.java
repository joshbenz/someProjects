import java.util.NoSuchElementException;


public class StackList implements IStack {
	private int size;
	private Node head;

	public StackList() {
		head = null;
		size = 0;
	}

	public boolean empty() {
		return head == null;
	}

	public void push(Room r) {
		Node curr = head;
		head = new Node();
		head.data = r;
		head.next = curr;
		size++;
	}

	public Room pop() {
		if(empty()) throw new NoSuchElementException(); 
		
		Room r = head.data;
		head = head.next;
		size--;
		return r;
		
	}

	public Room peek() {
		if(empty()) throw new NoSuchElementException();
		return head.data;
	}


	private class Node {
		private Room data;
		private Node next;
	}
}
