package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class SimpleServer extends AbstractServer {

	public SimpleServer(int port) {
		super(port);
		
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String msgString = msg.toString();
		if (msgString.equals("GetNames")) {
			//Get names from database
			sendMessage("Student names: Alon Michail Tomer",client);
		}
		else if (msgString.startsWith("GetGrades")){
			HashMap<String, String> grades = new HashMap<>();
			grades.put("Alon", "100 100 100");
			grades.put("Michail", "99 99 99");
			grades.put("Tomer", "98 98 98");
			sendMessage("Grades: " + msgString.indexOf(" ") + 1 + " " + grades.get(msgString.indexOf(" ") + 1),client);

		}
	}
	protected void sendMessage(String msg, ConnectionToClient client){
		Message message = new Message(msg);
		try {
			client.sendToClient(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
