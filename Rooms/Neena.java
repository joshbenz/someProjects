import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Neena {
	private IStack visitedRooms;
	private RoomGraph map;
	private Room currRoom;
	private boolean foundGoldRoom;

	public Neena(List<String> m, int stack) {
		map 			= new RoomGraph(m);
		currRoom 		= new Room();
		foundGoldRoom	= false;

		switch(stack) {
			case 1: visitedRooms = new StackJava(); 	break;
			case 2: visitedRooms = new StackList(); 	break;
			//case 3: visitedRooms = new StackArray(); 	break;
			default: visitedRooms = new StackJava();	break;
		}
	}

	public boolean isWinner() {
		return (foundGoldRoom && visitedRooms.empty());
	}

	public String playGame(String[] input) {
		String cmd = input[0];
		if(cmd.equals("f") || cmd.equals("forward")) {
			if(foundGoldRoom) return "Nope...Find your way back";

			if(enterRoom(new Room(input[1], input[2]))) {
				return "Entered the " + input[1] + " room";
			} else {
				return "No doorway to the " + input[1] + " room";
			}
		}

		if(cmd.equals("b") || cmd.equals("backward")) {
			if(leaveRoom(input[1])) {
				return "Leaving current room.\n Entered the " + currRoom.getColor() + " room"; 
			} else {
				return "Invalid Code!";
			}
		}

		return null;
	}

	private boolean enterRoom(Room nextRoom) {
		if(visitedRooms.empty()) {
			currRoom = new Room("green", "000");
		}

		if(!visitedRooms.empty()) {
			if(visitedRooms.peek().getCode().equals(nextRoom.getCode())) {
				return false;
			}
		}

		if(map.canEnterRoom(currRoom, nextRoom)) {
			visitedRooms.push(new Room(currRoom.getColor(), currRoom.getCode()));
			currRoom = new Room(nextRoom);

			if(currRoom.getColor().equals("gold")) {
				foundGoldRoom = true;
				Driver.toggleOptions();
			}
			return true;
		}
		return false;
	}

	private boolean leaveRoom(String code) {
		if(visitedRooms.empty()) {
			return false;
		}

		if(map.canLeaveRoom(code, visitedRooms.peek())) {
			Room prevRoom = visitedRooms.pop();
			currRoom = new Room(prevRoom);
			return true;
		}
		return false;
	}
	
	public void dumpStack() {
		IStack tmp = new StackJava();

		if(visitedRooms.empty()) return;

		while(!visitedRooms.empty()) {
			tmp.push(visitedRooms.pop());
		}
		
		System.out.println("Stack contents:");

		while(!tmp.empty()) {
			Room room = tmp.pop();
			System.out.println(room.getColor());
			visitedRooms.push(room);
		}
	}
}
