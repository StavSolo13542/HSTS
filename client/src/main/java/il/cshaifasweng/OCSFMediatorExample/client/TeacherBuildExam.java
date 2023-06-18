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
    private TextField author_text_field;

    @FXML
    private Button courses_button;

    @FXML
    private ChoiceBox<?> courses_choice_box;

    @FXML
    private TextArea duration_test_area;

    @FXML
    private Button go_back_button;

    @FXML
    private AnchorPane pane;

    @FXML
    private ListView<?> questions_list_view;

    @FXML
    private Button save_q_btn;

    @FXML
    private TextArea selected_questions_text_area;

    @FXML
    private ChoiceBox<?> subjects_choice_box;

    @FXML
    private Label teacher_id;

    @FXML
    private Label teacher_name;

    private static String msg;

    public static void receiveMessage(String message)
    {
        msg = message;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialize subjects_choice_box
        SimpleClient.sendMessage("get all subjects");
        while (msg == null){}
        String[] subjects = msg.split("___");
        List<String> subjectList = Arrays.asList(subjects);
        subjects_choice_box.setItems((ObservableList<String>) subjectList);
        msg = null;

        // initialize courses_choice_box
        SimpleClient.sendMessage("get all courses");
        while (msg == null){}
        String[] courses = msg.split("___");
        List<String> courseList = Arrays.asList(courses);
        courses_choice_box.setItems((ObservableList<String>) courseList);

        // initialize questions_list_view
        SimpleClient.sendMessage("get all questions");
        while (msg == null){}
        String[] courses = msg.split("___");
        List<String> courseList = Arrays.asList(courses);
        questions_list_view.setItems((ObservableList<String>) courseList);
    }

    @FXML
    void AnotherQuestionBtn(ActionEvent event) {

    }

    @FXML
    void coursesBtnPushed(ActionEvent event) {

    }

    @FXML
    void initializeQuestionTF(ActionEvent event) {

    }

    @FXML
    void saveQuestionBtn(ActionEvent event) {

    }

    @FXML
    void viewLastPage(ActionEvent event) {

    }

}
