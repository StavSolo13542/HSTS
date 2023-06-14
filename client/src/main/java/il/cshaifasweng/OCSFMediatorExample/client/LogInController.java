package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
        roleCB.getItems().add("student");
        roleCB.getItems().add("teacher");
        roleCB.getItems().add("principle");
    }
    @FXML
    void logInAttempt(ActionEvent event) {
        String username = usernameTF.getText();
        String password = passwordTF.getText();
        String role = roleCB.getSelectionModel().getSelectedItem();
        String message = "LogIn " + username + " " + password + " " + role;

        System.out.println("the message is: " + message);//for debugging

        SimpleClient.sendMessage(message);

    }
}