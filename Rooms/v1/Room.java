import java.util.ArrayList;
import java.util.List;

public class Room {
	private String color;
	private String code;

	public Room(String color, String code, List<Room> rooms) {
		this.color 	= color;
		this.code 	= code;
	}

	public Room(String color, String code) {
		this.color 	= color;
		this.code 	= code;
	}

	public Room() {
		color = new String();
		code = new String();
	}

	public String getColor() 			{ return color; }
	public String getCode()				{ return code; 	}
	public void setColor(String c)		{ color = c; }
	public void setCode(String c)		{ code 	= c; }

}
