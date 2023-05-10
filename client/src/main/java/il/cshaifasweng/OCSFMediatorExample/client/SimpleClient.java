package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.greenrobot.eventbus.EventBus;

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
			EventBus.getDefault().post(new ShowNameEvent(new Message(msg_string)));
		}
		if (msg_string.startsWith("Grades:")){
			EventBus.getDefault().post(new ShowGradeEvent(new Message(msg_string)));
		}
	}
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3100);
		}
		return client;
	}

}
