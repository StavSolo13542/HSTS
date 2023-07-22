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

    public static void receiveMessage(String message)
    {
        msg = message;
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
        while (msg == null){
            System.out.print("");
        }
        System.out.println("after first while loop of adding exam to readyExam");
        String[] Exams = msg.split("___");
        List<String> subjectList = Arrays.asList(Exams);

        // Create a new ObservableList and pass the arrayArrayList as an argument to the FXCollections.observableArrayList() method
        ObservableList<String> observableList = FXCollections.observableArrayList(subjectList);
        exam.setItems(observableList);
    }

    @FXML
    void saveExambtn(ActionEvent event) {
        String exam_final_code =  exam_name + "@@@" + exam_code.getText() + "@@@" + mode.getValue() + "@@@" + time.getText();
//        System.out.println(exam_without_questions);
        SimpleClient.sendMessage("save readyExam" + exam_final_code);
        System.out.println("Pressed button to save readyExam!");
    }

    @FXML
    void pullAnotherExam(ActionEvent event) {

    }

    public void addExam(ActionEvent event)
    {
        this.exam_name = exam.getValue();
    }

    @FXML
    void viewLastPage(ActionEvent event) {
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

//
//    @FXML
//    void viewLastPage(ActionEvent event) {
//        // Go to "log_in.fxml"
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("log_in.fxml"));
//            Parent root = loader.load();
//            Scene nextScene = new Scene(root);
//            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            currentStage.setScene(nextScene);
//            currentStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//