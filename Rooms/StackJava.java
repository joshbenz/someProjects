import java.util.Stack;

public class StackJava implements IStack {
	private Stack<Room> stack;

	public StackJava() {
		stack = new Stack<>();
	}

	public void push(Room r) {
		stack.push(r);
	}

	public Room pop() {
		return stack.pop();
	}

	public Room peek() {
		return stack.peek();
	}

	public boolean empty() {
		return stack.empty();
	}
}
