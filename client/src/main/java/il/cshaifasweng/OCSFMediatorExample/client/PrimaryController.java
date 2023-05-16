package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Student;
import il.cshaifasweng.OCSFMediatorExample.entities.StudentGrade;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PrimaryController {

	@FXML
	private Button displayNamesBtn;

	@FXML
	private Button updateGradesBtn;

	@FXML
	private Label header;

	@FXML
	private TableView<StudentGrade> grades_table;

	@FXML
	private TableView<Student> name_table = new TableView<>();
	@FXML
	//Send request to the server to get the student names
	void ShowNames(ActionEvent event) {
		if(!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
		SimpleClient.sendMessage("GetNames");
	}
	@Subscribe
	//Display student names after receiving them from the server
	public void DisplayNames(ShowNameEvent event){
		//Process the message which contains the names
		String msg_string = event.getMessage().toString();
		ObservableList<Student> students = FXCollections.observableArrayList();
//		int index = msg_string.indexOf(":") + 2;
//		while(index != msg_string.length()){
//			students.add(new Student(msg_string.substring(index,msg_string.indexOf(" ",index))));
//			index = msg_string.indexOf(" ",index) + 1;
//		}

		String[] namesArray = msg_string.split(":")[1].trim().split("\\s+");

		// Append each name to the list
		for (String name : namesArray) {
			students.add(new Student(name));
		}

		//Add the names to the table
		Platform.runLater(
				() -> {
					name_table.setItems(students);
					TableColumn<Student,String> nameCol = new TableColumn<Student,String>("name");
					nameCol.setCellValueFactory(new PropertyValueFactory("name"));
					name_table.getColumns().setAll(nameCol);
					addButtonToTable();
					SwitchState(false);
				}
		);
	}
	//Add update buttons to the name table
	private void addButtonToTable() {
		TableColumn<Student, Void> colBtn = new TableColumn("Button Column");

		Callback<TableColumn<Student, Void>, TableCell<Student, Void>> cellFactory = new Callback<TableColumn<Student, Void>, TableCell<Student, Void>>() {
			@Override
			public TableCell<Student, Void> call(final TableColumn<Student, Void> param) {
				final TableCell<Student, Void> cell = new TableCell<Student, Void>() {

					private final Button btn = new Button("View Student's grades");
					{
						//Send a request to the server to get the grades which correspond to the relevant student
						btn.setOnAction((ActionEvent event) -> {
							Student student = getTableView().getItems().get(getIndex());
							try {
								Message message = new Message("GetGrades " + student.getName());
								SimpleClient.getClient().sendToServer(message);
							} catch (IOException e) {
								e.printStackTrace();
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
		colBtn.setCellFactory(cellFactory);
		name_table.getColumns().add(colBtn);
	}
	@Subscribe
	//Display grades after receiving them from the server
	public void DisplayGrades(ShowGradeEvent event) {
		//Process the message which contains the grades
		String msg_string = event.getMessage().toString();
		ObservableList<StudentGrade> grades = FXCollections.observableArrayList();
		int index = msg_string.indexOf(":") + 2;
		String name = msg_string.substring(index, msg_string.indexOf(" ", index));

//		index = msg_string.indexOf(" ", index) + 1;
//		while (index != msg_string.length()) {
//			String test_id = msg_string.substring(index, msg_string.indexOf(" ", index));
//			index = msg_string.indexOf(" ", index) + 1;
//			String grade = msg_string.substring(index, msg_string.indexOf(" ", index));
//			index = msg_string.indexOf(" ", index) + 1;
//			grades.add(new StudentGrade(name, test_id, grade));
//		}

		int startIndex = msg_string.lastIndexOf(":") + 1;
		String numbersString = msg_string.substring(startIndex).trim();
		String[] gradesArray = numbersString.split(" ");

		// Append each name to the list
		for (int i = 0; i < gradesArray.length; i++) {
			String grade = gradesArray[i];
			grades.add(new StudentGrade(name, String.valueOf(i), grade));
		}


		//Add the grades to the table
		Platform.runLater(
				() -> {
					grades_table.setItems(grades);
					TableColumn<StudentGrade,String> nameCol = new TableColumn<StudentGrade,String>("name");
					nameCol.setCellValueFactory(new PropertyValueFactory("name"));
					TableColumn<StudentGrade,String> testIdCol = new TableColumn<StudentGrade,String>("test id");
					testIdCol.setCellValueFactory(new PropertyValueFactory("testId"));
					TableColumn<StudentGrade,String> gradeCol = new TableColumn<StudentGrade,String>("grade");
					gradeCol.setCellValueFactory(new PropertyValueFactory("grade"));
					grades_table.getColumns().setAll(nameCol,testIdCol,gradeCol);
					grades_table.setEditable(true);
					SwitchState(true);
					gradeCol.setCellFactory(TextFieldTableCell.forTableColumn());
					gradeCol.setOnEditCommit(
							t -> t.getTableView().getItems().get(
									t.getTablePosition().getRow()).setGrade(t.getNewValue())
					);
				}
		);
	}
	@FXML
	//Send an update request to the server
	void UpdateGrades(ActionEvent event) {
		ObservableList<StudentGrade> grades = grades_table.getItems();
		String msg = "UpdateGrade " + grades.get(0).getName() + " ";
//		for(int i = 0; i < grades.size(); i++){
//			msg += grades.get(i).getTestId() + " " + grades.get(i).getGrade() + " ";
//		}
		msg += "1 " + "100";
		SimpleClient.sendMessage(msg);
	}
	//Change the UI based on the viewed table
	private void SwitchState(Boolean state){
		name_table.setVisible(!state);
		grades_table.setVisible(state);
		updateGradesBtn.setVisible(state);
		displayNamesBtn.setVisible(state);
	}
	@Subscribe
	//Display input error
	public void DisplayInputError(InputErrorEvent event) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.ERROR,
					String.format("Update failed due to an input error")
			);
			alert.setTitle("Error!");
			alert.setHeaderText("Error:");
			alert.show();
		});
	}
	@Subscribe
	//Display success message
	public void DisplaySuccessMsg(UpdateSucEvent event) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION,
					String.format("Update succeeded")
			);
			alert.setTitle("Success");
			alert.setHeaderText("Success:");
			alert.show();
		});
	}
}
