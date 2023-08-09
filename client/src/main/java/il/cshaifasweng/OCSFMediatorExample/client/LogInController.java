package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class LogInController {

    @FXML
    private PasswordField passwordTF;

    @FXML
    private ComboBox<String> roleCB;

    @FXML
    private Button submitBtn;

    @FXML
    private TextField usernameTF;
    @FXML
    void initialize(){
        //EventBus.getDefault().register(this);
        roleCB.getItems().add("student");
        roleCB.getItems().add("teacher");
        roleCB.getItems().add("principal");
    }
    @FXML
    void logInAttempt(ActionEvent event) {
        String username = usernameTF.getText();
        String password = passwordTF.getText();
//        String role = roleCB.getSelectionModel().getSelectedItem();
//        String message = "LogIn " + username + " " + password + " " + role;
        String message = "LogIn " + username + " " + password;

        System.out.println("the message is: " + message);//for debugging

        SimpleClient.sendMessage(message);

//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_primary.fxml"));
//            Parent root = loader.load();
//            Scene nextScene = new Scene(root);
//            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            currentStage.setScene(nextScene);
//            currentStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}