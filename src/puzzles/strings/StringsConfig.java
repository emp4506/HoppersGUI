package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The Configuration class for the String puzzle.
 *
 * @author Evan Prizel, emp4506
 */
public class StringsConfig implements Configuration {

    /**
     * The current state of the word as an ArrayList of Characters.
     */
    private ArrayList<Character> currValue;

    /**
     * The finish value we are searching for as an ArrayList of Characters.
     */
    private ArrayList<Character> finish;

    /**
     * The finish value as a String
     */
    private String finishS;

    /**
     * Creates a StringConfig object. Storing the currValue as well as the finish value.
     *
     * @param startS  The current state of the String.
     * @param finishS The final String we are searching for.
     */
    public StringsConfig(String startS, String finishS) {
        currValue = new ArrayList<>();
        finish = new ArrayList<>();
        this.finishS = finishS;
        for (int i = 0; i < startS.length(); ++i) {
            currValue.add(startS.charAt(i));
        }
        for (int i = 0; i < finishS.length(); ++i) {
            finish.add(finishS.charAt(i));
        }
    }

    /**
     * Gets the name as an ArrayList of Characters.
     *
     * @return The name of the object as an ArrayList of Characters.
     */
    public Collection<Character> getName() {
        return currValue;
    }

    /**
     * Is the given configuration a solution?
     *
     * @return True if yes, otherwise no.
     */
    @Override
    public boolean isSolution() {
        return currValue.equals(finish);
    }

    /**
     * The equals method for hashing. Are the two objects equal to each other?
     *
     * @param other the other object.
     * @return True if yes, otherwise no.
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof StringsConfig config) {
            result = this.getName().equals(config.getName());
        }
        return result;
    }

    /**
     * Gives the integer value used for hashing.
     *
     * @return the integer value for hashing.
     */
    @Override
    public int hashCode() {
        return this.currValue.hashCode();
    }

    /**
     * A human-readable String of text that makes sense within the context of the puzzle.
     *
     * @return A String of text.
     */
    @Override
    public String toString() {
        String result = "";
        String string2;
        for (Character character : currValue) {
            string2 = String.valueOf(character);
            result = result + string2;
        }
        return result;
    }

    /**
     * Get the neighbors of a given configuration.
     *
     * @return The collection of neighbors of the given configuration.
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighbors = new ArrayList<>();
        String newStartLow = "";
        String newStartHigh = "";
        String string2;
        for (int i = 0; i < currValue.size(); ++i) {
            if (currValue.get(i) != finish.get(i)) {
                Character newCharLow = currValue.get(i);
                if (newCharLow != Character.valueOf('A')) {
                    newCharLow--;
                } else {
                    newCharLow = 'Z';
                }
                Character newCharHigh = currValue.get(i);
                if (newCharHigh != Character.valueOf('Z')) {
                    newCharHigh++;
                } else {
                    newCharHigh = 'A';
                }
                for (int j = 0; j < currValue.size(); ++j) {
                    if (i == j) {
                        string2 = String.valueOf(newCharLow);
                    } else {
                        string2 = String.valueOf(currValue.get(j));
                    }
                    newStartLow = newStartLow + string2;
                }
                neighbors.add(new StringsConfig(newStartLow, finishS));
                for (int j = 0; j < currValue.size(); ++j) {
                    if (i == j) {
                        string2 = String.valueOf(newCharHigh);
                    } else {
                        string2 = String.valueOf(currValue.get(j));
                    }
                    newStartHigh = newStartHigh + string2;
                }
                neighbors.add(new StringsConfig(newStartHigh, finishS));
                return neighbors;
            }
        }
        return null;
    }
}
