package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Answer;
import il.cshaifasweng.OCSFMediatorExample.entities.Question;
import il.cshaifasweng.OCSFMediatorExample.entities.ReadyExam;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

public class WordExamController {
    private ReadyExam exam;
    private final int[] remaining_time = {0,0};
    private String id;
    private String start_time;
    @FXML
    private Button dirBtn;

    @FXML
    private Label examHeaderLabel;

    @FXML
    private Button submitBtn;

    @FXML
    private Label timerLabel;

    public WordExamController() throws FileNotFoundException {
    }
    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
        SimpleClient.postMessage("StartExam");
    }
    @FXML
    void ChooseDir(ActionEvent event) throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose test directory");
        File selectedDirectory = chooser.showDialog(App.getStage());
        if (selectedDirectory == null) EventBus.getDefault().post(new InputErrorEvent(" You need to choose a directory"));
        else{
            String path = selectedDirectory.getPath();
            //Check the generated path. If it is not there, create it.
            if (!Paths.get(path + "\\test").toFile().exists()) Files.createDirectories(Paths.get(path + "\\test"));
            //Create Word docs.
            Platform.runLater(() -> {

                //Blank Document
                XWPFDocument document = new XWPFDocument();
                XWPFParagraph title = document.createParagraph();
                title.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun titleRun = title.createRun();
                titleRun.setText(exam.getExam().getName());
                titleRun.setColor("009933");
                titleRun.setBold(true);
                titleRun.setFontFamily("Ariel");
                titleRun.setFontSize(20);

                try {
                    //Write the Document in file system
                    FileOutputStream out = new FileOutputStream(path + "\\test\\questions" +  ".docx");
                    XWPFParagraph paragraph = document.createParagraph();
                    XWPFRun run = paragraph.createRun();
                    run.setFontSize(16);
                    run.setFontFamily("Ariel");
                    run.setText(exam.getExam().getNote_to_students());
                    List<Question> questions = exam.getExam().getQuestions();
                    for (int i = 0; i < questions.size(); i++) {
                        String question_str;
                        Question question = questions.get(i);
                        question_str = "Question " + (i + 1) + ":\n" + question.getText();
                        paragraph = document.createParagraph();
                        run = paragraph.createRun();
                        run.setFontSize(14);
                        run.setFontFamily("Ariel");
                        List<Answer> answers = question.getAnswers();
                        for (int j = 0; j < answers.size(); j++) {
                            question_str += "\nAnswer " + (j + 1) + ": " + answers.get(j).getAnswer_text();
                        }
                        String[] lines = question_str.split("\n");
                        run.setText(lines[0], 0); // set first line into XWPFRun
                        for(int j=1;j<lines.length;j++) {
                            // add break and insert new text
                            run.addBreak();
                            run.setText(lines[j]);
                        }

                    }
                    document.write(out);
                    //Close document
                    out.close();
                    System.out.println(path + "\\test\\questions" +  ".docx" + " written successfully");
                    submitBtn.setVisible(true);
                    dirBtn.setVisible(false);
                    timerLabel.setVisible(true);
                    examHeaderLabel.setText("Answer the questions in the directory you chose and submit your answers file before your time runs out!");
                        remaining_time[0] = exam.getActual_solving_time();
                        Timer myTimer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                if (remaining_time[1] == 0) {
                                    if (remaining_time[0] == 0) {
                                        myTimer.cancel();
                                        try {
                                            SubmitAnswersForced();
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    } else {
                                        remaining_time[1] = 59;
                                        remaining_time[0]--;
                                    }
                                } else {
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
                        myTimer.scheduleAtFixedRate(task, 0, 1000);

                } catch (FileNotFoundException e) {
                    EventBus.getDefault().post(new InputErrorEvent(" There was an error, please submit the directory again"));
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    EventBus.getDefault().post(new InputErrorEvent(" There was an error, please submit the directory again"));
                    throw new RuntimeException(e);
                }
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                start_time = dtf.format(now);

            });

        }
    }

    void SubmitAnswersForced() throws IOException {
        EventBus.getDefault().post(new InputErrorEvent(" Your time has ended"));
        EventBus.getDefault().post(new SwitchScreenEvent("student_primary"));
    }
    void SubmitAnswers(File answers) throws IOException {
        try{
            XWPFDocument docx = new XWPFDocument(new FileInputStream(answers));

            //using XWPFWordExtractor Class
            XWPFWordExtractor we = new XWPFWordExtractor(docx);
            String text = we.getText();
            String message = "SubmitAnswersWord@" + id + "@" + exam.getId() + "@" + start_time + "@" + (exam.getExam().getDuration_in_minutes() - remaining_time[0]) + "@" + text;
            System.out.println("the message is: " + message);//for debugging
            SimpleClient.sendMessage(message);
            EventBus.getDefault().post(new SuccessEvent("Your test was submitted successfully"));
            EventBus.getDefault().post(new SwitchScreenEvent("student_primary"));
        }
        catch (Exception ex){
            EventBus.getDefault().post(new InputErrorEvent(" You need to submit the test in word format"));
        }

    }
    @Subscribe
    public void LoadExam(StartExamEvent event) {
        exam = event.getExam();
        id = event.getMessage();
    }
    @Subscribe
    public void UpdateTime(TimeExtensionEvent event) {
        Integer minutes = Integer.parseInt(event.getMessage());
        remaining_time[0] += minutes;
    }
    @FXML
    void SubmitFile(ActionEvent event) throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose answers file");
        File selectedFile = chooser.showOpenDialog(App.getStage());
        SubmitAnswers(selectedFile);
    }

}
