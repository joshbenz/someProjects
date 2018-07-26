import java.util.Stack;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Neena {
	Stack<Room> visitedRooms;
	RoomMap map;
	Room currRoom;

	public Neena(List<String> m) {
		visitedRooms = new Stack<Room>();
		map = new RoomMap(m);
		currRoom = new Room();
	}

	public void playGame(String[] input) {
		if(visitedRooms.empty()) {
			currRoom.setColor("green");	
			currRoom.setCode("000");
			visitedRooms.push(new Room("green", "000"));
		}

		if(map.canEnterRoom(currRoom, input[1])) {
			visitedRooms.push(new Room(input[1], input[2]));

			currRoom.setColor(input[1]);
			currRoom.setCode(input[2]);
		} else {
			System.out.println("You moron");
		}


	}
	
	public void dumpStack() {
		while(!visitedRooms.empty()) {
			System.out.println(visitedRooms.pop().getColor());
		}
	}
}
