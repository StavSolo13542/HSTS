package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeacherPullExamFromDrawer {

    @FXML
    private Button another_exam_btn;

    @FXML
    private ChoiceBox<String> mode;

    @FXML
    private ChoiceBox<String> exam;

    @FXML
    private TextArea time;

    @FXML
    private TextArea exam_code;

    @FXML
    private Button go_back_button;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button pulled_exam;


    @FXML
    private Label teacher_name;

    public static String msg;

    private String exam_name;

    public static void receiveMessage(String message) {
        msg = message;
    }

    public static Boolean isExamCodeOk(String str)
    {
        if (str.length() != 4)
        {
            return false;
        }
        for (int i=0; i < str.length(); i++)
        {
            if (!((str.charAt(i) <= 'z' && str.charAt(i) >= 'a') || (str.charAt(i) <= '9' && str.charAt(i) >= '0')))
            {
                return false;
            }
        }
        return true;
    }

    public static Boolean isDateOk(String str)
    {
        if (str.length() != 16)
        {
            return false;
        }
        if (!((str.charAt(0) <= '9' && str.charAt(0) >= '0')))
        {
            return false;
        }
        if (!((str.charAt(1) <= '9' && str.charAt(1) >= '0')))
        {
            return false;
        }
        if (str.charAt(2) != '/')
        {
            return false;
        }
        if (!((str.charAt(3) <= '9' && str.charAt(3) >= '0')))
        {
            return false;
        }
        if (!((str.charAt(4) <= '9' && str.charAt(4) >= '0')))
        {
            return false;
        }
        if (str.charAt(5) != '/')
        {
            return false;
        }
        if (!((str.charAt(6) <= '9' && str.charAt(6) >= '0')))
        {
            return false;
        }
        if (!((str.charAt(7) <= '9' && str.charAt(7) >= '0')))
        {
            return false;
        }
        if (!((str.charAt(8) <= '9' && str.charAt(8) >= '0')))
        {
            return false;
        }
        if (!((str.charAt(9) <= '9' && str.charAt(9) >= '0')))
        {
            return false;
        }
        if (str.charAt(10) != ' ')
        {
            return false;
        }
        if (!((str.charAt(11) <= '9' && str.charAt(11) >= '0')))
        {
            return false;
        }
        if (!((str.charAt(12) <= '9' && str.charAt(12) >= '0')))
        {
            return false;
        }
        if (str.charAt(13) != ':')
        {
            return false;
        }
        if (!((str.charAt(14) <= '9' && str.charAt(14) >= '0')))
        {
            return false;
        }
        if (!((str.charAt(15) <= '9' && str.charAt(15) >= '0')))
        {
            return false;
        }
        return true;
    }

    @FXML
    void initialize() {
        this.teacher_name.setText(SimpleClient.name);
        exam.setOnAction(this::addExam);
        List<String> modes = new ArrayList<String>();
        modes.add("online");
        modes.add("offline");

        // Create a new ObservableList and pass the arrayArrayList as an argument to the FXCollections.observableArrayList() method
        ObservableList<String> observableList1 = FXCollections.observableArrayList(modes);
        this.mode.setItems(observableList1);


        SimpleClient.sendMessage("Get All exAms" + SimpleClient.name);
        while (msg == null) {
            System.out.print("");
        }
        System.out.println("after first while loop of adding exam to readyExam");
        String[] Exams = msg.split("___");
        List<String> subjectList = Arrays.asList(Exams);

        // Create a new ObservableList and pass the arrayArrayList as an argument to the FXCollections.observableArrayList() method
        ObservableList<String> observableList = FXCollections.observableArrayList(subjectList);
        exam.setItems(observableList);
        msg = null;
    }

    @FXML
    void saveExambtn(ActionEvent event) {
        String exam_final_code = exam_name + "@@@" + exam_code.getText() + "@@@" + mode.getValue() + "@@@" + time.getText();
        if (!isExamCodeOk(this.exam_code.getText()))
        {
            System.out.println(" exam code number is not in a correct format.");
            EventBus.getDefault().post(new InputErrorEvent(" Exam code number must be 4 digits and lowercase letters."));
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_primary.fxml"));
                Parent root = loader.load();
                Scene nextScene = new Scene(root);
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.setScene(nextScene);
                currentStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            msg = null;
            return;
        }
        if (!isDateOk(this.time.getText()))
        {
            System.out.println(" Time & Date are not in a correct format.");
            EventBus.getDefault().post(new InputErrorEvent(" Time & Date are not in a correct format."));
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_primary.fxml"));
                Parent root = loader.load();
                Scene nextScene = new Scene(root);
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.setScene(nextScene);
                currentStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            msg = null;
            return;
        }
//        System.out.println(exam_without_questions);
        SimpleClient.sendMessage("save readyExam" + exam_final_code);
        System.out.println("Pressed button to save readyExam!");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_primary.fxml"));
            Parent root = loader.load();
            Scene nextScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(nextScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        msg = null;
        return;
    }

    @FXML
    void pullAnotherExam(ActionEvent event) {
        // Open another "teacher_pull_exam.fxml"
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


    public void addExam(ActionEvent event) {
        this.exam_name = exam.getValue();
    }

    @FXML
    void viewLastPage(ActionEvent event) {
        // Go to "teacher_primary.fxml"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_primary.fxml"));
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
