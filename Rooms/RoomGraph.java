import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class RoomGraph {
	private HashMap<String, List<EdgeNode>> graph;

	public RoomGraph(List<String> input) {
		graph = new HashMap<String, List<EdgeNode>>();

		//build graph
		for(int i=0; i<input.size(); i++) { 
			String[] parsed = input.get(i).trim().split(",");
			String room = parsed[0];
			String[] doorways = parsed[1].trim().split("\\s+");
			
			graph.put(room, new ArrayList<EdgeNode>()); //add vertex to graph

			for(String doorway : doorways) { //add edges between vertices
				graph.get(room).add(new EdgeNode(room, doorway)); 
			}
		}
	}

	public boolean canEnterRoom(Room room, Room nextRoom) {
		return isEdge(room.getColor(), nextRoom.getColor());	
	}

	public boolean canLeaveRoom(String code, Room nextRoom) {
		return nextRoom.getCode().equals(code);
	}

	private boolean isEdge(String source, String next) {
		if(graph.containsKey(source)) {
			List<EdgeNode> edges = graph.get(source);
			for(EdgeNode edge : edges) {
				if(edge.getSink().equals(next)) {
					return true;
				}
			}
		}
		return false;
	}


	private class EdgeNode {
		String vertex1, vertex2;
	
		public EdgeNode(String v1, String v2) {
			vertex1 = v1;
			vertex2 = v2;
		}	

		public String getSource() 	{ return vertex1; }
		public String getSink()		{ return vertex2; }
	}
}

