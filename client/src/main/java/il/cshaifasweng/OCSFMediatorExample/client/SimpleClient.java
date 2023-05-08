package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		String msg_string = msg.toString();
		if (msg_string.startsWith("Student names:")){
			ObservableList<String> students = FXCollections.observableArrayList();
			int index = msg_string.indexOf(":") + 2;
			while(msg_string.indexOf(" ",index) != -1){
				students.add(msg_string.substring(index,msg_string.indexOf(" ",index)));
				index = msg_string.indexOf(" ",index) + 1;
			}
			students.add(msg_string.substring(index));
			PrimaryController.DisplayNames(students);
		}

	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3100);
		}
		return client;
	}

}
