package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;

import java.net.URL;
import java.util.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import org.hibernate.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;


public class PrinciplePrimaryController {

    private Stage primaryStage;
    private Message message;

    private static String msg;

    private String lastMsg;

    private static int Counter = 0;

    private String numberOfQuestion;

    private String[] questionInExam;

    private String[] answersInExam;


    public PrinciplePrimaryController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private static Session session;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button allExamsBtn;

    @FXML
    private Button approveBtn;

    @FXML
    private ComboBox<String> examCb;

    @FXML
    public TextField examIdTb;

    @FXML
    private TextField extentionTb;

    @FXML
    public static Button notApproveBtn;

    @FXML
    private ComboBox<String> pickAreaCb;

    @FXML
    private ComboBox<String> pickPersonCb;

    @FXML
    private AnchorPane pane;

    @FXML
    private TableView<distributionExam> distributionTable;
    @FXML
    private TableColumn<distributionExam, String> avgColumn;
    @FXML
    private TableColumn<distributionExam, String> medianColumn;

    @FXML
    private TableColumn<distributionExam, String> testIdColumn;

    @FXML
    private TableColumn<distributionExam, String> testNameColumn;


    @FXML
    private TableView<Distribution> distributaionStudents;

    @FXML
    private TableColumn<Distribution, String> zeroToTenColumn;

    @FXML
    private TableColumn<Distribution, String> TenToTwentyColumn;


    @FXML
    private TableColumn<Distribution, String> twentyToThirtyColumn;

    @FXML
    private TableColumn<Distribution, String> thirtyToFortyColumn;

    @FXML
    private TableColumn<Distribution, String> fortyToFithtyColumn;

    @FXML
    private TableColumn<Distribution, String> fithtyToSixtyColumn;

    @FXML
    private TableColumn<Distribution, String> sixtyToSeventyColumn;

    @FXML
    private TableColumn<Distribution, String> seventyToEightyColumn;

    @FXML
    private TableColumn<Distribution, String> eightyToNintyColumn;

    @FXML
    private TableColumn<Distribution, String> nintyToOneHundredColumn;


    ///////////////////////// end of table


    @FXML
    private Button resetDistributionBtn;

    @FXML
    private Button resetExamBtn;

    @FXML
    private TextField scoreTb;

    @FXML
    private Button showDistributionBtn;

    @FXML
    private TableView<ExamDetails> tableExams;

    @FXML
    private TableColumn<ExamDetails, String> columnAExam;

    @FXML
    private TableColumn<ExamDetails, String> columnBExam;

    @FXML
    private TableColumn<ExamDetails, String> columnCExam;
    //////////////////////////////////////////////////////////////////////////////

    @FXML
    private TableView<Distribution> spcificDistTable;

    @FXML
    private TableColumn<Distribution, String> zeroTenSpecifc;

    @FXML
    private TableColumn<Distribution, String> tenTwentySpecific;

    @FXML
    private TableColumn<Distribution, String> TwentyThirtySpecific;

    @FXML
    private TableColumn<Distribution, String> thirtyFortySpecific;

    @FXML
    private TableColumn<Distribution, String> fortyThithySpecific;


    @FXML
    private TableColumn<Distribution, String> fithtySixtySpecific;

    @FXML
    private TableColumn<Distribution, String> sixtySeventySpecifc;

    @FXML
    private TableColumn<Distribution, String> SeventyEightySpecific;
    @FXML
    private TableColumn<Distribution, String> EightyNintySpecific;
    @FXML
    private TableColumn<Distribution, String> nintyHundredSpecific;


    @FXML
    private Button showExamBtn;

    @FXML
    private Button showQuestionsBtn;

    @FXML
    private TableColumn<ShowQuestion, String> columnA;

    @FXML
    private TableColumn<ShowQuestion, String> columnB;


    @FXML
    private TableView<ShowQuestion> table;


    @FXML
    void Approve(ActionEvent event) {

    }

    @FXML
    void NotApprove(ActionEvent event) {


    }

    @FXML
    void ResetTable(ActionEvent event) {

    }

    @FXML
    void ShowDistribution(ActionEvent event) {
        distributionLisst.clear();;
        distributions.clear();
        String i;
        if (pickAreaCb.getValue() == "teacher") {
            i = "1";

        } else if (pickAreaCb.getValue() == "course") {
            i = "2";
        } else//pupil
        {
            i = "3";
        }
        SimpleClient.sendMessage("distribution:" + pickPersonCb.getValue() + " and number:" + i);
        while (msg == null) {
            System.out.println("10");
        }
        // String a="All the distributions: code is: 01011 name is: first exam all the scores are:70 60 40 code is: 02223 name is: second exam all the scores are:5060 40 code is: 02223 name is: third exam all the scores are:5060 40 code is: 02223 name is: fourth exam all the scores are:5060 40";
        ThreeStringArray t=GetDistInfo(msg);
        msg=null;
        String[]codes=t.getExam_code();
        String[]names=t.getExam_names();
        String[]grades=t.getScores();
        String[]medianAndAvg=new String[grades.length*2];

        System.out.println("CODES:");
        for(int k=0;k<codes.length;k++)
        {
            System.out.println(codes[k]);
        }
        System.out.println("NAMES:");
        for(int k=0;k<names.length;k++)
        {
            System.out.println(names[k]);
        }
        System.out.println("GRADES:");
        for(int k=0;k<grades.length;k++)
        {
            System.out.println(grades[k]);
        }



        for (int j=0;j<grades.length;j++) {
            String[] gradesArray = grades[j].split(" ");
            List<Integer> gradesList = new ArrayList<>();

            // Convert grades from string to integers
            for (String grade : gradesArray) {
                gradesList.add(Integer.parseInt(grade));
            }
            // Calculate average
            double sum = 0;
            for (int grade : gradesList) {
                sum += grade;
            }
            double average = sum / gradesList.size();

            // Calculate median
            Collections.sort(gradesList);
            double median;
            int middleIndex = gradesList.size() / 2;
            if (gradesList.size() % 2 == 0) {
                median = (gradesList.get(middleIndex - 1) + gradesList.get(middleIndex)) / 2.0;
            } else {
                median = gradesList.get(middleIndex);
            }
            System.out.println("Grades: " + gradesList);
            System.out.println("Average: " + average);
            System.out.println("Median: " + median);
            medianAndAvg[2*j]=Double.toString(average);
            medianAndAvg[2*j+1]=Double.toString(median);
        }

        for(int k=0;k<grades.length;k++)
        {
            System.out.println("avg: "+medianAndAvg[2*k]);
            System.out.println("meian: "+medianAndAvg[2*k+1]);
        }

        testIdColumn.setCellValueFactory(new PropertyValueFactory<distributionExam, String>("test_id"));
        testNameColumn.setCellValueFactory(new PropertyValueFactory<distributionExam, String>("test_name"));
        avgColumn.setCellValueFactory(new PropertyValueFactory<distributionExam, String>("avg"));
        medianColumn.setCellValueFactory(new PropertyValueFactory<distributionExam, String>("median"));

        zeroToTenColumn.setCellValueFactory(new PropertyValueFactory<Distribution, String>("zero_ten"));
        TenToTwentyColumn.setCellValueFactory(new PropertyValueFactory<Distribution, String>("ten_twenty"));
        twentyToThirtyColumn.setCellValueFactory(new PropertyValueFactory<Distribution, String>("twenty_thirty"));
        thirtyToFortyColumn.setCellValueFactory(new PropertyValueFactory<Distribution, String>("thirty_forty"));
        fortyToFithtyColumn.setCellValueFactory(new PropertyValueFactory<Distribution, String>("forty_fithty"));
        fithtyToSixtyColumn.setCellValueFactory(new PropertyValueFactory<Distribution, String>("fithty_sixty"));
        sixtyToSeventyColumn.setCellValueFactory(new PropertyValueFactory<Distribution, String>("sixty_seventy"));
        seventyToEightyColumn.setCellValueFactory(new PropertyValueFactory<Distribution, String>("seventy_eighty"));
        eightyToNintyColumn.setCellValueFactory(new PropertyValueFactory<Distribution, String>("eighty_ninty"));
        nintyToOneHundredColumn.setCellValueFactory(new PropertyValueFactory<Distribution, String>("ninty_oneHundred"));

        for(int k=0;k< names.length;k++)
        {
            int countZeroTen=0,countTenTwenty=0,countTwentThirty=0,countThirtyForty=0,countFortyFithty=0,countFithtySixty=0,countSixtySeventy=0,countSeventyEighty=0,countEightyNinty=0,countNintyHundred=0;
            distributionExam d=new distributionExam(codes[k],names[k],medianAndAvg[2*k],medianAndAvg[2*k+1]);
            distributionLisst.add(d);
            String allTheGrades=grades[k];
            String[] numbersArray = allTheGrades.split(" ");
            for (String numberStr : numbersArray) {
                System.out.println("the required string is: " + numberStr);
                int number = Integer.parseInt(numberStr);

                if (number >= 0 && number <= 10) {
                    countZeroTen++;
                } else if (number > 10 && number <= 21) {
                    countTenTwenty++;
                } else if (number > 21 && number <= 30) {
                    countTwentThirty++;
                } else if (number > 31 && number <= 40) {
                    countThirtyForty++;
                } else if (number > 41 && number <= 50) {
                    countFortyFithty++;

                } else if (number > 51 && number <= 60) {
                    countFithtySixty++;

                } else if (number > 61 && number <= 70) {
                    countSixtySeventy++;

                } else if (number > 71 && number <= 80) {
                    countSeventyEighty++;

                } else if (number > 81 && number <= 90) {
                    countEightyNinty++;

                } else if (number > 91 && number <= 100) {
                    countNintyHundred++;
                }
            }
            Distribution dist=new Distribution(Integer.toString(countZeroTen),Integer.toString(countTenTwenty),Integer.toString(countTwentThirty),Integer.toString(countThirtyForty),Integer.toString(countFortyFithty),Integer.toString(countFithtySixty),Integer.toString(countSixtySeventy),Integer.toString(countSeventyEighty),Integer.toString(countEightyNinty),Integer.toString(countNintyHundred));
            distributions.add(dist);

        }
        distributionTable.setItems(distributionLisst);
        distributaionStudents.setItems(distributions);








        // Print the results





    }
    @FXML
    void specificExamDist(ActionEvent event) {
        specificExam.clear();
        double sum=0,avg;
        if(examIdTb.getText()!=null)
        {
            SimpleClient.sendMessage("Specific exam with code:"+examIdTb.getText());
            while (msg==null)
            {
                System.out.println("9");
            }
            String[]allGrades=getAllGradesInSpecificExam(msg);
            int[]allNumbers=new int[allGrades.length];
            for(int i=0;i<allGrades.length;i++) {
                allNumbers[i] = Integer.parseInt(allGrades[i]);
            }
            msg=null;
            if(examCb.getValue()=="average") {
                spcificDistTable.setVisible(false);
                for (int i = 0; i < allGrades.length; i++) {
                    sum += Double.parseDouble(allGrades[i]);
                }
                avg = sum / allGrades.length;
                scoreTb.setText(Double.toString(avg));
            }

            else if(examCb.getValue()=="median")
            {
                spcificDistTable.setVisible(false);
                Arrays.sort(allNumbers);

                // Calculating the median
                double median;
                int length = allNumbers.length;
                if (length % 2 == 0) {
                    int middleIndex1 = length / 2 - 1;
                    int middleIndex2 = length / 2;
                    median = (allNumbers[middleIndex1] + allNumbers[middleIndex2]) / 2.0;
                } else {
                    int middleIndex = length / 2;
                    median = allNumbers[middleIndex];
                }
            }
            else
            {

                spcificDistTable.setVisible(true);
                zeroTenSpecifc.setCellValueFactory(new PropertyValueFactory<Distribution, String>("zero_ten"));
                tenTwentySpecific.setCellValueFactory(new PropertyValueFactory<Distribution, String>("ten_twenty"));
                TwentyThirtySpecific.setCellValueFactory(new PropertyValueFactory<Distribution, String>("twenty_thirty"));
                thirtyFortySpecific.setCellValueFactory(new PropertyValueFactory<Distribution, String>("thirty_forty"));
                fortyThithySpecific.setCellValueFactory(new PropertyValueFactory<Distribution, String>("forty_fithty"));
                fithtySixtySpecific.setCellValueFactory(new PropertyValueFactory<Distribution, String>("fithty_sixty"));
                sixtySeventySpecifc.setCellValueFactory(new PropertyValueFactory<Distribution, String>("sixty_seventy"));
                SeventyEightySpecific.setCellValueFactory(new PropertyValueFactory<Distribution, String>("seventy_eighty"));
                EightyNintySpecific.setCellValueFactory(new PropertyValueFactory<Distribution, String>("eighty_ninty"));
                nintyHundredSpecific.setCellValueFactory(new PropertyValueFactory<Distribution, String>("ninty_oneHundred"));
                int countZeroTen=0,countTenTwenty=0,countTwentThirty=0,countThirtyForty=0,countFortyFithty=0,countFithtySixty=0,countSixtySeventy=0,countSeventyEighty=0,countEightyNinty=0,countNintyHundred=0;

                for (int i=0;i<allNumbers.length;i++)
                {
                    if (allNumbers[i] >= 0 && allNumbers[i] <= 10) {
                        countZeroTen++;
                    } else if (allNumbers[i] > 10 && allNumbers[i] <= 21) {
                        countTenTwenty++;
                    } else if (allNumbers[i] > 21 && allNumbers[i] <= 30) {
                        countTwentThirty++;
                    } else if (allNumbers[i] > 31 && allNumbers[i] <= 40) {
                        countThirtyForty++;
                    } else if (allNumbers[i] > 41 && allNumbers[i] <= 50) {
                        countFortyFithty++;

                    } else if (allNumbers[i] > 51 && allNumbers[i] <= 60) {
                        countFithtySixty++;

                    } else if (allNumbers[i] > 61 && allNumbers[i] <= 70) {
                        countSixtySeventy++;

                    } else if (allNumbers[i] > 71 && allNumbers[i] <= 80) {
                        countSeventyEighty++;

                    } else if (allNumbers[i] > 81 && allNumbers[i] <= 90) {
                        countEightyNinty++;

                    } else if (allNumbers[i] > 91 && allNumbers[i] <= 100) {
                        countNintyHundred++;
                    }
                }
                Distribution dist=new Distribution(Integer.toString(countZeroTen),Integer.toString(countTenTwenty),Integer.toString(countTwentThirty),Integer.toString(countThirtyForty),Integer.toString(countFortyFithty),Integer.toString(countFithtySixty),Integer.toString(countSixtySeventy),Integer.toString(countSeventyEighty),Integer.toString(countEightyNinty),Integer.toString(countNintyHundred));
                specificExam.add(dist);
                spcificDistTable.setItems(specificExam);




            }

        }


    }

    @FXML
    void ShowExamDataBase(ActionEvent event) {
        table.setVisible(false);
        tableExams.setVisible(true);
        listExams.clear();
        SimpleClient.sendMessage("getAllExams");
        while (msg == null) {
            System.out.println("1");
        }
        System.out.println("hereeeeee and the message is:" + msg);
        QuestionAndAnswers exam = SpliteExamToTokens(msg);
        msg = null;
        String[] codes = exam.getAnswers();
        String[] names = exam.getQuestions();
        System.out.println("codes:");
        for (String code : codes) {
            System.out.println(code);
        }

        System.out.println("\ncodes:");
        for (String name : names) {
            System.out.println(name);
        }

        columnAExam.setCellValueFactory(new PropertyValueFactory<ExamDetails, String>("id"));
        columnBExam.setCellValueFactory(new PropertyValueFactory<ExamDetails, String>("code"));
        columnCExam.setCellValueFactory(new PropertyValueFactory<ExamDetails, String>("exam_name"));

        for (int i = 0; i < codes.length; i++) {
            String x = String.valueOf(i + 1);
            ExamDetails examInfo = new ExamDetails(x, codes[i], names[i]);
            listExams.add(examInfo);
        }
        tableExams.setItems(listExams);


    }

    @FXML
    void ShowQuestionDataBase(ActionEvent event) {
        tableExams.setVisible(false);
        table.setVisible(true);
        listQuestion.clear();
        // table.setVisible(true);
        SimpleClient.sendMessage("getAllQuestion");
        while (msg == null) {
            System.out.println("hellooooo");
        }


        System.out.println("hereeeeee and the message is:" + msg);
        QuestionAndAnswers a = SpliteToTokens(msg);
        msg = null;
        System.out.println("im here");
        String[] ans = a.getAnswers();
        String[] ques = a.getQuestions();

        System.out.println("Questions:");
        for (String question : ques) {
            System.out.println(question);
        }

        System.out.println("\nAnswers:");
        for (int i = 0; i < ans.length; i++) {
            ans[i] = ans[i].replace("(1)", ",");

        }
        for (String answer : ans) {
            System.out.println(answer);
        }


        columnA.setCellValueFactory(new PropertyValueFactory<ShowQuestion, String>("question_text"));
        columnB.setCellValueFactory(new PropertyValueFactory<ShowQuestion, String>("answers"));
        // ObservableList<ShowQuestion> questionInfos=table.getItems();
        for (int i = 0; i < ques.length; i++) {
            ShowQuestion questionInfo = new ShowQuestion(ques[i], ans[i]);
            listQuestion.add(questionInfo);
        }
        table.setItems(listQuestion);


    }

    @FXML
    void ShowSpecificExam(ActionEvent event) {
        String a = examIdTb.getText();
        String examId = "";
        System.out.println("A is: " + a);

        boolean containsValue = false;

        if (a != null && !a.isEmpty()) {
            try {
                //   int b = Integer.parseInt(a);

                for (ExamDetails examDetails : listExams) {
                    if (examDetails.getCode().equals(String.valueOf(a))) {
                        System.out.println("The code is: " + examDetails.getCode());
                        examId = examDetails.getId();
                        containsValue = true;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: not a valid number");
            }
        } else {
            System.out.println("Input is null or empty");
        }

        if (containsValue) {
            System.out.println("Object with matching code found");
        } else {
            System.out.println("Object with matching code not found");
        }


        if (containsValue == true) {
            try {

                SimpleClient.sendMessage("GetQusetionOfExam:" + examId);
                System.out.println("and the messag is:");
                while (msg == null) {
                    System.out.println("2");
                }
                System.out.println("the msg is:" + msg);
                ExamWindow ex = GetQustionInExam(msg);
                numberOfQuestion = ex.getNummberOfQuestions();
                questionInExam = ex.getQuestions();
                answersInExam = ex.getAnswers();
                System.out.println("Questionsisss:");
                for (String question : questionInExam) {
                    System.out.println(question);
                }
                System.out.println("Answersissss:");
                for (String answer : answersInExam) {
                    System.out.println(answer);
                }
                // System.out.println("what"+numberOfQuestion+"brake "+questionInExam+"brake "+ answersInExam+" brake");
                msg = null;


                FXMLLoader loader = new FXMLLoader(getClass().getResource("Hi.fxml"));
                test t = new test(numberOfQuestion, questionInExam, answersInExam);
                Parent hiPageRoot = loader.load();

                HiController hiController = loader.getController();
                hiController.setPreviousStage((Stage) showExamBtn.getScene().getWindow());

                //  hiController.setUp(numberOfQuestion,questionInExam,answersInExam);


                Stage hiStage = new Stage();
                hiStage.setTitle("Exam");
                hiStage.setScene(new Scene(hiPageRoot));

                hiStage.show();
                ((Stage) showExamBtn.getScene().getWindow()).close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void resetDistribution(ActionEvent event) {
        distributionLisst.clear();
        distributions.clear();
        distributionTable.setItems(distributionLisst);
        distributaionStudents.setItems(distributions);

    }

    final ObservableList<ShowQuestion> listQuestion = FXCollections.observableArrayList(

            new ShowQuestion("hi", "hello"),
            new ShowQuestion("s", "q")
    );

    final ObservableList<ExamDetails> listExams = FXCollections.observableArrayList(

            new ExamDetails("0", "sdsd", "asdsad")
    );
    final ObservableList<distributionExam> distributionLisst = FXCollections.observableArrayList(

            // new ExamDetails("0", "sdsd", "asdsad")
    );

    final ObservableList<Distribution> distributions = FXCollections.observableArrayList(

            // new ExamDetails("0", "sdsd", "asdsad")
    );

    final ObservableList<Distribution> specificExam = FXCollections.observableArrayList(

            // new ExamDetails("0", "sdsd", "asdsad")
    );

    @FXML
    void ChooseAvgDistributionOrMedian(ActionEvent event) {

    }

    @FXML
    void ChooseInfoOfChosen(ActionEvent event) {

    }

    @FXML
    void ChooseTeacherCourseStudent(ActionEvent event) {
        pickPersonCb.getItems().clear();
        String[] names;
        String selectedValue = pickAreaCb.getValue();
        if (selectedValue == "teacher") {
            SimpleClient.sendMessage("Get all teachers");
            while (msg == null) {
                System.out.println("5");
            }
            names = GetAllNames(msg);
            msg = null;


        } else if (selectedValue == "course") {
            SimpleClient.sendMessage("Get all courses");
            while (msg == null) {
                System.out.println("5");
            }
            names = GetAllNames(msg);
            msg = null;


        } else {
            SimpleClient.sendMessage("Get all students");
            while (msg == null) {
                System.out.println("5");
            }
            names = GetAllNames(msg);
            msg = null;

        }
        for (int i = 0; i < names.length; i++) {
            System.out.println("int the " + i + " place is: " + names[i]);
        }
        for (int i = 0; i < names.length; i++) {
            pickPersonCb.setPromptText("show info");
            pickPersonCb.getItems().add(names[i]);
        }

    }

    @FXML
    void initialize() throws IOException {
        Parent userParent = il.cshaifasweng.OCSFMediatorExample.client.App.loadFXML("log_out");
        pane.getChildren().add(0, userParent);
        examCb.getItems().add("average");
        examCb.getItems().add("distribution");
        examCb.getItems().add("median");
        pickAreaCb.getItems().add("teacher");
        pickAreaCb.getItems().add("course");
        pickAreaCb.getItems().add("student");
        columnA.setText("question");
        columnB.setText("answers");
        columnAExam.setText("id");
        columnBExam.setText("code");
        columnCExam.setText("exam name");
        table.setVisible(false);
        tableExams.setVisible(false);
        zeroToTenColumn.setText("0-10");
        spcificDistTable.setVisible(false);
        ;


        //System.out.println(message.toString());
        // examIdTb.setText("asddddd");


    }


    public PrinciplePrimaryController(Message message) {
        this.message = message;
    }

    public PrinciplePrimaryController() {

    }

    public static void receiveMessage(String message) {
        // System.out.println("Message received: " + message);
        msg = message;
    }

    public QuestionAndAnswers SpliteToTokens(String msg) {
        String[] splitMessage = msg.split("next question:");
        String[] questions = new String[splitMessage.length - 1];
        String[] answers = new String[splitMessage.length - 1];

        for (int i = 1; i < splitMessage.length; i++) {
            String[] qAndA = splitMessage[i].split("and the answers:");
            String question = qAndA[0].trim();
            String answer = qAndA[1].trim();
            questions[i - 1] = question;
            answers[i - 1] = answer;
        }

        QuestionAndAnswers q = new QuestionAndAnswers(answers, questions);
        return q;
    }

    public QuestionAndAnswers SpliteExamToTokens(String message) {
        String[] parts = message.split("exam code is:");

// Initialize arrays to store the exam codes and names
        String[] examCodes = new String[parts.length - 1];
        String[] examNames = new String[parts.length - 1];

        for (int i = 1; i < parts.length; i++) {
            // Split each part into code and name using "exam name is:" as the delimiter
            String[] subParts = parts[i].split("exam name is:");

            // Extract the code and name from the subParts array
            String code = subParts[0].trim();
            String name = subParts[1].trim();

            // Store the code and name in the respective arrays
            examCodes[i - 1] = code;
            examNames[i - 1] = name;
        }

// Print the arrays for verification
        System.out.println("Exam Codes:");
        for (String code : examCodes) {
            System.out.println(code);
        }

        System.out.println("Exam Names:");
        for (String name : examNames) {
            System.out.println(name);
        }
        QuestionAndAnswers q = new QuestionAndAnswers(examCodes, examNames);
        return q;
    }


    public ExamWindow GetQustionInExam(String message) {
        String[] questionAndAnswers = message.split("next question:");

// Extract the number of questions in the exam
        String examInfo = questionAndAnswers[0];  // "question in exam:2"
        int numOfQuestions = Integer.parseInt(examInfo.split(":")[1]);

// Initialize arrays to store questions and answers
        String[] questions = new String[numOfQuestions];
        String[] answers = new String[numOfQuestions];

        for (int i = 1; i <= numOfQuestions; i++) {
            String questionAnswer = questionAndAnswers[i];  // "which is bigger?and the answers:1 km/h(1) 2 km/h(1) 3 km/h(1) 4 km/h"

            // Split the question and answers by "and the answers:"
            String[] qaSplit = questionAnswer.split("and the answers:");

            String question = qaSplit[0];  // "which is bigger?"
            String answer = qaSplit[1];    // "1 km/h(1) 2 km/h(1) 3 km/h(1) 4 km/h"

            // Split the answers by "(1)" delimiter
            String[] individualAnswers = answer.split("\\(1\\)");

            // Remove any leading or trailing white spaces from each answer and append newline
            StringBuilder sb = new StringBuilder();
            for (String ans : individualAnswers) {
                ans = ans.trim();
                sb.append(ans).append("\n");
            }
            String formattedAnswers = sb.toString().trim();

            questions[i - 1] = question;
            answers[i - 1] = formattedAnswers;
        }

// Output the results

        String numOfQuestion = Integer.toString(numOfQuestions);
        ExamWindow examWindow = new ExamWindow(numOfQuestion, questions, answers);
        return examWindow;
    }

    public String[] GetAllNames(String msg) {

        String prefix;
        if (msg.startsWith("Teachers")) {
            prefix = "Teachers names are:";
        } else if (msg.startsWith("Student")) {
            prefix = "Student names are:";
        } else {
            prefix = "Courses names are:";
            System.out.println("courses names are:" + msg);
        }
        String[] names;

        if (msg.contains(prefix)) {
            String namesString = msg.substring(msg.indexOf(prefix) + prefix.length());
            names = namesString.split("\\(1\\)");

            // Remove leading and trailing whitespaces from each name
            for (int i = 0; i < names.length; i++) {
                names[i] = names[i].trim();
            }

        } else {
            names = null;
        }
        return names;


    }

    public ThreeStringArray GetDistInfo(String msg) {

// Split the input string into segments using "code is:" as the delimiter
        String[] segments = msg.split("code is:");

// Initialize arrays to store the exam names, codes, and scores
        String[] examNames = new String[segments.length - 1];
        String[] codes = new String[segments.length - 1];
        String[] scores = new String[segments.length - 1];

// Process each segment
        for (int i = 1; i < segments.length; i++) {
            String[] parts = segments[i].trim().split("name is:");
            codes[i - 1] = parts[0].trim();
            examNames[i - 1] = parts[1].substring(0, parts[1].indexOf("all the scores")).trim();
            scores[i - 1] = parts[1].substring(parts[1].indexOf("all the scores are:") + 18).replace(":", "").trim();
        }

// Print the extracted data
        /*System.out.println("Exam Names:");
        for (String name : examNames) {
            System.out.println(name);
        }

        System.out.println("\nCodes:");
        for (String code : codes) {
            System.out.println(code);
        }

        System.out.println("\nScores:");
        for (String score : scores) {
            System.out.println(score);
        }*/
        ThreeStringArray t=new ThreeStringArray(examNames,codes,scores);
        return t;
    }

    public String[] getAllGradesInSpecificExam(String msg)
    {
        System.out.println("the message is: "+msg);
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(msg);

        // List to store the extracted numbers as strings
        List<String> numbers = new ArrayList<>();

        // Extracting numbers and storing them in the list as strings
        while (matcher.find()) {
            String number = matcher.group();
            numbers.add(number);
        }

        // Converting the list to a string array
        String[] numbersArray = numbers.toArray(new String[0]);

        // Printing the string array of numbers
        for (String num : numbersArray) {
            System.out.println(num);
        }
        return numbersArray;

    }
}