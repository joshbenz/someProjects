import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class RoomMap {
	HashMap<String, String> map;

	public RoomMap(List<String> doorways) {
		map = new HashMap<>();

		for(String doorway : doorways) {
			String[] doors = doorway.trim().split(",");
			map.put(doors[0], doors[1]);
		}
	}

	public boolean canEnterRoom(Room room, String nextRoomColor) {
		boolean canEnter = false;
		if(map.containsKey(room.getColor())) {
			if(map.get(room.getColor()).contains(nextRoomColor)) {
				canEnter = true;
			}
		}
		return canEnter;
	}

	public void leaveRoom() {}
}

