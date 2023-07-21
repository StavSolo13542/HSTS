package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.Question;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;
import org.greenrobot.eventbus.EventBus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.Column;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.util.Arrays;
import java.util.List;


public class TeacherChangeQuestion implements Initializable {

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
    private TextField correct_answer;

    @FXML
    private Button go_back_button;

    @FXML
    private AnchorPane pane;

    @FXML
    private ChoiceBox<String> question_choice_box;

    @FXML
    private TextField question_text_field;

    @FXML
    private Button save_q_btn;

    @FXML
    private ChoiceBox<String> subjects_choice_box;

    @FXML
    private Label teacher_name;

    @FXML
    void AnotherQuestionBtn(ActionEvent event) {

    }
    private static String msg;

    private String subject_name;

    private String question_name;

    private List<String> questionList;

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
        question_choice_box.setOnAction(this::addQuestion);
        SimpleClient.sendMessage("get all SUbjects" + SimpleClient.name);
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

//        // initialize courses_list_view
//        SimpleClient.sendMessage("get all courses" + SimpleClient.name);
//        while (msg == null){
//            System.out.print("");
//        }
//        System.out.println("after second while loop");
//        String[] courses = msg.split("___");
//        List<String> courseList = Arrays.asList(courses);
//
//        // Create a new ObservableList and pass the arrayArrayList as an argument to the FXCollections.observableArrayList() method
//        ObservableList<String> observableList1 = FXCollections.observableArrayList(courseList);
//        courses_list_view.setItems(observableList1);
//        msg = null;

    }

    @FXML
    void coursesBtnPushed(ActionEvent event) {
        /*String textAreaString = "";
        List<String> selectedCourses = courses_list_view.getSelectionModel().getSelectedItems();
        for (String item : selectedCourses)
        {
            textAreaString += String.format("%s%n",(String) item);
        }
        selected_courses_text_area.setText(textAreaString);*/
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
        /*String question_without_courses =  question_text_field.getText() + "---" + answer1_text_field.getText() + "///" + "true" + "---" + answer2_text_field.getText() + "///" + "false" + "---" + answer3_text_field.getText() + "///" + "false" + "---" + answer4_text_field.getText() + "///" + "false" + "---" + subject_name;
        SimpleClient.sendMessage("save basic question" + question_without_courses);
        System.out.println("Pressed button to save basic question!");
        List<String> selectedCourses = courses_list_view.getSelectionModel().getSelectedItems();
        for (String course : selectedCourses)
        {
            SimpleClient.sendMessage("save course-question" + question_text_field.getText() + "```" + course);
        }*/
        String final_code = question_text_field.getText() + "---" + answer1_text_field.getText() + "///" + String.valueOf((correct_answer.getText().equals("1") ? "true" : "false")) +
                "---" + answer2_text_field.getText() + "///" + String.valueOf((correct_answer.getText().equals("2") ? "true" : "false")) +
                "---" + answer3_text_field.getText() + "///" + String.valueOf((correct_answer.getText().equals("3") ? "true" : "false")) +
                "---" + answer4_text_field.getText() + "///" + String.valueOf((correct_answer.getText().equals("4") ? "true" : "false")) +
                "---" + this.subject_name;
        final_code = this.question_name + "```" + final_code;
        System.out.println("FINAL code after update: " + final_code);
        SimpleClient.sendMessage("save Update Question" + final_code);
    }

    @FXML
    void viewLastPage(ActionEvent event) {

    }

    public void addQuestion(ActionEvent event)
    {
        this.question_name = question_choice_box.getValue();
        for (String q : this.questionList)
        {
            String[] substrings = q.split("---");
            if (substrings[0].equals(this.question_name))
            {
                for(int i=1; i <= 4; i++)
                {
                    if (substrings[i].split("///")[1].equals("true"))
                    {
                        /*correct_answer.setText(substrings[i].split("///")[0]);*/
                        correct_answer.setText(String.valueOf(i));
                    }
                }
                question_text_field.setText(substrings[0]);
                answer1_text_field.setText(substrings[1].split("///")[0]);
                answer2_text_field.setText(substrings[2].split("///")[0]);
                answer3_text_field.setText(substrings[3].split("///")[0]);
                answer4_text_field.setText(substrings[4].split("///")[0]);
            }
        }

    }

    public void addSubject(ActionEvent event)
    {
        this.subject_name = subjects_choice_box.getValue();
        // initialize courses_list_view
        /*SimpleClient.sendMessage("get all courses" + this.subject_name);
        while (msg == null){
            System.out.print("");
        }
        System.out.println("after second while loop");
        String[] courses = msg.split("___");
        List<String> courseList = Arrays.asList(courses);

        // Create a new ObservableList and pass the arrayArrayList as an argument to the FXCollections.observableArrayList() method
        ObservableList<String> observableList1 = FXCollections.observableArrayList(courseList);
        courses_list_view.setItems(observableList1);
        msg = null;*/
        SimpleClient.sendMessage("get all QUestions" + this.subject_name);
        while (msg == null){
            System.out.print("");
        }
        System.out.println("after second while loop");
        String[] questions = msg.split("___");
        this.questionList = Arrays.asList(questions);
        List<String> real_questions = new ArrayList<String>();
        for (String q : questionList)
        {
            real_questions.add(q.split("---")[0]);
        }

        // Create a new ObservableList and pass the arrayArrayList as an argument to the FXCollections.observableArrayList() method
        ObservableList<String> observableList = FXCollections.observableArrayList(real_questions);
        question_choice_box.setItems(observableList);
        msg = null;
    }

    @FXML
    void initializecorrect_answerTF(ActionEvent event) {

    }

}
