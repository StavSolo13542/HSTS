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
        EventBus.getDefault().register(this);
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
        SimpleClient.sendMessage(message);

    }
    @Subscribe
    //Display input error
    public void DisplayInputError(InputErrorEvent event) {
        String error_str = event.getMessage();
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    String.format(error_str.substring(error_str.indexOf(" ") + 1))
            );
            alert.setTitle("Error!");
            alert.setHeaderText("Error:");
            alert.show();
        });
    }
}