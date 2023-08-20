package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Grade;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.ReadyExam;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

// TODO: get the student grades (need the name)

public class StudentExamScoresController {
    @FXML
    private TableColumn<Grade, String> examCol;

    @FXML
    private Button go_back_button;

    @FXML
    private TableColumn<Grade, Integer> gradeCol;

    @FXML
    private TableView<Grade> gradesTBL;

    @FXML
    private AnchorPane pane;

    @FXML
    private TableColumn<Grade, Void> viewCol;


    @FXML
    void initialize() throws IOException {
		EventBus.getDefault().register(this);
		Parent userParent = il.cshaifasweng.OCSFMediatorExample.client.App.loadFXML("log_out");
		pane.getChildren().add(0, userParent);
		RequestGrades();
    }
	void RequestGrades(){
		String message = "GetStudentGrades " + SimpleClient.real_id;
		System.out.println("the message is: " + message);//for debugging
		SimpleClient.sendMessage(message);
	}
	@Subscribe
	public void ShowGrades(StudentGradesEvent event) {
		ObservableList<Grade> grades = FXCollections.observableArrayList();
		for (Grade grade:event.getGrades()) {
			grades.add(grade);
		}
		Platform.runLater(
				() -> {
					gradesTBL.setItems(grades);
					gradeCol.setCellValueFactory(new PropertyValueFactory("the_grade"));
					examCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReadyExam().getExam().getName()));
					gradesTBL.getColumns().setAll(examCol,gradeCol);
					addCustomRowsToTable();
				});
	}
	@Subscribe
	public void RefreshGrades(RefreshGradesEvent event) {
		RequestGrades();
	}
    private void addCustomRowsToTable() {
		Callback<TableColumn<Grade, Void>, TableCell<Grade, Void>> cellBtnFactory = new Callback<TableColumn<Grade, Void>, TableCell<Grade, Void>>() {
			@Override
			public TableCell<Grade, Void> call(final TableColumn<Grade, Void> param) {
				final TableCell<Grade, Void> cell = new TableCell<Grade, Void>() {

					private final Button btn = new Button("View Exam");
					{
						//Send a request to the server to see the corresponding exam
						btn.setOnAction((ActionEvent event) -> {
							Grade grade = getTableView().getItems().get(getIndex());
							if (grade.getReadyExam().getOnline()){
								SimpleClient.currGrade = grade;
								EventBus.getDefault().post(new SwitchScreenEvent("exam_result"));
							}
							else{
								EventBus.getDefault().post(new InputErrorEvent("The exam was not done via the system"));
							}
                        });
					}
					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(btn);
						}
					}
				};
				return cell;
			}
		};
		viewCol.setCellFactory(cellBtnFactory);
		gradesTBL.getColumns().add(viewCol);
	}
    @FXML
    void viewLastPage(ActionEvent event) {
        // Go to "student_primary.fxml"
        EventBus.getDefault().post(new SwitchScreenEvent("student_primary"));
    }
}
