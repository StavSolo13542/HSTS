package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LogOutController {

    @FXML
    private Button logOutBtn;

    @FXML
    private Label welcomeLabel;
    @FXML
    void initialize(){
        welcomeLabel.setText("Welcome " + SimpleClient.name);
    }
    @FXML
    void LogOut(ActionEvent event) {
        String message = "LogOut " + SimpleClient.name + " " + SimpleClient.role;

        System.out.println("the message is: " + message);//for debugging

        SimpleClient.sendMessage(message);
    }

}