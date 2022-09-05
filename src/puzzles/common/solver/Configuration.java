package puzzles.common.solver;

import java.util.Collection;

/**
 * The generic configuration for both the String and Crossing puzzle.
 *
 * @author RIT CS
 */
public interface Configuration {

    /**
     * Is the given configuration a solution?
     *
     * @return True if yes, otherwise no.
     */
    boolean isSolution();

    /**
     * Get the neighbors of a given configuration.
     *
     * @return The collection of neighbors of the given configuration.
     */
    Collection<Configuration> getNeighbors();

    /**
     * The equals method for hashing. Are the two objects equal to each other?
     *
     * @param other the other object.
     * @return True if yes, otherwise no.
     */
    boolean equals(Object other);

    /**
     * Gives the integer value used for hashing.
     *
     * @return the integer value for hashing.
     */
    int hashCode();

    /**
     * A human-readable String of text that makes sense within the context of the puzzle.
     *
     * @return A String of text.
     */
    String toString();
}