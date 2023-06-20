package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class TeacherPullExamFromDrawer {
    @FXML
    private Button pulled_exam_btn;

    @FXML
    private Button another_exam_btn;

    @FXML
    private ChoiceBox<?> courses_choice_box;

    @FXML
    private ChoiceBox<?> courses_choice_box1;

    @FXML
    private TextArea duration_test_area;

    @FXML
    private TextArea exam_name;

    @FXML
    private Button go_back_button;

    @FXML
    private AnchorPane pane;

    @FXML
    private Label teacher_name;

    @FXML
    void AnotherExamBtn(ActionEvent event) {
        // Go to "teacher_pull_exam.fxml"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_pull_exam.fxml"));
            Parent root = loader.load();
            Scene nextScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(nextScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void SavePulledExam(ActionEvent event) {
        // SAVE CODE HERE:

        // go back to "teacher_pull_exam.fxml"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_pull_exam.fxml"));
            Parent root = loader.load();
            Scene nextScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(nextScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void viewLastPage(ActionEvent event) {
        // Go to "log_in.fxml"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("log_in.fxml"));
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
