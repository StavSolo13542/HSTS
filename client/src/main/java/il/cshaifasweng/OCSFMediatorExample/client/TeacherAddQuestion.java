package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.Column;
import java.io.IOException;
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

    @FXML
    private TextField correct_ans_text_field;

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

    private static String msg;

    private int correctAnsNum;

    private String subject_name;


    // Methods
    public static void receiveMessage(String message)
    {
        msg = message;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        courses_list_view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        teacher_name.setText(SimpleClient.name);
        subjects_choice_box.setOnAction(this::addSubject);
        SimpleClient.sendMessage("get all subjects" + SimpleClient.name);
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
    void updateCorrectAns(ActionEvent event) {
        String userInput = correct_ans_text_field.getText();
        correctAnsNum = Integer.parseInt(userInput);
    }

    @FXML
    void saveQuestionBtn(ActionEvent event) {
        if (question_text_field.getText().equals(""))
        {
            System.out.println("empty question!");
            EventBus.getDefault().post(new InputErrorEvent(" empty question"));
            return;
        }
        if (answer1_text_field.getText().equals("") || answer2_text_field.getText().equals("") || answer3_text_field.getText().equals("") || answer4_text_field.getText().equals(""))
        {
            System.out.println("empty answer!");
            EventBus.getDefault().post(new InputErrorEvent(" empty answer(s)"));
            return;
        }
        if (correct_ans_text_field.getText().equals(""))
        {
            System.out.println("empty answer number!");
            EventBus.getDefault().post(new InputErrorEvent(" must select correct answer"));
            return;
        }
        try{
            int real_correct_answer = Integer.parseInt(correct_ans_text_field.getText());
            if (real_correct_answer <= 0 || real_correct_answer >=5)
            {
                System.out.println("correct answer must be in the range 1-4!");
                EventBus.getDefault().post(new InputErrorEvent(" correct answer out of range"));
                return;
            }
        }
        catch (NumberFormatException nfe){
            System.out.println("incorrect format for answer number!");
            EventBus.getDefault().post(new InputErrorEvent(" incorrect format for answer selection"));
            return;
        }
        if (courses_list_view.getSelectionModel().getSelectedItems().isEmpty())
        {
            System.out.println("no course selected!");
            EventBus.getDefault().post(new InputErrorEvent(" must select at least one course"));
            return;
        }
        String question_without_courses = "";
        if (Integer.parseInt(correct_ans_text_field.getText()) == 1) {
            question_without_courses =  question_text_field.getText() + "---" + answer1_text_field.getText() + "///" + "true" + "---" + answer2_text_field.getText() + "///" + "false" + "---" + answer3_text_field.getText() + "///" + "false" + "---" + answer4_text_field.getText() + "///" + "false" + "---" + subject_name;
        }
        else if (Integer.parseInt(correct_ans_text_field.getText()) == 2) {
            question_without_courses =  question_text_field.getText() + "---" + answer1_text_field.getText() + "///" + "false" + "---" + answer2_text_field.getText() + "///" + "true" + "---" + answer3_text_field.getText() + "///" + "false" + "---" + answer4_text_field.getText() + "///" + "false" + "---" + subject_name;
        }
        else if (Integer.parseInt(correct_ans_text_field.getText()) == 3) {
            question_without_courses =  question_text_field.getText() + "---" + answer1_text_field.getText() + "///" + "false" + "---" + answer2_text_field.getText() + "///" + "false" + "---" + answer3_text_field.getText() + "///" + "true" + "---" + answer4_text_field.getText() + "///" + "false" + "---" + subject_name;
        }
        else if (Integer.parseInt(correct_ans_text_field.getText()) == 4) {
            question_without_courses =  question_text_field.getText() + "---" + answer1_text_field.getText() + "///" + "false" + "---" + answer2_text_field.getText() + "///" + "false" + "---" + answer3_text_field.getText() + "///" + "false" + "---" + answer4_text_field.getText() + "///" + "true" + "---" + subject_name;
        }
        else {
            System.out.println("Error! No correct answer was chosen");

        }
        SimpleClient.sendMessage("save basic question" + question_without_courses);
        System.out.println("Pressed button to save basic question!");
        List<String> selectedCourses = courses_list_view.getSelectionModel().getSelectedItems();
        for (String course : selectedCourses)
        {
            SimpleClient.sendMessage("save course-question" + question_text_field.getText() + "```" + course);
        }
        SimpleClient.sendMessage("new question added");
        // Open a new "teacher_add_question.fxml"
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

    @FXML
    void AnotherQuestionBtn(ActionEvent event) {
        // Save the question first
        saveQuestionBtn(event);
        // Open a new "teacher_add_question.fxml"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_add_question.fxml"));
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

    public void addSubject(ActionEvent event)
    {
        this.subject_name = subjects_choice_box.getValue();
        // initialize courses_list_view
        SimpleClient.sendMessage("get all courses" + this.subject_name);
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

}
