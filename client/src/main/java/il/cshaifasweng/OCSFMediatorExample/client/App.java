package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class App extends Application {

    private Scene scene;
    private SimpleClient client;
    private static Stage stage;
    private String username;
    @Override
    public void start(Stage stage) throws IOException {
        EventBus.getDefault().register(this);
        client = SimpleClient.getClient();
        client.openConnection();
        scene = new Scene(loadFXML("log_in"), 740, 511);
        stage.setScene(scene);
        stage.show();
        this.stage = stage;
        setWindowTitle("Log In");
        stage.setOnCloseRequest(event -> {
            if (SimpleClient.real_id != ""){
                String message = "LogOut " + SimpleClient.real_id + " " + SimpleClient.role;

                System.out.println("the message is: " + message);//for debugging

                SimpleClient.sendMessage(message);
            }
        });
    }
    static public Stage getStage() {return stage;}
    public void setWindowTitle(String title) {
        stage.setTitle(title);
    }
    public void setContent(String pageName) throws IOException {
        Parent root = loadFXML(pageName);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @Subscribe
    public void switchScreen (SwitchScreenEvent event) {
        String msg = event.getMessage();
        String[] parts = msg.split(" ");
        String screenName = "";
        if (parts.length == 1) screenName = event.getMessage();
        else {
            username = parts[1];
            screenName = parts[2];
        }
        switch (screenName) {
            case "log_in":
                Platform.runLater(() -> {
                    setWindowTitle("Log In");
                    try {
                        setContent("log_in");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "student_primary":
                Platform.runLater(() -> {
                    setWindowTitle("Student - Main Page");
                    try {
                        setContent("student_primary");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "teacher_primary":
                Platform.runLater(() -> {
                    setWindowTitle("Teacher - Main Page");
                    try {
                        setContent("teacher_primary");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "principle_primary":
                Platform.runLater(() -> {
                    setWindowTitle("Principle - Main Page");
                    try {
                        setContent("principle_primary");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "exam":
                Platform.runLater(() -> {
                    setWindowTitle("Exam - online");
                    try {
                        setContent("exam");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "word_exam":
                Platform.runLater(() -> {
                    setWindowTitle("Exam - with word");
                    try {
                        setContent("word_exam");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "student_exam_scores":
                Platform.runLater(() -> {
                    setWindowTitle("Exam Scores");
                    try {
                        setContent("student_exam_scores");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "exam_result":
                Platform.runLater(() -> {
                    setWindowTitle("Exam Result");
                    try {
                        setContent("exam_result");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
        }
    }
    void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    @Override
    public void stop() throws Exception {
        // TODO Auto-generated method stub
        EventBus.getDefault().unregister(this);
        super.stop();
    }
    public static void main(String[] args) {
        launch();
    }

}