import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Driver {
	private final static int CODE_LENGTH = 3;
	private static String options = "Commands :\n(f)orward <command> <color> <code> \n(b)ackward <code>\nexit";

	private static int getStackType() {
		System.out.println(" 1 StackJava\n 2 StackList\n 3 StackArray");
		Scanner scanner = new Scanner(System.in);
		return scanner.nextInt();
	}

	private static boolean validateCode(String input) {
		input = input.toLowerCase();
		if(input.contains("exit")) { 
			return true; 
		}

		String[] splitStr = input.trim().split("\\s+");

		//forward command validation
		if(splitStr[0].equals("f") || splitStr.equals("forward")) {
			if(splitStr == null || splitStr.length < 3 || splitStr.length > 3) {
				System.out.println("Not a valid Command");
				return false;
			}

			if(!Driver.isValidCode(splitStr[2])) return false;
		} else {
			return true;
		}

		if(splitStr[0].equals("b") || splitStr[0].equals("backward")) {
			if(splitStr == null || splitStr.length < 2 || splitStr.length > 2) {
				System.out.println("Not a valid Command");
				return false;
			}

			if(!Driver.isValidCode(splitStr[1])) return false;
		} else {
			return true;
		}

		return false;
	}

	private static boolean isValidCode(String code) {
		if(code.length() != Driver.CODE_LENGTH) {
			System.out.println("Not a valid Code");
			return false;
		}
		return true;
	}

	private static String getInput() {
		Scanner scanner = new Scanner(System.in);
		System.out.println(Driver.options);
		String input = scanner.nextLine();
	   	
		if(!Driver.validateCode(input)) {
			input = null;
		}

		return input;
	}

	public static void toggleOptions() {
		options = "Commands :\n(b)ackward <code>\n exit";
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


		Neena neena = new Neena(map, Driver.getStackType());
		String input  = Driver.getInput();
		while(input != null) {
			if(input.contains("exit")) {
				neena.dumpStack();
				break;
			}

			String move = neena.playGame(input.trim().split("\\s+"));
			System.out.println("\f");
			System.out.println(move);
			
			if(neena.isWinner()) {
				System.out.println("You Win");
				break;
			}
			input = Driver.getInput();
		}
	}
}
