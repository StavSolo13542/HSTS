package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class StudentPrimaryController {
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField codeTF;
    @FXML
    private Button codeBtn;
    @FXML
    void initialize(){

    }
    @FXML
    void EnterExam(ActionEvent event) {
        String code = codeTF.getText();
        String message = "EnterExam " + code;
        System.out.println("the message is: " + message);//for debugging

        SimpleClient.sendMessage(message);
    }
}
