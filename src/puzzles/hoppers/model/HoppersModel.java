package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * This is the Hoppers model that does the logic for the hoppers GUI
 *
 * @author Evan Prizel, emp4506
 */
public class HoppersModel {
    /**
     * the collection of observers of this model
     */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /**
     * the current configuration
     */
    private HoppersConfig currentConfig;

    /**
     * selection[0] = the row of the users selection
     * selection[1] = the column of the users selection
     */
    private int[] selection;

    /**
     * creates the model and configuration using the filename, also initializes the selection array
     *
     * @param filename The current name for the file of the model.
     * @throws IOException Needed for compiling.
     */
    public HoppersModel(String filename) throws IOException {
        currentConfig = new HoppersConfig(filename);
        selection = new int[2];
        selection[0] = -100;
        selection[1] = -100;
    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     * Gives the user a hint using the solver and the second output that is in the path.
     * If there is no solution, notify the user that there is no solution.
     */
    public void hint() {
        Solver solver = new Solver();
        Collection<Configuration> path = solver.solve(currentConfig, null);
        HoppersConfig hintConfig;
        boolean solved = false;
        int i = 0;
        if (!path.isEmpty()) {
            if (path.size() == 1) {
                System.out.println("Already solved!");
                solved = true;
            } else {
                for (Configuration config : path) {
                    if (i == 1) {
                        hintConfig = (HoppersConfig) config;
                        this.currentConfig = hintConfig;
                        break;
                    }
                    ++i;
                }
                System.out.println("Next step!");
            }
            if (solved) {
                alertObservers("Already solved!");
            } else {
                alertObservers("Next step!");
            }
        } else {
            System.out.println("There is no solution :(");
            alertObservers("There is no solution :(");
        }
    }

    /**
     * Loads a new game if the file name is valid for hoppers.
     * If the filename is invalid, notify the user.
     *
     * @param filename The file name.
     */
    public void load(String filename) {
        try {
            this.currentConfig = new HoppersConfig(filename);
            selection[0] = -100;
            selection[1] = -100;
            filename = filename.substring(filename.lastIndexOf(File.separator) + 1);
            System.out.println("Loaded: " + filename);
            alertObservers("Loaded: " + filename);
        } catch (IOException e) {
            System.out.println("Failed to load: " + filename);
            alertObservers("Failed to load: " + filename);
        }
    }

    /**
     * CUSTOM
     * Checks if there is a solution still available and notifies the user based on that answer.
     */
    public void isSolutionLeft() {
        Solver solver = new Solver();
        Collection<Configuration> path = solver.solve(currentConfig, null);
        if (path.isEmpty()) {
            System.out.println("There is no solution :(");
            alertObservers("There is no solution :(");
        } else {
            System.out.println("There is a solution left!");
            alertObservers("There is a solution left!");
        }
    }

    /**
     * For the first selection, the user is able to select a cell on the board. If there is a piece there, there is an
     * indication and the selection advances to the second part. Otherwise if there is no piece there an error message
     * displayed and selection ends.
     *
     * @param r The row of the selection.
     * @param c The column of the selection.
     */
    public void select(int r, int c) {
        if (selection[0] == -100 && selection[1] == -100) {
            char[][] theGrid = currentConfig.getGrid();
            if (r >= 0 && c >= 0 && (theGrid[r][c] == 'G' || theGrid[r][c] == 'R')) {
                selection[0] = r;
                selection[1] = c;
                System.out.println("Selected (" + r + ", " + c + ")");
                alertObservers("Selected (" + r + ", " + c + ")");
            } else {
                System.out.println("Invalid selection (" + r + ", " + c + ")");
                alertObservers("Invalid selection (" + r + ", " + c + ")");
            }
        } else {
            int moveR = r - selection[0];
            int moveC = c - selection[1];
            String change = null;
            boolean fail = false;
            if (moveR == -4 && moveC == 0) {
                change = "UP";
            } else if (moveR == 4 && moveC == 0) {
                change = "DOWN";
            } else if (moveR == 0 && moveC == 4) {
                change = "RIGHT";
            } else if (moveR == 0 && moveC == -4) {
                change = "LEFT";
            } else if (moveR == -2 && moveC == -2) {
                change = "UPLEFT";
            } else if (moveR == 2 && moveC == -2) {
                change = "DOWNLEFT";
            } else if (moveR == -2 && moveC == 2) {
                change = "UPRIGHT";
            } else if (moveR == 2 && moveC == 2) {
                change = "DOWNRIGHT";
            } else {
                fail = true;
            }
            if (!fail && currentConfig.contains(change, selection[0], selection[1])) {
                char[][] theGrid = currentConfig.getGrid();
                this.currentConfig = new HoppersConfig(currentConfig.getNumRows(), currentConfig.getNumCols(), theGrid,
                        change, theGrid[selection[0]][selection[1]], selection[0], selection[1]);
                System.out.println("Jumped from (" + selection[0] + ", " + selection[1] + ")  to (" + r + ", " + c + ")");
                alertObservers("Jumped from (" + selection[0] + ", " + selection[1] + ")  to (" + r + ", " + c + ")");
                selection[0] = -100;
                selection[1] = -100;
            } else {
                System.out.println("Can't jump from (" + selection[0] + ", " + selection[1] + ")  to (" + r + ", " + c + ")");
                alertObservers("Can't jump from (" + selection[0] + ", " + selection[1] + ")  to (" + r + ", " + c + ")");
                selection[0] = -100;
                selection[1] = -100;
            }
        }
    }

    /**
     * Gets the number of rows of the configuration.
     *
     * @return The number of rows.
     */
    public int getRows() {
        return currentConfig.getNumRows();
    }

    /**
     * Gets the number of columns of the configuration.
     *
     * @return The number of columns.
     */
    public int getCols() {
        return currentConfig.getNumCols();
    }

    /**
     * Gets the grid of the configuration.
     *
     * @return The grid.
     */
    public char[][] getGrid() {
        return currentConfig.getGrid();
    }

    /**
     * Resets the board to the initial state.
     *
     * @param filename The file name of the current board.
     */
    public void reset(String filename) {
        try {
            this.currentConfig = new HoppersConfig(filename);
            selection[0] = -100;
            selection[1] = -100;
            System.out.println("Puzzle Reset!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            alertObservers("Puzzle Reset!");
        }
    }

    /**
     * A string representation of the configuration with added row and column labels.
     *
     * @return The string representation of the current configuration
     */
    @Override
    public String toString() {
        String str1 = "";
        String str2;
        String result = "   ";
        for (int i = 0; i < currentConfig.getNumCols(); ++i) {
            result = result + i + " ";
        }
        result = result + "\n   ";
        for (int i = 0; i < currentConfig.getNumCols(); ++i) {
            result = result + "--";
        }
        result = result + "\n";
        char[][] grid = currentConfig.getGrid();
        for (int r = 0; r < currentConfig.getNumRows(); ++r) {
            for (int c = 0; c < currentConfig.getNumCols(); ++c) {
                str2 = String.valueOf(grid[r][c]);
                str1 = str1 + " " + str2;
            }
            result = result + r + "| " + str1 + "\n";
            str1 = "";
        }
        return result;
    }
}
