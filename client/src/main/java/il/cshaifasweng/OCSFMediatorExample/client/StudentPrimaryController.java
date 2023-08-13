package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class StudentPrimaryController {
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField codeTF;
    @FXML
    private Button codeBtn;

    @FXML
    void initialize() throws IOException {
        Parent userParent = il.cshaifasweng.OCSFMediatorExample.client.App.loadFXML("log_out");
        pane.getChildren().add(0, userParent);
    }

    @FXML
    void EnterExam(ActionEvent event) {
        String code = codeTF.getText();
        if (SimpleClient.EmptyCheck(code)){
            String message = "EnterExam " + code + " " + SimpleClient.real_id;
            System.out.println("the message is: " + message);//for debugging

            SimpleClient.sendMessage(message);
        }

    }

    @FXML
    void viewExamScores(ActionEvent event) {
        EventBus.getDefault().post(new SwitchScreenEvent("student_exam_scores"));
    }
}