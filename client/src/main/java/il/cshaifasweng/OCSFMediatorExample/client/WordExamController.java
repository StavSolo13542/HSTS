package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class WordExamController {

    @FXML
    private Button dirBtn;

    @FXML
    private AnchorPane examHeader;

    @FXML
    private Button submitBtn;

    @FXML
    private Label timerLabel;

    @FXML
    void ChooseDir(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("JavaFX Projects");

        File defaultDirectory = new File("c:/dev/javafx");
        chooser.setInitialDirectory(defaultDirectory);

        File selectedDirectory = chooser.showDialog(App.getStage());

    }

    @FXML
    void SubmitFile(ActionEvent event) {

    }

}
