package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.server.entities.Pupil;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import static il.cshaifasweng.OCSFMediatorExample.server.App.getSessionFactory;

public class SimpleServer extends AbstractServer {

	public SimpleServer(int port) {
		super(port);

	}

	public static String getColumnAsString(String columnName) throws Exception {
		try (Session session = getSessionFactory().openSession()) {
			Query query = session.createNativeQuery("SELECT name FROM StudentsWithGrades");
			List<String> names = query.getResultList();
			String msg = "Student names: ";
			return msg+names.toString();
		}
	}
	public static String getGradesAsString(String name) throws Exception {
		try (Session session = getSessionFactory().openSession()) {
			Query query = session.createNativeQuery("SELECT grades FROM StudentsWithGrades where name = '" + name +"'");

			List<byte[]> gradesList = query.getResultList();

			// Convert byte arrays to strings
			List<String> gradesAsString = new ArrayList<>();
			for (byte[] gradeBytes : gradesList) {
				String gradeString = new String(gradeBytes);
				gradesAsString.add(gradeString);
			}

			String msg = "Grades: " + gradesAsString.toString();
			return msg;
		}
	}

	public static void updateGrades(/* String name, int test_id, String newGrade*/ String msg) throws Exception {
		try (Session session = getSessionFactory().openSession()) {

			String[] parts_name = msg.split(" ");
			String command = parts_name[0];
			String name = parts_name[1];
			msg = msg.replace(command +" ", "").replace(name+" ", "");

			// Extract the numbers from the input string
			String[] parts = msg.split("\\s+");
			List<Integer> numbers = new ArrayList<>();

			for (int i = 2; i < parts.length; i += 2) {
				numbers.add(Integer.parseInt(parts[i]));
			}
			session.getTransaction().begin();

			String[] test_id_array = new String[parts.length/2];
			String[] grades_array = new String[parts.length/2];

			for (int i = 0; i < parts.length; i++) {
				if (i % 2 == 0) {
					test_id_array[i / 2] = parts[i];
				} else {
					grades_array[i / 2] = parts[i];
				}
			}

			String newGrades = String.join(" ", grades_array);
			Query query2 = session.createNativeQuery("UPDATE StudentsWithGrades SET grades = '" + newGrades+ "' WHERE name = '" + name +"';");
			int rowsUpdated = query2.executeUpdate();
				// Begin a transaction
//				session.getTransaction().begin();
//				Query query2 = session.createNativeQuery("UPDATE StudentsWithGrades SET grades = '" + grades + "' WHERE name = '" + name +"';");
//				int rowsUpdated = query2.executeUpdate();

			// Commit the transaction
			session.getTransaction().commit();
		}
	}
	//TODO check if the username and password match the ones in the database (in the role's table) and whether the user is not already logged in
	//TODO if the user is valid and not already logged in then let him log in and return success message else return error message
	//success message format: LogIn <<username>> <<role>> (role is one of the following options: "student", "teacher, "principle")
	//error message format: InputError <<error description>>
	private String logIn(String username, String password, String role){
		return "";
	}
	HashMap<String, String> grades = new HashMap<>();
	@Override
	//Treating the message from the clint
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) throws Exception {
		String msgString = msg.toString();
		//Getting student names from the database and returning them to the client
		if (msgString.equals("GetNames")) {
			//message format: "Student names: <<name1>> <<name2>>... <<nameN>> "
			String nameColumn = "name";
			try {
				sendMessage(getColumnAsString(nameColumn), client);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}
		//Getting relevant grades from the database and returning them to the client
		else if (msgString.startsWith("GetGrades")) {
			//message format: "Grades: <<name>> <<test_id1>> <<grade1>> <<test_id2>> <<grade2>>... <<gradeN>> "
			int index = msgString.indexOf(" ") + 1;
			String name = msgString.substring(index);
			try {
				sendMessage("Grades: " + name + " " + getGradesAsString(name), client);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}
		//Update grades and inform the client whether the update succeeded or not
		else if (msgString.startsWith("UpdateGrade")) {
			//Validate the grades
			int index = msgString.indexOf(" ") + 1;
			String name = msgString.substring(index, msgString.indexOf(" ", index));
			String[] parts = msgString.split(" ");
			String test_id_str = parts[2];
			int test_id =  Integer.parseInt(test_id_str);
			String grade_str = parts[3];
			int grade = Integer.parseInt(grade_str);
			updateGrades(msgString);
			sendMessage("UpdateSuc",client);
		}
		//validates the entered information and either lets the respective user to proceed to the rest of the system
		//(based on its role) or sends a relevant error message
		else if (msgString.startsWith("LogIn")) {
			String[] parts = msgString.split(" ");
			String message = logIn(parts[1],parts[2],parts[3]);
			message = "LogIn Alon student";
			sendMessage(message,client);
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


