package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;

import java.io.IOException;
import java.util.ArrayList;

public class PrimaryController {

	@FXML
	private Label header;

	@FXML
	private static TableView<String> name_table = new TableView<>();
	public static void ShowNames(){
		try {
			Message message = new Message("GetNames");
			SimpleClient.getClient().sendToServer(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void DisplayNames(ObservableList<String> students){
		name_table.setItems(students);
		TableColumn<String,String> nameCol = new TableColumn<String,String>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory("name"));
		name_table.getColumns().setAll(nameCol);
	}
}
