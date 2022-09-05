package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * This is the Hoppers Config that the common solver will use.
 *
 * @author Evan Prizel, emp4506
 */
public class HoppersConfig implements Configuration {

    /**
     * The number of rows of the grid.
     */
    private int numRows;

    /**
     * The number of columns of the grid.
     */
    private int numCols;

    /**
     * The grid which stores the location of each frog and the available/unavailable spaces it can go to.
     */
    private char[][] grid;

    /**
     * All possible directions that frogs are able to jump.
     */
    private final static String DOWN = "DOWN";
    private final static String LEFT = "LEFT";
    private final static String RIGHT = "RIGHT";
    private final static String UP = "UP";
    private final static String UPLEFT = "UPLEFT";
    private final static String UPRIGHT = "UPRIGHT";
    private final static String DOWNLEFT = "DOWNLEFT";
    private final static String DOWNRIGHT = "DOWNRIGHT";

    /**
     * An open space a frog can jump on.
     */
    private final static char EMPTY = '.';

    /**
     * A green frog.
     */
    private final static char GREEN = 'G';

    /**
     * A red frog, the one that you want left on the board.
     */
    private final static char RED = 'R';

    /**
     * Creates the configuration that reads the file and creates the frog grid.
     *
     * @param filename The file name.
     * @throws IOException Not needed because the file passed in is valid. (Needs it for compiling)
     */
    public HoppersConfig(String filename) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String[] fields = in.readLine().split("\\s+");
            numRows = Integer.parseInt(fields[0]);
            numCols = Integer.parseInt(fields[1]);
            grid = new char[numRows][numCols];
            for (int i = 0; i < numRows; i++) {
                String[] line = in.readLine().split("\\s+");
                for (int j = 0; j < numCols; ++j) {
                    grid[i][j] = line[j].charAt(0);
                }
            }
        }
    }

    /**
     * Gets the number of rows.
     *
     * @return The number of rows.
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Gets the number of columns.
     *
     * @return The number of columns.
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Gets the grid of frogs.
     *
     * @return The grid of frogs.
     */
    public char[][] getGrid() {
        return grid;
    }

    /**
     * The copy constructor that the getneighbors function uses to create the neighbors of a given frog.
     *
     * @param numRows The number of rows.
     * @param numCols The number of columns.
     * @param grid    The grid of frogs.
     * @param change  The direction to change the frog.
     * @param color   The color of the frog it wants to change.
     * @param curR    The current row of the frog.
     * @param curC    The current column of the frog.
     */
    public HoppersConfig(int numRows, int numCols, char[][] grid, String change, char color, int curR, int curC) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.grid = new char[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            System.arraycopy(grid[i], 0, this.grid[i], 0, grid[i].length);
        }
        this.grid[curR][curC] = EMPTY;
        switch (change) {
            case DOWN -> {
                this.grid[curR + 2][curC] = EMPTY;
                this.grid[curR + 4][curC] = color;
            }
            case UP -> {
                this.grid[curR - 2][curC] = EMPTY;
                this.grid[curR - 4][curC] = color;
            }
            case LEFT -> {
                this.grid[curR][curC - 2] = EMPTY;
                this.grid[curR][curC - 4] = color;
            }
            case RIGHT -> {
                this.grid[curR][curC + 2] = EMPTY;
                this.grid[curR][curC + 4] = color;
            }
            case UPLEFT -> {
                this.grid[curR - 1][curC - 1] = EMPTY;
                this.grid[curR - 2][curC - 2] = color;
            }
            case UPRIGHT -> {
                this.grid[curR - 1][curC + 1] = EMPTY;
                this.grid[curR - 2][curC + 2] = color;
            }
            case DOWNLEFT -> {
                this.grid[curR + 1][curC - 1] = EMPTY;
                this.grid[curR + 2][curC - 2] = color;
            }
            case DOWNRIGHT -> {
                this.grid[curR + 1][curC + 1] = EMPTY;
                this.grid[curR + 2][curC + 2] = color;
            }
        }

    }


    /**
     * Is the given configuration a solution?
     *
     * @return True if yes, otherwise no.
     */
    @Override
    public boolean isSolution() {
        int totalReds = 0;
        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < numCols; ++j) {
                if (grid[i][j] == 'R') {
                    totalReds++;
                } else if (grid[i][j] == 'G') {
                    return false;
                }
            }
        }
        return totalReds == 1;
    }

    /**
     * Is the wanted move valid?
     *
     * @param change The direction the frog wants to move in.
     * @param r      The current row of the frog.
     * @param c      The current column of the frog.
     * @return True if yes, otherwise no.
     */
    public boolean contains(String change, int r, int c) {
        switch (change) {
            case UP:
                if ((r - 4 >= 0) && (grid[r - 4][c] == EMPTY) && (grid[r - 2][c] == 'G')) {
                    return true;
                }
                break;
            case DOWN:
                if ((r + 4 <= numRows - 1) && (grid[r + 4][c] == EMPTY) && (grid[r + 2][c] == 'G')) {
                    return true;
                }
                break;
            case LEFT:
                if ((c - 4 >= 0) && (grid[r][c - 4] == EMPTY) && (grid[r][c - 2] == 'G')) {
                    return true;
                }
                break;
            case RIGHT:
                if ((c + 4 <= numCols - 1) && (grid[r][c + 4] == EMPTY) && (grid[r][c + 2] == 'G')) {
                    return true;
                }
                break;
            case UPLEFT:
                if ((c - 2 >= 0) && (r - 2 >= 0) && (grid[r - 2][c - 2] == EMPTY) && (grid[r - 1][c - 1] == 'G')) {
                    return true;
                }
                break;
            case UPRIGHT:
                if ((r - 2 >= 0) && (c + 2 <= numCols - 1) && (grid[r - 2][c + 2] == EMPTY) && (grid[r - 1][c + 1] == 'G')) {
                    return true;
                }
                break;
            case DOWNLEFT:
                if ((c - 2 >= 0) && (r + 2 <= numRows - 1) && (grid[r + 2][c - 2] == EMPTY) && (grid[r + 1][c - 1] == 'G')) {
                    return true;
                }
                break;
            case DOWNRIGHT:
                if ((r + 2 <= numRows - 1) && (c + 2 <= numCols - 1) && (grid[r + 2][c + 2] == EMPTY) && (grid[r + 1][c + 1] == GREEN)) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Check if two configurations are equal.
     *
     * @param o The other configuration.
     * @return True if yes, otherwise no.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoppersConfig that = (HoppersConfig) o;
        return numRows == that.numRows && numCols == that.numCols && Arrays.deepEquals(grid, that.grid);
    }

    /**
     * Hashes the current configuration so the hashmap can store it correctly.
     *
     * @return The integer value of the hash.
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(numRows, numCols);
        result = 31 * result + Arrays.deepHashCode(grid);
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
        for (int r = 0; r < numRows; ++r) {
            if (r % 2 == 0) {
                for (int c = 0; c < numCols; ++c) {
                    if (grid[r][c] == GREEN || grid[r][c] == RED) {
                        if (c % 2 == 0) {
                            if (contains(UP, r, c)) {
                                neighbors.add(new HoppersConfig(numRows, numCols, grid.clone(), UP, grid[r][c], r, c));
                            }
                            if (contains(RIGHT, r, c)) {
                                neighbors.add(new HoppersConfig(numRows, numCols, grid.clone(), RIGHT, grid[r][c], r, c));
                            }
                            if (contains(LEFT, r, c)) {
                                neighbors.add(new HoppersConfig(numRows, numCols, grid.clone(), LEFT, grid[r][c], r, c));
                            }
                            if (contains(DOWN, r, c)) {
                                neighbors.add(new HoppersConfig(numRows, numCols, grid.clone(), DOWN, grid[r][c], r, c));
                            }
                            if (contains(DOWNRIGHT, r, c)) {
                                neighbors.add(new HoppersConfig(numRows, numCols, grid.clone(), DOWNRIGHT, grid[r][c], r, c));
                            }
                            if (contains(DOWNLEFT, r, c)) {
                                neighbors.add(new HoppersConfig(numRows, numCols, grid.clone(), DOWNLEFT, grid[r][c], r, c));
                            }
                            if (contains(UPRIGHT, r, c)) {
                                neighbors.add(new HoppersConfig(numRows, numCols, grid.clone(), UPRIGHT, grid[r][c], r, c));
                            }
                            if (contains(UPLEFT, r, c)) {
                                neighbors.add(new HoppersConfig(numRows, numCols, grid.clone(), UPLEFT, grid[r][c], r, c));
                            }
                        }
                    }
                }
            } else {
                for (int c = 0; c < numCols; ++c) {
                    if (grid[r][c] == GREEN || grid[r][c] == RED) {
                        if (c % 2 == 1) {
                            if (contains(DOWNRIGHT, r, c)) {
                                neighbors.add(new HoppersConfig(numRows, numCols, grid.clone(), DOWNRIGHT, grid[r][c], r, c));
                            }
                            if (contains(DOWNLEFT, r, c)) {
                                neighbors.add(new HoppersConfig(numRows, numCols, grid.clone(), DOWNLEFT, grid[r][c], r, c));
                            }
                            if (contains(UPRIGHT, r, c)) {
                                neighbors.add(new HoppersConfig(numRows, numCols, grid.clone(), UPRIGHT, grid[r][c], r, c));
                            }
                            if (contains(UPLEFT, r, c)) {
                                neighbors.add(new HoppersConfig(numRows, numCols, grid.clone(), UPLEFT, grid[r][c], r, c));
                            }
                        }
                    }
                }
            }
        }
        return neighbors;
    }

    /**
     * Creates a string representation of the configuration.
     *
     * @return The string representation of the configuration.
     */
    @Override
    public String toString() {
        String str1 = "";
        String str2;
        String result = "";
        for (int r = 0; r < numRows; ++r) {
            for (int c = 0; c < numCols; ++c) {
                str2 = String.valueOf(grid[r][c]);
                str1 = str1 + " " + str2;
            }
            result = result + str1 + "\n";
            str1 = "";
        }
        return result;
    }
}
