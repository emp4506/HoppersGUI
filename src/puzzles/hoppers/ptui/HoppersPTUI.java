package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;
import java.util.Scanner;

/**
 * This is the PTUI class for the Hoppers game. This implements the PTUI using the HoppersModel
 *
 * @author Evan Prizel, emp4506
 */
public class HoppersPTUI implements Observer<HoppersModel, String> {

    /**
     * The model of the hoppers game.
     */
    private HoppersModel model;

    /**
     * The filename of the game.
     */
    private String filename;

    /**
     * This is what's displayed when the user needs help knowing what inputs are valid.
     */
    public final static String HELP = """

            h(int)              -- hint next move
            l(oad) filename     -- load new puzzle file
            s(elect) r c        -- select cell at r, c
            q(uit)              -- quit the game
            r(eset)             -- reset the current game""";


    /**
     * Initializes the PTUI by creating a new model using the given filename and prints out the starting statements.
     *
     * @param filename the file name
     * @throws IOException Thrown if the input the user gives for the filename is invalid.
     */
    public HoppersPTUI(String filename) throws IOException {
        this.model = new HoppersModel(filename);
        this.filename = filename;
        initializeView();
        System.out.println("Loaded: " + filename);
        System.out.println(model);
        System.out.println(HELP);
    }

    /**
     * Runs the PTUI and handles user inputs.
     */
    private void run() {
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("> ");
            System.out.flush();
            mainLoop:
            while (in.hasNextLine()) {
                String[] command = in.nextLine().split("\\s+");
                switch (command[0]) {
                    case "h":
                        model.hint();
                        break;
                    case "l":
                        if (command.length >= 2) {
                            model.load(command[1]);
                            filename = command[1];
                        } else {
                            System.out.println("Invalid number of arguments!");
                        }
                        break;
                    case "s":
                        if (command.length >= 3) {
                            model.select(Integer.parseInt(command[1]), Integer.parseInt(command[2]));
                        } else {
                            System.out.println("Invalid number of arguments!");
                        }
                        break;
                    case "q":
                        break mainLoop;
                    case "r":
                        model.reset(filename);
                        break;
                    default:
                        System.out.println(HELP);
                        break;
                }
                System.out.print("> ");
                System.out.flush();
            }
        }
    }

    /**
     * Initializes the view.
     */
    private void initializeView() {
        model.addObserver(this);
    }

    /**
     * Prints out the updated model.
     *
     * @param model The new model.
     * @param msg   The message from the observers telling the update method what action the user took.
     */
    @Override
    public void update(HoppersModel model, String msg) {
        System.out.println(model);
    }

    /**
     * Runs the game and handles command arguments.
     *
     * @param args The command arguments
     * @throws IOException Thrown if the input the user gives for the filename is invalid.
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            HoppersPTUI hoppersPTUI = new HoppersPTUI(args[0]);
            hoppersPTUI.run();
        }
    }
}
