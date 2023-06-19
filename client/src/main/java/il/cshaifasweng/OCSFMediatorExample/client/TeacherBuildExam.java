package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class TeacherBuildExam implements Initializable {

    @FXML
    private Button another_q_btn;

    @FXML
    private TextArea exam_name;

    @FXML
    private Button courses_button;

    @FXML
    private ChoiceBox<String> courses_choice_box;

    @FXML
    private TextArea duration_test_area;

    @FXML
    private Button go_back_button;

    @FXML
    private AnchorPane pane;

    @FXML
    private ListView<String> questions_list_view;

    @FXML
    private Button save_q_btn;

    @FXML
    private TextArea selected_questions_text_area;

    @FXML
    private TextArea note_to_students;

    @FXML
    private TextArea note_to_teachers;


    @FXML
    private Label teacher_name;

    private static String msg;

    private String course_name;

    public static void receiveMessage(String message)
    {
        msg = message;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teacher_name.setText(SimpleClient.name);
        courses_choice_box.setOnAction(this::addCourse);
        SimpleClient.sendMessage("Get All Courses For Exam" + SimpleClient.name);
        while (msg == null){
            System.out.print("");
        }
        System.out.println("after first while loop of adding course to exam");
        String[] subjects = msg.split("___");
        List<String> subjectList = Arrays.asList(subjects);

        // Create a new ObservableList and pass the arrayArrayList as an argument to the FXCollections.observableArrayList() method
        ObservableList<String> observableList = FXCollections.observableArrayList(subjectList);
        courses_choice_box.setItems(observableList);
        msg = null;

//        // initialize courses_list_view
//        SimpleClient.sendMessage("Get All Questions For Exam");
//        while (msg == null){
//            System.out.print("");
//        }
//        System.out.println("after second while loop");
//        String[] courses = msg.split("___");
//        List<String> questionList = Arrays.asList(courses);
//
//        // Create a new ObservableList and pass the arrayArrayList as an argument to the FXCollections.observableArrayList() method
//        ObservableList<String> observableList1 = FXCollections.observableArrayList(questionList);
//        questions_list_view.setItems(observableList1);
//        msg = null;
//        // initialize subjects_choice_box
//        SimpleClient.sendMessage("get all subjects");
//        while (msg == null){}
//        String[] subjects = msg.split("___");
//        List<String> subjectList = Arrays.asList(subjects);
//        subjects_choice_box.setItems(FXCollections.observableArrayList(subjectList));
//        msg = null;
//
//        // initialize courses_choice_box
//        SimpleClient.sendMessage("get all courses");
//        while (msg == null){}
//        String[] courses = msg.split("___");
//        List<String> courseList = Arrays.asList(courses);
//        courses_choice_box.setItems((ObservableList<String>) courseList);
//
//        // initialize questions_list_view
//        SimpleClient.sendMessage("get all questions");
//        while (msg == null){}
//        String[] courses = msg.split("___");
//        List<String> courseList = Arrays.asList(courses);
//        questions_list_view.setItems((ObservableList<String>) courseList);
    }

    @FXML
    void AnotherQuestionBtn(ActionEvent event) {

    }

    @FXML
    void coursesBtnPushed(ActionEvent event) {
        String textAreaString = "";
        List<String> selectedQuestions = questions_list_view.getSelectionModel().getSelectedItems();
        for (String item : selectedQuestions)
        {
            textAreaString += String.format("%s%n",(String) item);
        }
        selected_questions_text_area.setText(textAreaString);
    }

    @FXML
    void initializeQuestionTF(ActionEvent event) {

    }

    @FXML
    void saveQuestionBtn(ActionEvent event) {
        //String exam_without_questions =  question_text_field.getText() + "---" + answer1_text_field.getText() + "///" + "true" + "---" + answer2_text_field.getText() + "///" + "false" + "---" + answer3_text_field.getText() + "///" + "false" + "---" + answer4_text_field.getText() + "///" + "false" + "---" + subject_name;
        String exam_without_questions =  exam_name.getText() + "@@@" + course_name + "@@@" + duration_test_area.getText() + "@@@" + note_to_students.getText() + "@@@" + note_to_teachers.getText() + "@@@" + SimpleClient.name;
//        System.out.println(exam_without_questions);
        SimpleClient.sendMessage("save basic exam" + exam_without_questions);
        System.out.println("Pressed button to save basic exam!");
        List<String> selectedQuestions = questions_list_view.getSelectionModel().getSelectedItems();
        for (String question : selectedQuestions)
        {
            SimpleClient.sendMessage("save exam-question" + exam_name.getText() + "```" + question + "```" + "10");        // TODO: need to change 10 to teacher-chosen points for the question
        }
    }

    @FXML
    void viewLastPage(ActionEvent event) {

    }


    public void addCourse(ActionEvent event)
    {
        this.course_name = courses_choice_box.getValue();
        // initialize courses_list_view
        SimpleClient.sendMessage("Get All Questions For Exam" + this.course_name);
        while (msg == null){
            System.out.print("");
        }
        System.out.println("after second while loop");
        String[] courses = msg.split("___");
        List<String> questionList = Arrays.asList(courses);

        // Create a new ObservableList and pass the arrayArrayList as an argument to the FXCollections.observableArrayList() method
        ObservableList<String> observableList1 = FXCollections.observableArrayList(questionList);
        questions_list_view.setItems(observableList1);
        msg = null;
    }
}
