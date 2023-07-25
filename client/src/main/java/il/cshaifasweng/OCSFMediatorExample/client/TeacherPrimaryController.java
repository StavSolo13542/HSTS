package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class TeacherPrimaryController {
    @FXML
    void initialize() {
        System.out.println("name of the teacher is: " + SimpleClient.name);
        teacher_name.setText(SimpleClient.name);
    }

    @FXML
    private Button change_question;

    @FXML
    private Button pull_exam_btn;

    @FXML
    private Button add_question_button;

    @FXML
    private Button build_exams_button;

    @FXML
    private Button go_back_button;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button start_exam_button;


    @FXML
    private Label teacher_name;

    @FXML
    private Button check_exam_button;

    @FXML
    private Button ask_time_extention;

    @FXML
    void goToAskExtention(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ask_overtime.fxml"));
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
    void addQuestion(ActionEvent event) {
        // Go to "teacher_add_question.fxml"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_add_question.fxml"));
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
    void buildExams(ActionEvent event) {
        // Go to "teacher_build_exam.fxml"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_build_exam.fxml"));
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
    void startExam(ActionEvent event) {

    }

    @FXML
    void viewLastPage(ActionEvent event) {
        // Go to "log_in.fxml"
        try {
            String message = "LogOut " + SimpleClient.real_id + " " + SimpleClient.role;

            System.out.println("the message is: " + message);//for debugging

            SimpleClient.sendMessage(message);

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
    void goToPullExam(ActionEvent event) {
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
    void goToChangeQuestion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_change_question.fxml"));
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
    void goToCheckExam(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_check_exam.fxml"));
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
