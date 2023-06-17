package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;


import java.io.IOException;

// TODO: get the student grades (need the name)

public class StudentExamScores {
    @FXML
    private TableColumn<?, ?> exam_scores_table;

    @FXML
    private AnchorPane pane;

    @FXML
    private Label student_id;

    @FXML
    private Label student_name;

    @FXML
    private Button go_back_button;


    @FXML
    void initialize() {
        // need to get to data abput student's previous exams
//        String filename = "100-sample-contactslist-excel.csv";
//        String[] columns = {"first_name",
//                "last_name", "phone", "email"};
//        List<DataRow> data = readCSVFile(filename,
//                columns);
//        first_name.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFirst_name()));
//        last_name.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLast_name()));
//        phone.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPhone()));
//        email.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));
//        contactsTable.setItems(FXCollections.observableList(data));
    }

    @FXML
    void viewLastPage(ActionEvent event) {
        // Go to "student_primary.fxml"
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("student_primary.fxml"));
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
