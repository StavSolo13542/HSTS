package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WordExamController {
    private Exam exam;
    @FXML
    private Button dirBtn;

    @FXML
    private AnchorPane examHeader;

    @FXML
    private Button submitBtn;

    @FXML
    private Label timerLabel;
    void initialize(){
        EventBus.getDefault().register(this);
    }
    @FXML
    void ChooseDir(ActionEvent event) throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose test directory");
        File selectedDirectory = chooser.showDialog(App.getStage());
        String path = selectedDirectory.getPath();
        //Check the generated path. If it is not there, create it.
        if (!Paths.get(path + "/test").toFile().exists()) Files.createDirectories(Paths.get(path + "/test"));
        //Create Word docs.
        for (int i = 0;;) {
            //Blank Document
            XWPFDocument document = new XWPFDocument();
            //Write the Document in file system
            FileOutputStream out = new FileOutputStream("generated/" + "createdWord" + "_.docx");
            //create Paragraph
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("VK Number (Parameter):  here you type your text...\n");
            document.write(out);
            //Close document
            out.close();
            System.out.println("createdWord" + "_.docx" + " written successfully");
        }
    }
    @Subscribe
    public void LoadExam(StartExamEvent event) {
        exam = event.getExam();
    }

    @FXML
    void SubmitFile(ActionEvent event) {

    }

}
