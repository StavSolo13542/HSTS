package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;
import org.greenrobot.eventbus.EventBus;
import il.cshaifasweng.OCSFMediatorExample.entities.Student;

// TODO: get the student details and pass them to child pages

public class StudentPrimaryController {

    @FXML
    private Button exams_scores_button;

    @FXML
    private Button go_back_button;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button start_exams_button;

    @FXML
    private Label student_id;

    @FXML
    private Label student_name;

    @FXML
    void initialize(){
        //EventBus.getDefault().register(this);
        String temp_id = "123456789";
        String temp_name = "Stav";

        student_id.setText(temp_id);
        student_name.setText(temp_name);
    }

    @FXML
    void viewExamScores(ActionEvent event) {
        // Go to "student_exam_scores.fxml"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("student_exam_scores.fxml"));
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

    @FXML
    void viewStartExam(ActionEvent event) {
        // Go to "student_start_test.fxml"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("student_start_test.fxml"));
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