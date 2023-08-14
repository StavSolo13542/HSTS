package il.cshaifasweng.OCSFMediatorExample.client;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class TeacherBuildExam implements Initializable {
    @FXML
    private TableView<DoubleString> questions_table;

    @FXML
    private TableColumn<String, String> selectedQuestionCol;

    @FXML
    private TableColumn<DoubleString, String> gradesCol;

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
    private TextArea note_to_students;

    @FXML
    private TextArea note_to_teachers;

    @FXML
    private Button another_exam_btn;

    @FXML
    private Label teacher_name;

    private DoubleString questionGradePair;

    private ObservableList<DoubleString> selectedQ = FXCollections.observableArrayList();

    private static String msg;

    private String course_name;

    public static void receiveMessage(String message)
    {
        msg = message;
    }

    private void editData() {
        gradesCol.setCellFactory(TextFieldTableCell.forTableColumn());
        gradesCol.setOnEditCommit(
                t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventBus.getDefault().register(this);
        questions_list_view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Associate the columns with data properties
        selectedQuestionCol.setCellValueFactory(new PropertyValueFactory<>("question"));
        gradesCol.setCellValueFactory(new PropertyValueFactory<>("grade"));

        // Make the grades column editable
        gradesCol.setCellFactory(TextFieldTableCell.forTableColumn());
        gradesCol.setEditable(true);

        // Make the grades column editable
        gradesCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        gradesCol.setOnEditCommit(event -> {
            // Get the updated value from the event
            String newGrade = event.getNewValue();
            // Get the corresponding DoubleString item
            DoubleString doubleString = event.getTableView().getItems().get(event.getTablePosition().getRow());
            // Update the grade property of the DoubleString item
            doubleString.setGrade(newGrade);
        });

        // Set the items of the TableView
        questions_table.setItems(selectedQ);

        // Initialize choice box
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
            questionGradePair = new DoubleString(item, "");
            selectedQ.add(questionGradePair);
        }
        System.out.println("after loop on items");
        questions_table.setItems(selectedQ);
        System.out.println(selectedQ);
    }
    @Subscribe
    public void updateQuestions(RefreshQuestionsEvent event) {
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
        System.out.println("after message = null");
    }
    @FXML
    void initializeQuestionTF(ActionEvent event) {

    }

    @FXML
    void saveExamBtn(ActionEvent event) {
        if (exam_name.getText().equals(""))
        {
            System.out.println("blanc exam name!");
            EventBus.getDefault().post(new InputErrorEvent(" exam name cannot be empty"));
            return;
        }
        if (duration_test_area.getText().equals(""))
        {
            System.out.println("blanc time duration!");
            EventBus.getDefault().post(new InputErrorEvent(" exam duration cannot be empty"));
            return;
        }
        try{
            int test_int_format = Integer.parseInt(duration_test_area.getText());
            if (test_int_format <= 0)
            {
                System.out.println("duration must be at least 1 minute!");
                EventBus.getDefault().post(new InputErrorEvent(" duration must be at least 1 minute"));
                return;
            }
        }
        catch (NumberFormatException nfe){
            System.out.println("incorrect format for duration!");
            EventBus.getDefault().post(new InputErrorEvent(" incorrect format for duration"));
            return;
        }
        if (note_to_teachers.getText().equals(""))
        {
            note_to_teachers.setText("None");
        }
        if (note_to_students.getText().equals(""))
        {
            note_to_students.setText("None");
        }
        //String exam_without_questions =  question_text_field.getText() + "---" + answer1_text_field.getText() + "///" + "true" + "---" + answer2_text_field.getText() + "///" + "false" + "---" + answer3_text_field.getText() + "///" + "false" + "---" + answer4_text_field.getText() + "///" + "false" + "---" + subject_name;
        String exam_without_questions =  exam_name.getText() + "@@@" + course_name + "@@@" + duration_test_area.getText() + "@@@" + note_to_students.getText() + "@@@" + note_to_teachers.getText() + "@@@" + SimpleClient.name;
//        System.out.println(exam_without_questions);

        // Save exam questions
        List<DoubleString> rows = questions_table.getItems();
        int total_grade = 0;
        for (DoubleString doubleString : rows) {
            String grade = doubleString.getGrade();
            try{
                int real_grade = Integer.parseInt(grade);
                total_grade += real_grade;
                if (real_grade <= 0)
                {
                    System.out.println("points must be above 0!");
                    EventBus.getDefault().post(new InputErrorEvent(" points must be above 0"));
                    return;
                }
            }
            catch (NumberFormatException nfe){
                System.out.println("incorrect format for points!");
                EventBus.getDefault().post(new InputErrorEvent(" incorrect format for points"));
                return;
            }
        }
        if (total_grade != 100)
        {
            System.out.println("total grade does not add up to 100!");
            EventBus.getDefault().post(new InputErrorEvent(" total grade does not add up to 100"));
            return;
        }
        SimpleClient.sendMessage("save basic exam" + exam_without_questions);
        System.out.println("Pressed button to save basic exam!\n\n");
        for (DoubleString doubleString : rows) {
            String question = doubleString.getQuestion();
            String grade = doubleString.getGrade();
            System.out.println("save exam-question" + exam_name.getText() + "```" + question + "```" + grade + "end of msg");
            SimpleClient.sendMessage("save exam-question" + exam_name.getText() + "```" + question + "```" + grade);         // TODO: need to change 10 to teacher-chosen points for the question
        }
        // Open another "teacher_build_exam.fxml" page
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

    @FXML
    void AnotherExamBtn(ActionEvent event) {
        // Save the exam first
        saveExamBtn(event);

        // Open a new "teacher_build_exam.fxml"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teacher_build_exam.fxml"));
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
