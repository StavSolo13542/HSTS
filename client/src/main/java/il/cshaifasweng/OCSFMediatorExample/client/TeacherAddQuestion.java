package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;
import org.greenrobot.eventbus.EventBus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.Column;
import java.net.URL;
import java.util.ResourceBundle;

import java.util.Arrays;
import java.util.List;


public class TeacherAddQuestion implements Initializable {

    @FXML
    private Button another_q_btn;

    @FXML
    private TextField answer1_text_field;

    @FXML
    private TextField answer2_text_field;

    @FXML
    private TextField answer3_text_field;

    @FXML
    private TextField answer4_text_field;

    @FXML
    private Button courses_button;

    @FXML
    private ListView<String> courses_list_view;

    @FXML
    private Button go_back_button;

    @FXML
    private AnchorPane pane;

    @FXML
    private TextField question_text_field;

//    @FXML
//    private TextArea question_id_text_area;

    @FXML
    private Button save_q_btn;

    @FXML
    private TextArea selected_courses_text_area;

    @FXML
    private ChoiceBox<String> subjects_choice_box;


    @FXML
    private Label teacher_name;

    @FXML
    void AnotherQuestionBtn(ActionEvent event) {

    }
    private static String msg;

    private String subject_name;

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
        SimpleClient.sendMessage("get all subjects");
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

        // initialize courses_list_view
        SimpleClient.sendMessage("get all courses");
        while (msg == null){
            System.out.print("");
        }
        System.out.println("after second while loop");
        String[] courses = msg.split("___");
        List<String> courseList = Arrays.asList(courses);

        // Create a new ObservableList and pass the arrayArrayList as an argument to the FXCollections.observableArrayList() method
        ObservableList<String> observableList1 = FXCollections.observableArrayList(courseList);
        courses_list_view.setItems(observableList1);
        msg = null;

    }

    @FXML
    void coursesBtnPushed(ActionEvent event) {
        String textAreaString = "";
        List<String> selectedCourses = courses_list_view.getSelectionModel().getSelectedItems();
        for (String item : selectedCourses)
        {
            textAreaString += String.format("%s%n",(String) item);
        }
        selected_courses_text_area.setText(textAreaString);
    }

    @FXML
    void initializeAns1TF(ActionEvent event) {
    }

    @FXML
    void initializeAns2TF(ActionEvent event) {
    }

    @FXML
    void initializeAns3TF(ActionEvent event) {
    }

    @FXML
    void initializeAns4TF(ActionEvent event) {
    }

    @FXML
    void initializeQuestionTF(ActionEvent event) {
    }

    @FXML
    void updateQuestion(KeyEvent event) {

    }

    @FXML
    void saveQuestionBtn(ActionEvent event) {
        String question_without_courses =  question_text_field.getText() + "---" + answer1_text_field.getText() + "///" + "true" + "---" + answer2_text_field.getText() + "///" + "false" + "---" + answer3_text_field.getText() + "///" + "false" + "---" + answer4_text_field.getText() + "///" + "false" + "---" + subject_name;
        SimpleClient.sendMessage("save basic question" + question_without_courses);
        System.out.println("Pressed button to save basic question!");
        List<String> selectedCourses = courses_list_view.getSelectionModel().getSelectedItems();
        for (String course : selectedCourses)
        {
            SimpleClient.sendMessage("save course-question" + question_text_field.getText() + "```" + course);
        }
    }

    @FXML
    void viewLastPage(ActionEvent event) {

    }

    public void addSubject(ActionEvent event)
    {
        this.subject_name = subjects_choice_box.getValue();
    }

}
