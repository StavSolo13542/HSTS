package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.Pupil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class TeacherAskOvertime implements Initializable {

    @FXML
    private ChoiceBox<String> course_choice_box;

    @FXML
    private ChoiceBox<String> exam_choice_box;

    @FXML
    private Button go_back_button;

    @FXML
    private TextField extension;

    @FXML
    private TextField note_to_principal;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button save_btn;

    @FXML
    private ChoiceBox<String> subjects_choice_box;

    @FXML
    private Label teacher_name;

    private String subject_name;

    private String course_name;

    private String exam_name;

    private int[] exam_ids;

    private int exam_index;

    private static String msg;

    public static void receiveMessage(String message)
    {
        msg = message;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //EventBus.getDefault().register(this);
        // initialize subjects_choice_box
        teacher_name.setText(SimpleClient.name);
        subjects_choice_box.setOnAction(this::addSubject);
        course_choice_box.setOnAction(this::addCourse);
        exam_choice_box.setOnAction(this::addExam);
        SimpleClient.sendMessage("gET aLl SUbjects" + SimpleClient.name);
        while (msg == null){
            System.out.print("");
        }
        System.out.println("after first while loop");
        String[] subjects = msg.split("___");
        List<String> subjectList = Arrays.asList(subjects);

        // Create a new ObservableList and pass the arrayArrayList as an argument to the FXCollections.observableArrayList() method
        ObservableList<String> observableList = FXCollections.observableArrayList(subjectList);
        subjects_choice_box.setItems(observableList);
        msg = null;

    }

    public void addSubject(ActionEvent event)
    {
        this.subject_name = subjects_choice_box.getValue();
        // initialize courses_list_view
        SimpleClient.sendMessage("geT aLl Courses" + this.subject_name);
        while (msg == null){
            System.out.print("");
        }
        System.out.println("after second while loop");
        String[] courses = msg.split("___");
        List<String> courseList = Arrays.asList(courses);

        // Create a new ObservableList and pass the arrayArrayList as an argument to the FXCollections.observableArrayList() method
        ObservableList<String> observableList1 = FXCollections.observableArrayList(courseList);
        course_choice_box.setItems(observableList1);
        msg = null;
    }


    public void addCourse(ActionEvent event)
    {
        this.course_name = course_choice_box.getValue();
        // initialize courses_list_view
        SimpleClient.sendMessage("get aLl REaDy Exams" + this.course_name);
        while (msg == null){
            System.out.print("");
        }
        if (msg.equals(""))
        {
            System.out.println("blanc message!");
            EventBus.getDefault().post(new InputErrorEvent(" No ongoing exams!"));
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
        System.out.println("after second while loop");
        String[] ready_exams = msg.split("~~~");
        this.exam_ids = new int[ready_exams.length];
        for (int i=0; i<ready_exams.length; i++)
        {
            this.exam_ids[i] = Integer.valueOf(ready_exams[i].split("@")[2]);
            ready_exams[i] = ready_exams[i].split("@")[0] + "@" + ready_exams[i].split("@")[1];
        }
        List<String> examList = Arrays.asList(ready_exams);

        // Create a new ObservableList and pass the arrayArrayList as an argument to the FXCollections.observableArrayList() method
        ObservableList<String> observableList1 = FXCollections.observableArrayList(examList);
        exam_choice_box.setItems(observableList1);
        msg = null;
    }

    public void addExam(ActionEvent event)
    {
        this.exam_index = this.exam_choice_box.getSelectionModel().getSelectedIndex();
    }

    @FXML
    void saveBtn(ActionEvent event) {
        if (subjects_choice_box.getSelectionModel().isEmpty())
        {
            EventBus.getDefault().post(new InputErrorEvent(" Please select a subject"));
            return;
        }
        if (course_choice_box.getSelectionModel().isEmpty())
        {
            EventBus.getDefault().post(new InputErrorEvent(" Please select a course"));
            return;
        }
        if (exam_choice_box.getSelectionModel().isEmpty())
        {
            EventBus.getDefault().post(new InputErrorEvent(" Please select an exam"));
            return;
        }
        if (this.extension.getText().equals("") || this.note_to_principal.getText().equals(""))
        {
            EventBus.getDefault().post(new InputErrorEvent(" No empty fields are allowed"));
            return;
        }
        try{
            int time_to_extend = Integer.parseInt(extension.getText());
            if (time_to_extend <= 0)
            {
                System.out.println("Extension must be above 0 minutes!");
                EventBus.getDefault().post(new InputErrorEvent(" Extension must be above 0 minutes"));
                return;
            }
        }
        catch (NumberFormatException nfe){
            System.out.println("incorrect format for extension!");
            EventBus.getDefault().post(new InputErrorEvent(" incorrect format for asking extension"));
            return;
        }
        SimpleClient.sendMessage("RequestMoreTime___" + this.exam_ids[this.exam_index] + "___" + this.extension.getText() + "___" + this.note_to_principal.getText());
        try{
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

    @FXML
    void viewLastPage(ActionEvent event) {
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
