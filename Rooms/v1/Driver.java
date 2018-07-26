import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Driver {
	final static int CODE_LENGTH = 3;

	public static boolean validateCode(String input) {
		input = input.toLowerCase();
		if(input.contains("exit")) { 
			return true; 
		}

		String[] splitStr = input.trim().split("\\s+");

		if(splitStr == null || splitStr.length < 3 || splitStr.length > 3) {
			return false;
		}

		if(splitStr[2].length() != Driver.CODE_LENGTH) {
			System.out.println("Not a Valid Code");
			return false;
		} else {
			return true;
		}
	}

	public static String getInput() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please use in the form <command> <color> <code> \n(f)orward (b)ackward exit");
		String input = scanner.nextLine();
	   	
		if(!Driver.validateCode(input)) {
			input = null;
		}

		return input;
	}

	public static void main(String[] args) {
		List<String> map = new ArrayList<>();
		map.add("green, brown pink blue");
		map.add("pink, green brown blue");
		map.add("brown, pink green red");
		map.add("blue, green pink yellow");
		map.add("red, brown yellow");
		map.add("yellow, red blue gold");
		map.add("gold, yellow");

		Neena neena = new Neena(map);
		String input  = Driver.getInput();
		while(input != null) {
			if(input.contains("exit")) {
				neena.dumpStack();
				break;
			}

			neena.playGame(input.trim().split("\\s+"));
			input = Driver.getInput();
		}
	}
}
