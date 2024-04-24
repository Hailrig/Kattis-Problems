import java.util.Scanner;
import java.util.ArrayDeque;
import java.util.Deque;

//Problem at: https://open.kattis.com/problems/chocolatechipfabrication

public class ChocolateChipFabrication { //Note, this program was designed for a Kattis problem, where the quality of input is certain and speed is important, and therefore does not do any error-handing or input validation
    static int rows, columns;
    static char[][] cookie;
    static int count = 0; //total passes through
    static Deque<int[]> queue = new ArrayDeque<>(); //ArrayDeque is used because it allows us to remove in O(1) time, which significantly speeds up the code for large inputs.
    static final int[] moveRow = {-1, 1, 0, 0};
    static final int[] moveCol = {0, 0, -1, 1};
    public static void main(String[] args) {
        
        getInput(); //aquire input from prompt, getting rows, cols, and creating cookie array
        
        initialPass(); //sweep through the whole cookie array, noting cells that can initially be changed, and adding them to a queue to check their neighbors
        
        repeatPasses(); //Breadth first search through all of the queued cells, looking for how many layers deep the tree goes
        
        System.out.print(count); //print final answer
    }
    
    public static void getInput() {
        //Acquire rows and columns parameter
        Scanner scanner = new Scanner(System.in);
        rows = scanner.nextInt();
        columns = scanner.nextInt();
        scanner.nextLine();
        
        //Take cookie provided as input into 2D character array
        cookie = new char[rows][columns];
        String temp;
        for (int i = 0; i < rows; i++) {
            temp = scanner.nextLine();
            for (int j = 0; j < columns; j++) {
                cookie[i][j] = temp.charAt(j);
            }
        }
        scanner.close();
    }
    
    public static void initialPass() {
        for (int i = 0; i < cookie.length; i++) {
            for (int j = 0; j < cookie[0].length; j++) { //scan through all values, adding cells that can currently be resolved to a queue and marking them as 'C'
                char currentValue = cookie[i][j];
                if (currentValue != '-') {
                    if (checkNeighbors(i, j)) {
                        cookie[i][j] = 'C';
                        int[] tempArray = {i, j};
                        queue.add(tempArray);
                    }
                }
            }
        }
    }
    
    public static void repeatPasses() { //repeatedly sweep through the queue, resolving all layers of the search tree horizontally - effectively breadth first search
        while (!queue.isEmpty()) {
            count++;
            int tempSize = queue.size();
            for (int i = 0; i < tempSize; i++) {
                int[] removed = queue.remove();
                markNeighbors(removed[0], removed[1]);
            }
        }
    }
    
    public static boolean checkNeighbors(int i, int j) { //check to see if a cell has an X or C on all sides, if not, mark as C and add to queue. Used in the initial pass. Return true if it can be marked, false if not
        if (i == 0 || i == rows-1 || j == 0 || j == columns-1)
            return true;
        return !(cookie[i+1][j] != '-' && cookie[i-1][j] != '-' && cookie[i][j+1] != '-' && cookie[i][j-1] != '-');

    }
    
    public static void markNeighbors(int i, int j) { //mark and add to queue all cells adjacent to the called cells - those that have already been added as a 'C'
        for (int x = 0; x < 4; x++) {
            int xShift = i + moveRow[x];
            int yShift = j + moveCol[x];
            
            if (xShift < 0 || xShift >= rows || yShift < 0 || yShift >= columns)
                continue;
            
            if (cookie[xShift][yShift] == 'X') {
                cookie[xShift][yShift] = 'C';
                int[] tempArray = {xShift, yShift};
                queue.add(tempArray);
            }
        }
    }
}

