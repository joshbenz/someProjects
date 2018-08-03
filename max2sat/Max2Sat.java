import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


public class Max2Sat {
static int maxTrueSoFar = 0;
private static List<Integer> listOfVars = new ArrayList<>();
private static List<Boolean> assignmentsTable = new ArrayList<>();
private static List<Literal> assignmentsTableGreedy = new ArrayList<>();


	public static void main(String[] args) {
		List<Clause> clauses = initFromFile("1.txt");

		greedyLocalSearch(clauses);
		System.out.println("NextPhase");

		bruteForce(clauses);
		System.out.println("DONE!");
	}
	
	public static void greedyLocalSearch(List<Clause> clauses) {
		List<Literal> tableWithProbabilities  = Arrays.asList(new Literal[Collections.max(listOfVars)]);
		maxTrueSoFar = 0;
		Random random = new Random();
		
		for(int i=0; i<listOfVars.size(); i++) { 
			Literal literal = new Literal(listOfVars.get(i), true);
			literal.setProbability(calculateProbabilities(literal, clauses));
			tableWithProbabilities.set(listOfVars.get(i)-1,literal); 
		}
		
		int ineffective = 0;
		tableWithProbabilities = randomizeTruthTable(tableWithProbabilities);

		long startTime =System.currentTimeMillis();
		long currentTime =startTime;
		
		while(currentTime<startTime+20000) {
			currentTime =System.currentTimeMillis();
			
			if(ineffective % 10 == 0) { tableWithProbabilities = randomizeTruthTable(tableWithProbabilities);}

			int numTrue = 0;
			for(Clause clause : clauses) {
				clause.setLiteralsState(tableWithProbabilities.get(Math.abs(clause.getFirstLiteral().getSubscript())-1).getState(), 
						tableWithProbabilities.get(Math.abs(clause.getSecondLiteral().getSubscript())-1).getState());
		
				if(clause.performExp() == true) { numTrue++; }
			}
			if(numTrue > maxTrueSoFar) { 
				maxTrueSoFar = numTrue; 
				ineffective = 0;
				String bestAssignment = new String();
				assignmentsTableGreedy = tableWithProbabilities;
				for(int k=0; k< listOfVars.size(); k++) {
		        	if(assignmentsTableGreedy.get(listOfVars.get(k)-1).getState() == true) {
		        		bestAssignment = bestAssignment + listOfVars.get(k);
		        	}
				}
				
				System.out.println(maxTrueSoFar+" clauses satisfyied! "+"Best Assignment so far:" + bestAssignment);	
			} else {
				ineffective++;
				
			}
			Collections.sort(clauses); Collections.reverse(clauses);
			int count = 1;
			
			for(Clause clause : clauses) {
				if(clause.performExp() == true) {
					break;
				}
				count++;
			}
			
			int literalToFlip = random.nextInt(count);
			Clause tmpClause = clauses.get(literalToFlip);
			
			if(tmpClause.getFirstLiteral().getProbability() < tmpClause.getSecondLiteral().getProbability()) {
				tableWithProbabilities.get(Math.abs(tmpClause.getFirstLiteral().getSubscript()-1)).flipState();
			} else {
				tableWithProbabilities.get(Math.abs(tmpClause.getSecondLiteral().getSubscript()-1)).flipState();
			}
		}
	}
	
	public static List<Literal> randomizeTruthTable(List<Literal> table) {
		Random random = new Random();
		boolean state = true;
		
		for(int i=0; i<listOfVars.size(); i++) {
			int rand = random.nextInt(2);
			if(rand == 0) { state = false; }
			table.get(listOfVars.get(i)-1).setState(state);
		}
		return table;
	}
	
	public static float calculateProbabilities(Literal literal, List<Clause> clauses) {
		int count = 0;
		
		for(Clause c : clauses) {
			if(Math.abs(c.getFirstLiteral().getSubscript())  == Math.abs(literal.getSubscript()) ||
					Math.abs(c.getSecondLiteral().getSubscript()) == Math.abs(literal.getSubscript())) {
				count++; 
			}
		}
		return  ((float)count / clauses.size());
	}
	
	public static void bruteForce(List<Clause> clauses) {
		maxTrueSoFar = 0;
		List<Boolean> tmpAssignments = assignmentsTable;
		int rows = (int) Math.pow(2,listOfVars.size());
        
		for (int i=0; i<rows; i++) {
			String table = new String();

            for (int j=listOfVars.size()-1; j>=0; j--) {
            	table = table + (i/(int) Math.pow(2, j))%2 + " ";
            }
            
            String[] tableArray = table.trim().split(" ");
        	int counter = 0;

        	for(String s : tableArray) {
            	boolean value = true;
            	if(s.contains("1")) { value = true; }
            	if(s.contains("0")) { value = false; }
            	tmpAssignments.set(listOfVars.get(counter)-1, value);
            	counter++;
            } 
            
            int numTrue = 0;
			for(Clause clause : clauses) {
				clause.setLiteralsState(tmpAssignments.get(Math.abs(clause.getFirstLiteral().getSubscript())-1), 
						tmpAssignments.get(Math.abs(clause.getSecondLiteral().getSubscript())-1));
			
				if(clause.performExp() == true) {
					numTrue++;
				}
			}
			if(numTrue > maxTrueSoFar) {
				maxTrueSoFar = numTrue;
				assignmentsTable = tmpAssignments;
				String bestAssignment = new String();
				for(int k=0; k< listOfVars.size(); k++) {
		        	if(assignmentsTable.get(listOfVars.get(k)-1) == true) {
		        		bestAssignment = bestAssignment + listOfVars.get(k);
		        	}
				}
				
				System.out.println(maxTrueSoFar+" clauses Satisfyied! "+"Best Assignment so far:" + bestAssignment);			
			}        
		}
	}
	
	
	public static List<Clause> initFromFile(String filename) {
		List<Clause> clauses = new ArrayList<>();
		HashSet<Integer> assignmentTable = new HashSet<>();
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        String[] vars = line.trim().split(" ");
		        
		        assignmentTable.add(Math.abs(Integer.parseInt(vars[0])));
		        assignmentTable.add(Math.abs(Integer.parseInt(vars[1])));
		        clauses.add(new Clause(new Literal(Integer.parseInt(vars[0]), true),
		        		new Literal(Integer.parseInt(vars[1]), true))); //assumes that we always only have two literals
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Integer> assignmentList = new ArrayList<>(assignmentTable);

		for(int i=0; i<=Collections.max(assignmentList); i++) { assignmentsTable.add(false); }
		for(Integer n : assignmentList) { listOfVars.add(n); }
		
		return clauses;
	}
}

/********* Other Classes ********/
class Clause implements Comparable<Clause>{
	private Literal firstLiteral;
	private Literal secondLiteral;
	
	public Clause(Literal first, Literal second) {
		firstLiteral 	= first;
		secondLiteral 	= second;
	}
	
	public Literal getFirstLiteral() { return firstLiteral; }
	public Literal getSecondLiteral() { return secondLiteral; }
	public void setLiteralsState(boolean first, boolean second) {
		firstLiteral.setState(first);
		secondLiteral.setState(second);
	}
	public void flipLiteralState(Literal literal) { literal.setState(!(literal.getState()));}
	
	public boolean performExp() {
		boolean first = firstLiteral.getState(), second = secondLiteral.getState();
		
		if(firstLiteral.getSubscript() < 0) { first = !first;   }
		if(secondLiteral.getSubscript() < 0){ second = !second; }
		
		return(first || second);
	}
	
	public String toString() {
		return firstLiteral.toString() + ", " + secondLiteral.toString() + "\n";
	}

	@Override
	public int compareTo(Clause clause) {
		if(clause.performExp()) { return 1; }
		return 0;
	}
}

class Literal {
	private int subscript;
	private boolean state;
	private float probability;
	
	public Literal(int subscript, boolean state) {
		this.subscript = subscript;
		this.state = state;
	}
	
	public Literal(int subscript, boolean state, float probability) {
		this.subscript = subscript;
		this.state = state;
		this.probability = probability;
	}
	
	public int getSubscript() { return subscript; }
	public boolean getState() { return state; 	  }
	public void setSubscript(int subscript) { this.subscript = subscript; }
	public void setState(boolean state) 	{ this.state = state;		  }
	public float getProbability() { return probability; }
	public void setProbability(float prob) { probability = prob; }
	public void flipState() { state = !state; }
	
	public String toString() { return "x" + subscript + "=" + state; }
}
