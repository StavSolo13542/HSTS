package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	//Use EventBus to activate the relevant method based on the message
	protected void handleMessageFromServer(Object msg) {		String msg_string = msg.toString();
//		String msg_string = (String) msg;
		msg_string = msg_string.replace("[", "").replace("]", "").replace(",","");
		if (msg_string.startsWith("Student names:")){
			EventBus.getDefault().post(new ShowNameEvent(new Message(msg_string)));
		}
		else if (msg_string.startsWith("Grades:")){
			EventBus.getDefault().post(new ShowGradeEvent(new Message(msg_string)));
		}
		else if (msg_string.equals("InputError")){
			EventBus.getDefault().post(new InputErrorEvent(new Message(msg_string)));
		}
		else if (msg_string.equals("UpdateSuc")){
			EventBus.getDefault().post(new UpdateSucEvent(new Message(msg_string)));
		}

	}
	//Send received message to the server
	static public void sendMessage(String msg){
		try {
			Message message = new Message(msg);
			SimpleClient.getClient().sendToServer(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("192.168.1.213", 3100);
		}
		return client;
	}

}
