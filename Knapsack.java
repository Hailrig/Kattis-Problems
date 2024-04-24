import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// Problem at https://open.kattis.com/problems/knapsack

public class Knapsack { //This was designed for Kattis, and thus has no error handling or input validation, as inputs were reliable and speed is important
	static Scanner scanner = new Scanner(System.in);
	static int maxWeight, totalNumberOfObjects;
	static int[][] objects; //list of all objects, second field is value, weight, and original index
	static int[][] valueTable; //list of all item evaluation by weight and value, per dynamic programming
	static ArrayList<Integer> output = new ArrayList<Integer>();
	public static void main(String[] args) {
		while (scanner.hasNextLine()) {
			getInput(); //aquire all user input for the next problem set, create object array
			sortByWeight(); //sort object array by weight
			buildValueTable(); //use dynamic programming practices to build the value table
			findAnswer(); //use the value table to calculate selected items
			printOutput(); //print out used items
		}
		scanner.close();
	}
	
	public static void getInput() { //aquire all of the input for the next problem set, leaving the scanner open for future problems
		maxWeight = scanner.nextInt();
		totalNumberOfObjects = scanner.nextInt();
		
		objects = new int[totalNumberOfObjects][3];
		for (int i = 0; i < totalNumberOfObjects; i++) {
			objects[i][0] = scanner.nextInt();
			objects[i][1] = scanner.nextInt();
			objects[i][2] = i;
		}
		scanner.nextLine();
	}
	
	public static void sortByWeight() { //use the Comperator class to sort the array by weight. The third value of the second field stores the original index, for outputs
		Arrays.sort(objects, (a, b) -> Integer.compare(a[1], b[1]));
	}
	
	public static void buildValueTable() { //uses dynamic programming practices to build the value table. at any position [i][j], it shows the maximum amount of value achievable using only the i above items and j remaining weight
		valueTable = new int[totalNumberOfObjects][maxWeight+1];
		
		for (int i = 0; i < totalNumberOfObjects; i++) {
			for (int j = 0; j < maxWeight+1; j++) {
				if (j < objects[i][1]) {
					if (i == 0)
						valueTable[i][j] = 0;
					else 
						valueTable[i][j] = valueTable[i-1][j];
				} else {
					if (i == 0)
						valueTable[i][j] = objects[i][0];
					else
						valueTable[i][j] = Math.max(objects[i][0] + valueTable[i-1][j-objects[i][1]], valueTable[i-1][j]);
				}
			}
		}
	}
	
	public static void findAnswer() { //iterates through the value table to build an answer. For [i][j], if the cell above on the value table is the same value, current item not used, otherwise current item used. decrement item by 1 and weight by item weight.
		int currentWeight = maxWeight;
		int currentItem = totalNumberOfObjects-1;
		while(currentItem >= 0 && currentWeight >= objects[0][1]) {
			if (currentItem != 0) {
				if (valueTable[currentItem][currentWeight] == valueTable[currentItem-1][currentWeight]) {
					currentItem--;
					continue;
				}
			}
			output.add(objects[currentItem][2]);
			currentWeight -= objects[currentItem][1];
			currentItem--;
		}
	}
	
	public static void printOutput() { //print user output
		System.out.println(output.size());
		for (int i = 0; i < output.size(); i++) {
			System.out.print(output.get(i) + " ");
		}
		System.out.println("");
		output = new ArrayList<Integer>();
	}
}
