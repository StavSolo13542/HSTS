package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;


public class HiController {
    private Stage stage;
    private Stage previousStage;
    @FXML
    private Button backBtn;
    @FXML
    private VBox vbox;
    @FXML
    private Button btnshow;

    private static String numberOfQuestion;

    private static String[] questionInExam;

    private static String[] answersInExam;


    @FXML
    private TextField textt;

    public void setUp(String number,String[] theQuestion,String[]theAnswers)
    {
        System.out.println("im in class:");

        numberOfQuestion=number;
        questionInExam=theQuestion;
        answersInExam=theAnswers;
        for (int i=0;i<questionInExam.length;i++)
        {
            System.out.println("in the: "+ i+ " index there is: "+questionInExam[i]);
        }
        for (int i=0;i<answersInExam.length;i++)
        {
            System.out.println("in the: "+ i+ " index there is: "+answersInExam[i]);
        }
    }

    public static void recieveMessage(String number,String[] theQuestion,String[]theAnswers)
    {
        System.out.println("im in class:");

        numberOfQuestion=number;
        questionInExam=theQuestion;
        answersInExam=theAnswers;
        for (int i=0;i<questionInExam.length;i++)
        {
            System.out.println("in the: "+ i+ " index there is: "+questionInExam[i]);
        }
        for (int i=0;i<answersInExam.length;i++)
        {
            System.out.println("in the: "+ i+ " index there is: "+answersInExam[i]);
        }
    }


    public void setStage(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }

    @FXML
    void backToPrincipalPage(ActionEvent event) {
        Stage currentStage = (Stage) backBtn.getScene().getWindow();
        currentStage.close();
        previousStage.show();

    }

    @FXML
    void show(ActionEvent event) {
        // Initialize the exam page by generating Labels and TextFields dynamically
        textt.setText("2");
        vbox.setSpacing(20); // Set vertical spacing between question VBoxes

        HBox currentRow = new HBox();
        currentRow.setSpacing(20); // Set horizontal spacing between question VBoxes
        vbox.getChildren().add(currentRow);

        int maxItemsPerRow = 4;
        int itemCount = 0;

        for (int i = 0; i < 2; i++) {
            // Create question label
            Label questionLabel = new Label(questionInExam[i]);

            // Create answer labels (four in this case)
            String[] parts = answersInExam[i].split("\n", 4);

            for(int j=0;j<parts.length;j++)
            {
                System.out.println("in the "+j+"position there is: "+parts[j]);
            }

            Label answerLabel1 = new Label(parts[0]);
            Label answerLabel2 = new Label(parts[1]);
            Label answerLabel3 = new Label(parts[2]);
            Label answerLabel4 = new Label(parts[3]);

            // Create a VBox to hold the question and answer labels
            VBox questionBox = new VBox();
            questionBox.getChildren().addAll(questionLabel, answerLabel1, answerLabel2, answerLabel3, answerLabel4);

            currentRow.getChildren().add(questionBox);
            itemCount++;

            if (itemCount >= maxItemsPerRow) {
                currentRow = new HBox();
                currentRow.setSpacing(20); // Set horizontal spacing for the new row
                vbox.getChildren().add(currentRow);
                itemCount = 0;
            }
        }
    }
    @FXML
    void initialize() {
        textt.setVisible(false);
       /* while (questionInExam==null)
        {
            System.out.println("3");
        }*/
        vbox.setSpacing(20); // Set vertical spacing between question VBoxes

        HBox currentRow = new HBox();
        currentRow.setSpacing(20); // Set horizontal spacing between question VBoxes
        vbox.getChildren().add(currentRow);

        int maxItemsPerRow = 4;
        int itemCount = 0;

        for (int i = 0; i < 2; i++) {
            // Create question label
            Label questionLabel = new Label(questionInExam[i]);

            // Create answer labels (four in this case)
            String[] parts = answersInExam[i].split("\n", 4);

            for(int j=0;j<parts.length;j++)
            {
                System.out.println("in the "+j+"position there is: "+parts[j]);
            }

            Label answerLabel1 = new Label(parts[0]);
            Label answerLabel2 = new Label(parts[1]);
            Label answerLabel3 = new Label(parts[2]);
            Label answerLabel4 = new Label(parts[3]);

            // Create a VBox to hold the question and answer labels
            VBox questionBox = new VBox();
            questionBox.getChildren().addAll(questionLabel, answerLabel1, answerLabel2, answerLabel3, answerLabel4);

            currentRow.getChildren().add(questionBox);
            itemCount++;

            if (itemCount >= maxItemsPerRow) {
                currentRow = new HBox();
                currentRow.setSpacing(20); // Set horizontal spacing for the new row
                vbox.getChildren().add(currentRow);
                itemCount = 0;
            }

        }
    }
}