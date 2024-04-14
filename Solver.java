import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Solver {

    static ArrayList<Puzzle> puzzlesToCheck;
    static HashSet<Puzzle> viewedPuzzles;

    public static void main(String[] args){
        if(args.length > 0){
            if(args[0].equals("-test")){
                HashTest();
            } else if(args[0].equals("-default")){
                ReadPuzzle("puzzle8.txt");
                ReadPuzzle("puzzle17.txt");
                ReadPuzzle("puzzle24.txt");
            }
        } else {
            Scanner sc = new Scanner(System.in);
            try{
                System.out.println("Enter the puzzle file name: ");
                ReadPuzzle(sc.nextLine());
            } catch(Exception e){
                System.out.println("An error has occurred.");
            }
            sc.close();
        }       
    }

    /**
     * Reads in a puzzle from a text file and then solves it
     * @param fileName
     */
    public static void ReadPuzzle(String fileName){
        int puzzleNum = Integer.parseInt(fileName.split(".txt")[0].substring(6));
        String fileContents = getFileContents("Puzzles/" + fileName);
        Puzzle p = CreatePuzzle(fileContents);
        Result r = SolvePuzzle(p);
        System.out.println("Puzzle " + puzzleNum + " solved in " + r.num + " moves with solution " + r.moveSet);
    }

    /**
     * A method to read in the contents of a file
     * @param fileName the name of the file
     * @return a String with the contents of the file
     */
    public static String getFileContents(String fileName){
        String fileContents = "";
        try {
            fileContents = Files.readString(Path.of(fileName));
        } catch (IOException e) {
            System.out.println("File not found.");
        }
        return fileContents;
    }

    /**
     * A method to convert a string containing puzzle buttons to a Puzzle object
     * @param fileContents
     * @return a new Puzzle object
     */
    public static Puzzle CreatePuzzle(String fileContents){
        Scanner sc = new Scanner(fileContents);
        Puzzle p = new Puzzle();
        while(sc.hasNext()){
            String b = sc.nextLine();
            String[] button = b.split(" ");
            p.addButton(button);
        }
        sc.close();
        return p;
    }   

    /**
     * Returns a minimum solution to an input puzzle
     * @param p an input Puzzle object
     * @return a string describing the moveset of the smallest puzzle solution
     */
    public static Result SolvePuzzle(Puzzle p){  
        puzzlesToCheck = new ArrayList<Puzzle>(); // this is an ArrayList rather than a HashSet so that it remains ordered from lowest to highest move count
        viewedPuzzles = new HashSet<Puzzle>(); 

        puzzlesToCheck.add(p); // Starts off the process with our initial Puzzle

        while(true){
            for(int i = 0; i < puzzlesToCheck.size(); i++){ // this updates dynamically as we add more to the "to-do" list. Magic?
                Puzzle pT = puzzlesToCheck.get(i);
                if(!IsPuzzleSeen(pT)){ // checks to make sure we aren't calculating a previously calculated Puzzle
                    if(IsPuzzleSolved(pT)){
                        return new Result(pT.getMoveNum(), pT.getMoveList()); // if the Puzzle is solved, we gucci
                    } else {
                        HashSet<Puzzle> permutations = PuzzlePermutations(pT);
                        for(Puzzle a : permutations){
                            puzzlesToCheck.add(a); // adds each unique permutation to the "to-do" list
                        }
                        AddPuzzleToSeen(pT); // adds the current Puzzle to the "been there, done that" list
                    }
                }
            }
        }
    }

    /**
     * A method to check whether a given ArrayList contains a given Puzzle object
     * @param p a Puzzle object
     * @param list an ArrayList containing Puzzles
     * @return True if the ArrayList contains the puzzle, False otherwise
     */
    public static boolean IsPuzzleSeen(Puzzle p){
        Puzzle b = new Puzzle(p.getArray());
        for(Puzzle pT : viewedPuzzles){
            Puzzle a = new Puzzle(pT.getArray());
            if(a.equals(b)){
                return true;
            }
        }
        return false;
    }

    /**
     * A method to check whether a given puzzle is solved
     * @param p a Puzzle object
     * @return True if every single button in the puzzle is currently toggled "off", False otherwise
     */
    public static boolean IsPuzzleSolved(Puzzle p){
        ArrayList<String[]> buttons = p.getArray();
        for(int i = 0; i < buttons.size(); i++){
            String[] button = buttons.get(i);
            if(button[2].equals("true")){
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a Puzzle object to the list of viewed puzzles
     * @param p
     */
    public static void AddPuzzleToSeen(Puzzle p){
        if(!IsPuzzleSeen(p)){
            viewedPuzzles.add(p);
        }
    }

    /**
     * A method to clone an ArrayList
     * @param in
     * @return a clone of the input ArrayList
     */
    public static ArrayList<String[]> cloneList(ArrayList<String[]> in){
        ArrayList<String[]> out = new ArrayList<String[]>();
        for(int i = 0; i < in.size(); i++){
            String[] b = in.get(i);
            String[] a = {b[0], b[1], b[2]};
            out.add(a);
        }
        return out;
    }

    /**
     * A method to generate all possible permutations of a given puzzle
     * @param p a Puzzle object
     * @return a HashSet containing the results of pressing every possible button in p
     */
    public static HashSet<Puzzle> PuzzlePermutations(Puzzle p){
        HashSet<Puzzle> perms = new HashSet<Puzzle>();
        ArrayList<String[]> buttons = p.getArray();
        for(int i = 0; i < buttons.size(); i++){
            if(p.IsButtonPushable(i)){
                Puzzle temp = new Puzzle(cloneList(p.getArray()), cloneList(p.getMoveArray()));
                temp.PushButton(i);
                perms.add(temp);
            }
        }
        return perms;
    }

    public static void HashTest(){
        String[] arr1 = {"red", "star", "true"};
        String[] arr2 = {"red", "star", "true"};

        System.out.println(arr1[0].hashCode() + " " + arr2[0].hashCode());

        Puzzle a = new Puzzle();
        a.addButton(new String[]{"red", "star", "true"});
        a.addButton(new String[]{"blue", "circle", "true"});

        Puzzle b = new Puzzle();
        b.addButton(new String[]{"red", "star", "true"});
        b.addButton(new String[]{"blue", "circle", "true"});

        Puzzle c = new Puzzle();
        c.addButton(new String[]{"red", "star", "false"});
        c.addButton(new String[]{"blue", "circle", "true"});

        System.out.println("A equals B: " + a.equals(b));
        System.out.println("A equals C: " + a.equals(c));

        a.PushButton(0);
        b.PushButton(0);

        System.out.println("A and B are pushed");

        System.out.println("A equals B: " + a.equals(b));
        System.out.println("A equals C: " + a.equals(c));

        Puzzle d = CloneFreshPuzzle(a);
        System.out.println(a.equals(d));

        String hi1 = "true";
        String hi2 = "true";
        System.out.println(hi1.hashCode() == hi2.hashCode());

        String hi3 = "false";
        System.out.println(hi1.hashCode() == hi3.hashCode());

        hi1 = "false";
        System.out.println(hi1.hashCode() == hi3.hashCode());
    }

    /**
     * A method to clone a Puzzle object without the move history
     * @param p an existing Puzzle object
     * @return a new Puzzle with only the button list
     */
    public static Puzzle CloneFreshPuzzle(Puzzle p){
        return new Puzzle(p.getArray());
    }
}

class Result {
    public int num;
    public String moveSet;
    public Result(int num, String moveSet){
        this.num = num;
        this.moveSet = moveSet;
    }
}