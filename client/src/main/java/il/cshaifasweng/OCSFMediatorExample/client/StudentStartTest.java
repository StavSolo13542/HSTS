package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

// TODO: get the password, check if correct and go to test page

public class StudentStartTest {

    @FXML
    private Button blank_button;

    @FXML
    private Button exams_scores_button;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button start_exams_button;

    @FXML
    private Label student_id;

    @FXML
    private Label student_name;

    @FXML
    private Button go_back_button;

    @FXML
    void viewLastPage(ActionEvent event) {
        // Go to "log_in.fxml"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("student_primary.fxml"));
            Parent root = loader.load();
            Scene nextScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(nextScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
