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
import javax.persistence.criteria.CriteriaBuilder;
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

	public static void updateGrades(String name, int test_id, String newGrade) throws Exception {
		try (Session session = getSessionFactory().openSession()) {
			String grades = getGradesAsString(name);
			grades = grades.replace("Grades: ", "").replace("[", "").replace("]", "");

			String[] parts = grades.split(" ");
			parts[test_id] = newGrade;

			grades=grades.join(" ", parts);

//			gradesAsString.set(test_id, newGrade);
//
			// Begin a transaction
			session.getTransaction().begin();
			Query query2 = session.createNativeQuery("UPDATE StudentsWithGrades SET grades = '" + grades + "' WHERE name = '" + name +"';");
			int rowsUpdated = query2.executeUpdate();

			// Commit the transaction
			session.getTransaction().commit();

			String msg = "Updated: ";
//			return msg;
		}
	}


	HashMap<String, String> grades = new HashMap<>();
	@Override
	//Treating the message from the clint
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) throws Exception {
		String msgString = msg.toString();
//		if(grades.size() == 0){
//			grades.put("Alon", "1 100 2 100 3 100 ");
//			grades.put("Michael", "1 99 2 99 3 99 ");
//			grades.put("Tomer", "1 98 2 98 3 98 ");
//		}
		//Getting student names from the database and returning them to the client
		if (msgString.equals("GetNames")) {
			//message format: "Student names: <<name1>> <<name2>>... <<nameN>> "
			String nameColumn = "name";
			try {
				sendMessage(getColumnAsString(nameColumn), client);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

//			sendMessage("Student names: Alon Michail Tomer ", client);
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
//			sendMessage("Grades: " + name + " " + grades.get(name), client);

		}
		//Update grades and inform the clint whether the update succeeded or not
		else if (msgString.startsWith("UpdateGrade")) {
			//Validate the grades
			int index = msgString.indexOf(" ") + 1;
			String name = msgString.substring(index, msgString.indexOf(" ", index));
			String[] parts = msgString.split(" ");
			String test_id_str = parts[2];
			int test_id =  Integer.parseInt(test_id_str);
			String grade_str = parts[3];
			int grade = Integer.parseInt(grade_str);

//			Boolean flag = true;
//			index = msgString.indexOf(" ", index) + 1;
//			String update_str = msgString.substring(index);
//			index = 0;
//			while (index != update_str.length() && flag) {
//				index = update_str.indexOf(" ", index) + 1;
//				String grade = update_str.substring(index, update_str.indexOf(" ", index));
//				if (!isGrade(grade)) {
//					//If validation fails then send error message
//					sendMessage("InputError",client);
//					flag = false;
//				}
//				index = update_str.indexOf(" ", index) + 1;
//			}
//			//Extract the grades from the message and update the database
//			if(flag){
				//TODO extract grades from the message and update database (extraction can be done during validation)


//				grades.put(name,update_str);
				//If validation succeeds then send success message
			updateGrades(name, test_id, grade_str);
			sendMessage("UpdateSuc",client);
//			}
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


