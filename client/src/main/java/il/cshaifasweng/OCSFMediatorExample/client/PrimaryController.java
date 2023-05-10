package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.util.Callback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

public class PrimaryController {

	@FXML
	private Button displayNamesBtn;
	@FXML
	private Label header;

	@FXML
	private TableView<Student> name_table = new TableView<>();
	@FXML
	void ShowNames(ActionEvent event) {
		try {
			if(!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
			Message message = new Message("GetNames");
			SimpleClient.getClient().sendToServer(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Subscribe
	public void DisplayNames(ShowNameEvent event){
		String msg_string = event.getMessage().toString();
		ObservableList<Student> students = FXCollections.observableArrayList();
		int index = msg_string.indexOf(":") + 2;
		while(msg_string.indexOf(" ",index) != -1){
			students.add(new Student(msg_string.substring(index,msg_string.indexOf(" ",index))));
			index = msg_string.indexOf(" ",index) + 1;
		}
		students.add(new Student(msg_string.substring(index)));
		name_table.setItems(students);
		TableColumn<Student,String> nameCol = new TableColumn<Student,String>("name");
		nameCol.setCellValueFactory(new PropertyValueFactory("name"));
		name_table.getColumns().setAll(nameCol);
		addButtonToTable();
		name_table.setVisible(true);
	}
	private void addButtonToTable() {
		TableColumn<Student, Void> colBtn = new TableColumn("Button Column");

		Callback<TableColumn<Student, Void>, TableCell<Student, Void>> cellFactory = new Callback<TableColumn<Student, Void>, TableCell<Student, Void>>() {
			@Override
			public TableCell<Student, Void> call(final TableColumn<Student, Void> param) {
				final TableCell<Student, Void> cell = new TableCell<Student, Void>() {

					private final Button btn = new Button("View Student's grades");

					{
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
}
