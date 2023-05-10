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
	HashMap<String, String> grades = new HashMap<>();
	@Override
	//Treating the message from the clint
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String msgString = msg.toString();
		//TODO remove test hashmap when the database is built
		if(grades.size() == 0){
			grades.put("Alon", "1 100 2 100 3 100 ");
			grades.put("Michail", "1 99 2 99 3 99 ");
			grades.put("Tomer", "1 98 2 98 3 98 ");
		}
		//Getting student names from the database and returning them to the client
		if (msgString.equals("GetNames")) {
			//TODO get student names from the database
			//message format: "Student names: <<name1>> <<name2>>... <<nameN>> "
			sendMessage("Student names: Alon Michail Tomer ", client);
		}
		//Getting relevant grades from the database and returning them to the client
		else if (msgString.startsWith("GetGrades")) {
			//TODO get grades from the database
			//message format: "Grades: <<name>> <<test_id1>> <<grade1>> <<test_id2>> <<grade2>>... <<gradeN>> "
			int index = msgString.indexOf(" ") + 1;
			String name = msgString.substring(index);
			sendMessage("Grades: " + name + " " + grades.get(name), client);

		}
		//Update grades and inform the clint whether the update succeeded or not
		else if (msgString.startsWith("UpdateGrade")) {
			//Validate the grades
			int index = msgString.indexOf(" ") + 1;
			Boolean flag = true;
			String name = msgString.substring(index, msgString.indexOf(" ", index));
			index = msgString.indexOf(" ", index) + 1;
			String update_str = msgString.substring(index);
			index = 0;
			while (index != update_str.length() && flag) {
				index = update_str.indexOf(" ", index) + 1;
				String grade = update_str.substring(index, update_str.indexOf(" ", index));
				if (!isGrade(grade)) {
					//If validation fails then send error message
					sendMessage("InputError",client);
					flag = false;
				}
				index = update_str.indexOf(" ", index) + 1;
			}
			//Extract the grades from the message and update the database
			if(flag){
				//TODO extract grades from the message and update database (extraction can be done during validation)
				grades.put(name,update_str);
				//If validation succeeds then send success message
				sendMessage("UpdateSuc",client);
			}
		}
	}
	//Send received message to the relevant client
	protected void sendMessage (String msg, ConnectionToClient client){
		Message message = new Message(msg);
		try {
			client.sendToClient(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//Validates grade input
	private static boolean isGrade (String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0 || (length > 2 && !str.equals("100"))) {
			return false;
		}
		for (int i = 0; i < length; i++) {
			char c = str.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}
}


