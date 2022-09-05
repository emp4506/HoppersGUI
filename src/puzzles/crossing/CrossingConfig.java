package puzzles.crossing;

import puzzles.common.solver.Configuration;


import java.util.ArrayList;
import java.util.Collection;

/**
 * This is the configuration class for the Crossing game.
 *
 * @author Evan Prizel, emp4506
 */
public class CrossingConfig implements Configuration {

    /**
     * Holds the numbers for each pup/wolf on each side of the river.
     */
    private ArrayList<Integer> animals;

    /**
     * The boat char value, 'L' for left and 'R' for right
     */
    private char boat;

    /**
     * The total number of pups on both sides.
     */
    private int totalPups;

    /**
     * The total number of wolves on both sides.
     */
    private int totalWolves;

    /**
     * Creates the crossing configuration with the pup/wolf values at each side of the river. As well as what side the
     * boat is on.
     *
     * @param pupsL   Pups on the left side of the river.
     * @param wolvesL Wolves on the left side of the river.
     * @param pupsR   Pups on the right side of the river.
     * @param wolvesR Wolves on the right side of the river.
     * @param boat    The boat side 'L' or 'R'
     */
    public CrossingConfig(int pupsL, int wolvesL, int pupsR, int wolvesR, char boat) {
        animals = new ArrayList<>();
        this.animals.add(pupsL);
        this.animals.add(wolvesL);
        this.animals.add(pupsR);
        this.animals.add(wolvesR);
        this.boat = boat;
        totalPups = pupsL + pupsR;
        totalWolves = wolvesL + wolvesR;
    }

    /**
     * Gets the boat's char value for the equals hashing method.
     *
     * @return the boat's char value.
     */
    public char getBoat() {
        return boat;
    }


    /**
     * Is the given configuration a solution?
     *
     * @return True if yes, otherwise no.
     */
    @Override
    public boolean isSolution() {
        return animals.get(0) == 0 && animals.get(1) == 0 && animals.get(2) == totalPups && animals.get(3) == totalWolves && boat == 'R';
    }

    /**
     * Gives the integer value used for hashing.
     *
     * @return the integer value for hashing.
     */
    @Override
    public int hashCode() {
        return animals.hashCode() + boat;
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
        if (other instanceof CrossingConfig config) {
            result = animals.equals(config.animals) && boat == config.getBoat();
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
        if (boat == 'L') {
            for (int i = 0; i < 3; ++i) {
                if (i == 0) {
                    if (animals.get(0) > 0) {
                        neighbors.add(new CrossingConfig(animals.get(0) - 1, animals.get(1), animals.get(2) + 1, animals.get(3), 'R'));
                    }
                } else if (i == 1) {
                    if (animals.get(0) > 1) {
                        neighbors.add(new CrossingConfig(animals.get(0) - 2, animals.get(1), animals.get(2) + 2, animals.get(3), 'R'));
                    }
                } else {
                    if (animals.get(1) > 0) {
                        neighbors.add(new CrossingConfig(animals.get(0), animals.get(1) - 1, animals.get(2), animals.get(3) + 1, 'R'));
                    }
                }
            }
        } else {
            for (int i = 0; i < 3; ++i) {
                if (i == 0) {
                    if (animals.get(2) > 0) {
                        neighbors.add(new CrossingConfig(animals.get(0) + 1, animals.get(1), animals.get(2) - 1, animals.get(3), 'L'));
                    }
                } else if (i == 1) {
                    if (animals.get(2) > 1) {
                        neighbors.add(new CrossingConfig(animals.get(0) + 2, animals.get(1), animals.get(2) - 2, animals.get(3), 'L'));
                    }
                } else {
                    if (animals.get(3) > 0) {
                        neighbors.add(new CrossingConfig(animals.get(0), animals.get(1) + 1, animals.get(2), animals.get(3) - 1, 'L'));
                    }
                }
            }
        }
        return neighbors;
    }

    /**
     * A human-readable String of text that makes sense within the context of the puzzle.
     *
     * @return A String of text.
     */
    @Override
    public String toString() {
        String result;
        if (boat == 'L') {
            result = ("(BOAT) left=[" + animals.get(0) + ", " + animals.get(1) + "], right=[" + animals.get(2) + ", " + animals.get(3) + "]       ");
        } else {
            result = ("       left=[" + animals.get(0) + ", " + animals.get(1) + "], right=[" + animals.get(2) + ", " + animals.get(3) + "] (BOAT)");
        }
        return result;
    }
}
