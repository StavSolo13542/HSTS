package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Answer;
import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
import il.cshaifasweng.OCSFMediatorExample.entities.Question;
import il.cshaifasweng.OCSFMediatorExample.entities.ReadyExam;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ExamController {
    private final int[] remaining_time = {0,0};
    private ReadyExam exam;
    private String id;
    private String start_time;
    private ArrayList<ComboBox<String>> answers_list;
    @FXML
    private Label examHeaderLabel;
    @FXML
    private Button answersBtn;
    @FXML
    private ScrollPane examPane;
    @FXML
    private VBox examContentPane;
    @FXML
    private Button idBtn;
    @FXML
    private TextField idTF;
    @FXML
    private Label idLabel;
    @FXML
    private Label instructionsLabel;
    @FXML
    private Label timerLabel;
    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
    }
    @FXML
    void SubmitId(ActionEvent event) {
        String id = idTF.getText();
        if (SimpleClient.EmptyCheck(id)){
            String message = "StartExam " + id;
            System.out.println("the message is: " + message);//for debugging

            SimpleClient.Validate(message);
        }

    }
    @Subscribe
    public void StartExam(StartExamEvent event){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        start_time = dtf.format(now);
        id = event.getMessage();
        exam = event.getExam();
        answersBtn.setVisible(true);
        answers_list = new ArrayList<>();
        Platform.runLater(() -> {
            idBtn.setVisible(false);
            idLabel.setVisible(false);
            idTF.setVisible(false);
            examPane.setVisible(true);
            timerLabel.setVisible(true);
            examHeaderLabel.setText("Answer the questions and submit the test before your time runs out!\n" + exam.getExam().getName());
            instructionsLabel.setText(instructionsLabel.getText() + "\n" + exam.getExam().getNote_to_students());
            List<Question> questions = exam.getExam().getQuestions();
            for (int i = 0; i < questions.size(); i++){
                Question question = questions.get(i);
                List<Answer> answers = question.getAnswers();
                Label question_label = new Label("Question " + (i + 1) + "\n" + question.getText());
                question_label.setStyle("-fx-font: 24px \"System\";");
                ComboBox<String> answer_select = new ComboBox<>();
                answer_select.setStyle("-fx-font: 16px \"System\";");
                for (int j = 0; j < answers.size(); j++){
                    answer_select.getItems().add("Answer " + (j + 1) + ": " + answers.get(j).getAnswer_text());
                }
                examContentPane.getChildren().add(question_label);
                examContentPane.getChildren().add(answer_select);
                answers_list.add(answer_select);
            }
        });
        remaining_time[0] += exam.getActual_solving_time();
        Timer myTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (remaining_time[1] == 0){
                    if (remaining_time[0] == 0){
                        myTimer.cancel();
                        SubmitAnswersForced();
                    }
                    else{
                        remaining_time[1] = 59;
                        remaining_time[0]--;
                    }
                }
                else{
                    remaining_time[1]--;
                }
                Platform.runLater(() -> {
                    timerLabel.setText("Remaining Time: ");
                    if (remaining_time[0] < 10) timerLabel.setText(timerLabel.getText() + "0");
                    timerLabel.setText(timerLabel.getText() + remaining_time[0] + ":");
                    if (remaining_time[1] < 10) timerLabel.setText(timerLabel.getText() + "0");
                    timerLabel.setText(timerLabel.getText() + remaining_time[1]);

                });
            }
        };
        myTimer.scheduleAtFixedRate(task,0,1000);

    }
    @Subscribe
    public void UpdateTime(TimeExtensionEvent event) {
        Integer minutes = Integer.parseInt(event.getMessage());
        remaining_time[0] += minutes;
    }
    @FXML
    void SubmitAnswersOnTime(ActionEvent event) {
        SubmitAnswers();
    }
    void SubmitAnswersForced() {
        SubmitAnswers();
    }
    void SubmitAnswers() {
        String answers = "";
        for (int i = 0; i < answers_list.size(); i++){
            answers += (answers_list.get(i).getSelectionModel().getSelectedIndex() + 1);
        }
        String message = "SubmitAnswers " + SimpleClient.real_id + " " + exam.getId() + " " + answers + " " + start_time + " " + (exam.getExam().getDuration_in_minutes() - remaining_time[0]);
        System.out.println("the message is: " + message);//for debugging
        SimpleClient.sendMessage(message);
        EventBus.getDefault().post(new SuccessEvent("Your test was submitted successfully"));
        EventBus.getDefault().post(new SwitchScreenEvent("student_primary"));
    }

}