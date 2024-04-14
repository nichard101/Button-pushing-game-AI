import java.util.*;

public class Puzzle extends Object {
    public ArrayList<String[]> buttons;
    public ArrayList<String[]> buttonsPressed;
    
    /**
     * A constructor to create a new blank Puzzle
     */
    public Puzzle(){
        buttons = new ArrayList<String[]>();
        buttonsPressed = new ArrayList<String[]>();
    }

    /**
     * A constructor to create a new Puzzle with a predetermined button list
     * @param arr an ArrayList of buttons
     */
    public Puzzle(ArrayList<String[]> arr){
        buttons = arr;
        buttonsPressed = new ArrayList<String[]>();
    }

    /**
     * A constructor to create a new Puzzle with a predetermined button list and button press history
     * @param arr an ArrayList of buttons
     * @param pressedList an ArrayList of pressed buttons
     */
    public Puzzle(ArrayList<String[]> arr, ArrayList<String[]> pressedList){
        buttons = arr;
        buttonsPressed = pressedList;
    }

    /**
     * A constructor to create a new Puzzle based on an existing Puzzle object
     * @param p a Puzzle object
     */
    public Puzzle(Puzzle p){
        buttons = p.getArray();
        buttonsPressed = p.getMoveArray();
    }

    @Override
    public int hashCode(){
        int prime = 17;
        int prime2 = 23;
        int prime3 = 29;
        int hash = 57;
        for(String[] arr : buttons){
            hash = prime * hash + arr[0].hashCode() + prime2 * arr[1].hashCode() + prime3 * arr[2].hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj){
        if(obj.getClass() == this.getClass()){
            Puzzle p = (Puzzle)obj;
            if(areArraysEqual(p.getArray())){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * A method to add a new button to the puzzle
     * @param b a string array describing the state of the button
     */
    public void addButton(String[] b){
        buttons.add(b);
    }

    /**
     * A method to return the list of buttons in the puzzle
     * @return an ArrayList containing each button of the puzzle
     */
    public ArrayList<String[]> getArray(){
        return buttons;
    }

    /**
     * A method to check whether a given button is able to be pressed
     * @param i the index of the desired button
     * @return True if the button can be pressed, False if not
     */
    public boolean IsButtonPushable(int i){
        return Boolean.parseBoolean(buttons.get(i)[2]);
    }

    /**
     * A method to press a given button, as well as its neighbours
     * @param i the index of the button to be pressed
     */
    public void PushButton(int i){
        if(i >= buttons.size()){
            System.out.println("Button does not exist");
        } else {
            String[] b = buttons.get(i);
            if(IsButtonPushable(i)){ // checks if the button is toggleable
                buttonsPressed.add(b); // adds the button to the list of moves for this Puzzle
                for(int j = 0; j < buttons.size(); j++){
                    String[] bT = buttons.get(j);
                    if(buttonMatches(bT, b)){
                        bT[2] = toggleState(bT[2]); // toggles every button in the puzzle that matches with this button (which includes the button that was pressed, naturally)
                    }
                }
            }
        }
    }

    /**
     * A method to check whether two buttons match either shape or colour
     * @param button
     * @param b
     * @return True if either the colours or the shapes match, False otherwise
     */
    public boolean buttonMatches(String[] button, String[] b){
        return (button[0].equals(b[0]) || button[1].equals(b[1]));
    }

    /**
     * A method to return the opposite state for a button
     * @param state
     * @return "true" if "false", "false" if "true"
     */
    public String toggleState(String state){
        if(Boolean.parseBoolean(state)){
            return "false";
        } else {
            return "true";
        }
    }

    /**
     * A method to return the ArrayList of all pressed buttons
     * @return
     */
    public ArrayList<String[]> getMoveArray(){
        return buttonsPressed;
    }

    /**
     * A method to return a String describing the move history of this Puzzle object
     * @return
     */
    public String getMoveList(){
        String out = "";
        for(String[] b : buttonsPressed){
            out += "[" + b[0] + " " + b[1] + "] ";
        }
        return out;
    }

    /**
     * A method to return the number of buttons that have been pressed on this Puzzle object
     * @return
     */
    public int getMoveNum(){
        return buttonsPressed.size();
    }

    /**
     * A method to return a String describing the current state of this Puzzle object
     */
    public String toString(){
        String out = "";
        for(String[] b : buttons){
            out += "[" + b[0] + "|" + b[1] + "|" + b[2] + "] ";
        }
        return out;
    }   

    /**
     * A method to check whether an incoming array of buttons is equal to the current one
     * @param a the button ArrayList from another Puzzle object
     * @return True if the arrays match, False otherwise
     */
    public boolean areArraysEqual(ArrayList<String[]> a){
        if(a.size() == buttons.size()){
            for(int i = 0; i < a.size(); i++){
                if(!areButtonsEqual(a.get(i), buttons.get(i))){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * A method to check whether two buttons are equal
     * @param a a button
     * @param b a button
     * @return True if the buttons match, False otherwise
     */
    public boolean areButtonsEqual(String[] a, String[] b){
        return a[0].equals(b[0]) && a[1].equals(b[1]) && a[2].equals(b[2]);
    }
}
