package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.List;


public class TeacherAddQuestion {

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
    private TextArea question_id_text_area;

    @FXML
    private Button save_q_btn;

    @FXML
    private TextArea selected_courses_text_area;

    @FXML
    private ChoiceBox<String> subjects_choice_box;

    @FXML
    private Label teacher_id;

    @FXML
    private Label teacher_name;

    @FXML
    void AnotherQuestionBtn(ActionEvent event) {

    }

    public void initialize() {
        // TODO: get teacher's courses: List<int> teacherCourses
        //courseListView.getItems().addAll(teacherCourses);
    }

    @FXML
    void coursesBtnPushed(ActionEvent event) {
        String textAreaString = "";
        List<String> selectedCourses = courses_list_view.getSelectionModel().getSelectedItems();
        for (Object item : selectedCourses)
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
    void saveQuestionBtn(ActionEvent event) {

    }

    @FXML
    void viewLastPage(ActionEvent event) {

    }

}
