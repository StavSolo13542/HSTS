package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExamResultController {

    @FXML
    private VBox examPane;

    @FXML
    private Button go_back_button;

    @FXML
    private AnchorPane pane;
    @FXML
    void initialize() throws IOException {
        Parent userParent = il.cshaifasweng.OCSFMediatorExample.client.App.loadFXML("log_out");
        pane.getChildren().add(0, userParent);
        Grade grade = SimpleClient.currGrade;
        ReadyExam exam = grade.getReadyExam();
        Platform.runLater(() -> {
            Label label = new Label("Teacher's notes: " + grade.getNote_from_teacher());
            label.setStyle("-fx-font: 14px \"System\";");
            examPane.getChildren().add(label);
            label = new Label("Exam instructions: " + exam.getExam().getNote_to_students() + "\n\n");
            label.setStyle("-fx-font: 14px \"System\";");
            examPane.getChildren().add(label);
            List<Question> questions = exam.getExam().getQuestions();
            int correct_index = 0;
            for (int i = 0; i < questions.size(); i++){
                Question question = questions.get(i);
                List<Answer> answers = question.getAnswers();
                Label question_label = new Label("Question " + (i + 1) + ": " + question.getText());
                question_label.setStyle("-fx-font: 14px \"System\"; -fx-underline: true");
                Label answer_label = new Label("");
                answer_label.setStyle("-fx-font: 14px \"System\";");
                for (int j = 0; j < answers.size(); j++){
                    if (answers.get(j).getIs_correct()) correct_index = j + 1;
                    answer_label.setText(answer_label.getText() + "Answer " + (j + 1) + ": " + answers.get(j).getAnswer_text() + "\n");
                }
                answer_label.setText(answer_label.getText());
                Boolean flag = false;
                for (Question q : grade.getCorrectly_answered_questions())
                {
                    if (question.getQuestion_code_number().equals(q.getQuestion_code_number()))
                    {
                        flag = true;

                    }
                }
                Label check_label = new Label("");
                check_label.setStyle("-fx-font: 14px \"System\";");
                if (flag) check_label.setText("      You were correct. ");
                else check_label.setText("      You were wrong. ");
                check_label.setText(check_label.getText() + "The correct answer is: Answer " + correct_index + "\n\n");
                examPane.getChildren().add(question_label);
                examPane.getChildren().add(answer_label);
                examPane.getChildren().add(check_label);
            }
        });
    }
    @FXML
    void viewLastPage(ActionEvent event) {
        EventBus.getDefault().post(new SwitchScreenEvent("student_exam_scores"));
    }

}
