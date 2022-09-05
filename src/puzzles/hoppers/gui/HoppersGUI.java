package puzzles.hoppers.gui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * This is the GUI class for the Hoppers game. This implements the GUI using the HoppersModel
 *
 * @author Evan Prizel, emp4506
 */
public class HoppersGUI extends Application implements Observer<HoppersModel, String> {

    /**
     * The resources directory is located directly underneath the gui package
     */
    private final static String RESOURCES_DIR = "resources/";

    /**
     * The current model that the GUI is displaying.
     */
    private HoppersModel model;

    /**
     * The current name for the file of the model.
     */
    private String filename;

    /**
     * The main stage that the GUI uses.
     */
    private Stage stage;

    /**
     * The horizontal box used for displaying the helper buttons.
     */
    private HBox bottom;

    /**
     * The label at the top of the borderpane.
     */
    private HBox label;

    /**
     * The borderpane that holds the label, gridpane and horizontal box.
     */
    private BorderPane borderPane;

    /**
     * The gridpane that holds all of the buttons for the frogs, lilypads, or water.
     */
    private GridPane gridPane;

    /**
     * The images for the gridpane buttons.
     */
    private final Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "red_frog.png"));
    private final Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "green_frog.png"));
    private final Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "lily_pad.png"));
    private final Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR + "water.png"));

    /**
     * Initializes the GUI by creating a new model with the appropriate file name and creating an observer for it.
     *
     * @throws IOException This is basically ignored because the filename is valid from the args of the main method.
     */
    public void init() throws IOException {
        filename = getParameters().getRaw().get(0);
        this.model = new HoppersModel(filename);
        model.addObserver(this);
    }

    /**
     * Creates the horizontal box used for the helper buttons.
     *
     * @param stage The main stage that the hopper game is displayed on.
     */
    public void createHBox(Stage stage) {
        Button button;
        bottom = new HBox();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                button = new Button("Load");
                button.setOnAction((event) -> {
                    FileChooser chooser = new FileChooser();
                    String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
                    currentPath += File.separator + "data" + File.separator + "hoppers";
                    chooser.setInitialDirectory(new File(currentPath));
                    File file = chooser.showOpenDialog(stage);
                    filename = file.getPath();
                    model.load(file.getAbsolutePath());
                });
            } else if (i == 1) {
                button = new Button("Reset");
                button.setOnAction((event) -> model.reset(filename));
            } else if (i == 2) {
                button = new Button("Hint");
                button.setOnAction((event) -> model.hint());
            } else {
                button = new Button("Solution Left?");
                button.setOnAction((event) -> model.isSolutionLeft());
            }
            button.setMinHeight(20);
            bottom.getChildren().add(button);
        }
        bottom.setAlignment(Pos.CENTER);
    }

    /**
     * Creates the gridpane that is full of the buttons with frogs, lilypads, and water.
     */
    public void createGPane() {
        Button button;
        this.gridPane = new GridPane();
        char[][] grid = model.getGrid();
        for (int r = 0; r < model.getRows(); ++r) {
            for (int c = 0; c < model.getCols(); ++c) {
                button = new Button();
                if (grid[r][c] == 'R') {
                    button.setGraphic(new ImageView(redFrog));
                } else if (grid[r][c] == 'G') {
                    button.setGraphic(new ImageView(greenFrog));
                } else if (grid[r][c] == '.') {
                    button.setGraphic(new ImageView(lilyPad));
                } else if (grid[r][c] == '*') {
                    button.setGraphic(new ImageView(water));
                }
                int finalC = c;
                int finalR = r;
                button.setOnAction((event) -> model.select(finalR, finalC));
                button.setMinSize(75, 75);
                button.setMaxSize(75, 75);
                gridPane.add(button, c, r);
            }
        }
    }

    /**
     * Creates the label that notifies the user of it's actions.
     *
     * @param message The message from the observers telling the label what action the user took.
     */
    public void createLabel(String message) {
        label = new HBox();
        Label labelText = new Label(message);
        label.getChildren().add(labelText);
        label.setAlignment(Pos.CENTER);
    }

    /**
     * Creates the initial view for the hopper game.
     *
     * @param stage The main stage that the hopper game is displayed on.
     */
    @Override
    public void start(Stage stage) {
        borderPane = new BorderPane();
        createGPane();
        createHBox(stage);
        createLabel("Loaded: " + filename);
        borderPane.setCenter(gridPane);
        borderPane.setBottom(bottom);
        borderPane.setTop(label);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Hoppers GUI");
        this.stage = stage;
        stage.show();
    }

    /**
     * Refreshes the view with the updated model.
     *
     * @param model2  The updated model.
     * @param message The message from the observers telling the label what action the user took.
     */
    public void refresh(HoppersModel model2, String message) {
        this.model = model2;
        createGPane();
        borderPane.setCenter(gridPane);
        createHBox(stage);
        borderPane.setBottom(bottom);
        createLabel(message);
        borderPane.setTop(label);
    }

    /**
     * Calls the refresh method to update the view.
     *
     * @param hoppersModel The updated model.
     * @param msg          The message from the observers telling the label what action the user took.
     */
    @Override
    public void update(HoppersModel hoppersModel, String msg) {
        if (Platform.isFxApplicationThread()) {
            this.refresh(model, msg);
            stage.sizeToScene();
        } else {
            Platform.runLater(() -> this.refresh(model, msg));
        }
    }

    /**
     * Launches the application and checks that the args are the correct length.
     *
     * @param args Command arguments.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
