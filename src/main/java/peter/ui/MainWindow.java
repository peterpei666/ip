package peter.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import peter.Peter;
import peter.command.Command;

/**
 * Controller for Peter's main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Peter peter;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image peterImage = new Image(this.getClass().getResourceAsStream("/images/DaPeter.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Peter instance and displays its welcome message.
     *
     * @param p Peter instance that handles commands.
     */
    public void setPeter(Peter p) {
        peter = p;
        dialogContainer.getChildren().add(DialogBox.getPeterDialog(peter.getWelcomeMessage(), peterImage));
    }

    /**
     * Creates dialog boxes for the user's command and Peter's response.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.trim().isEmpty()) {
            userInput.clear();
            return;
        }
        String response = peter.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getPeterDialog(response, peterImage)
        );
        userInput.clear();
        if (Command.fromString(input) == Command.BYE) {
            Platform.exit();
        }
    }
}
