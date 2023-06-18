package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.*;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import org.hibernate.Session;

import javax.persistence.Query;
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

	//success message format: LogIn <<role>> <<username>> (role is one of the following options: "student", "teacher, "principle")
	//error message format: InputError <<error description>>

	private static String getTableName(String role)
	{
		if(role.equals("principle")) {
			return "Principals";
		}
		else if(role.equals("teacher")) {
			return "Teachers";
		}
		else if(role.equals("student"))
		{
				return "Pupils";
		}
		else
		{
			return "";
		}
	}
	private String logIn(String role, String username, String password)
	{
		String loginResultMessage;
		int count = 0;
		Boolean isLoggedIn = true;
		try (Session session = getSessionFactory().openSession()) {
			session.getTransaction().begin();
			Query query1 = session.createNativeQuery("select count(*) from "+ getTableName(role) +" where name = '" + username  + "' and password = '"+ password + "';");
			count = ((Number) query1.getSingleResult()).intValue();

			if(count > 0)
			{
				Query query2 = session.createNativeQuery("select isLoggedIn from "+ getTableName(role) +" where name = '" + username  + "' and password = '"+ password + "';");
				isLoggedIn = ((Boolean) query2.getSingleResult()).booleanValue();
			}

			// Commit the transaction
			session.getTransaction().commit();
		}

		if (count > 0)
		{
			if(!isLoggedIn)
			{
				loginResultMessage = "LogIn "+  role + " " + username;
				try (Session session = getSessionFactory().openSession()) {
					session.getTransaction().begin();

					Query query3 = session.createNativeQuery("UPDATE "+ getTableName(role) + " SET isLoggedIn = true" +" where name = '" + username  + "' and password = '"+ password + "';");
					int rowCount = query3.executeUpdate();

					// Commit the transaction
					session.getTransaction().commit();
				}
			}
			else
			{
				loginResultMessage = "InputError You are already logged in";
			}
		}
		else
		{
			loginResultMessage = "InputError The identification details are incorrect";
		}
		return loginResultMessage;
	}
	//Success message: EnterExam <<exam_description>>
	//Error message: InputError <<error_description>>
	private String connectToExam(String code){

		return "";
	}
	//Success message: StartExam
	//Error message: InputError <<error_description>>
	private String StartExam(String id, String name){
		return "";
	}
	private void LogOut(String name, String role){
		try (Session session = getSessionFactory().openSession()) {
			session.getTransaction().begin();
			Query query = session.createNativeQuery("UPDATE "+ getTableName(role) + " SET isLoggedIn = false" +" where name = '" + name + "';");
			query.executeUpdate();

			// Commit the transaction
			session.getTransaction().commit();
		}
	}
	private String GetStudentGrades(String name) {
		return "StudentGrades";
	}
	@Override
	//Treating the message from the clint
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) throws Exception {
		String msgString = msg.toString();
		//validates the entered information and either lets the respective user to proceed to the rest of the system
		//(based on its role) or sends a relevant error message
		if (msgString.startsWith("LogIn")) {
			String[] parts = msgString.split(" ");

			String username = parts[1];
			String password = parts[2];
			String role = parts[3];
			System.out.println("Checking login"); // for debugging
			String message = logIn(role, username, password);
			System.out.println("Finished checking login"); // for debugging
			//message = "LogIn Alon student";

			sendMessage(message,client);
		}
		else if (msgString.startsWith("EnterExam")) {
			String[] parts = msgString.split(" ");
			String code = parts[1];
			String message = connectToExam(code);
			if (code.compareTo("1234") == 0){

				message = "EnterExam";
			}
			else {
				message = "InputError The code you entered does not exist";

			}
			sendMessage(message,client);
		}
		else if (msgString.startsWith("StartExam")) {
			String[] parts = msgString.split(" ");

			String id = parts[1];
			String name = parts[2];
			String message = StartExam(id,name);
			message = "StartExam";
			sendMessage(message,client);
		}
		else if (msgString.startsWith("LogOut")) {
			String[] parts = msgString.split(" ");
			String name = parts[1];
			String role = parts[2];
			LogOut(name,role);
			sendMessage("LogOut",client);
		}
		else if (msgString.startsWith("GetStudentGrades")) {
			String[] parts = msgString.split(" ");
			String name = parts[1];
			String grades = GetStudentGrades(name);
			sendMessage(grades,client);
		}
		else if (msgString.startsWith("getAllQuestion")) {

			System.out.println("in begining");
			sendMessage(getQuesitons(),client);
		}
		else if (msgString.startsWith("getAllExams")) {

			System.out.println("getAlllllExamsss");
			sendMessage(getExam(),client);
		}
		else if (msgString.startsWith("GetQusetionOfExam")) {
			String[] parts = msgString.split(":");
			String id = parts[1];
			System.out.println("id is:"+id);
			sendMessage(getQuesitonsFromExam(id),client);
		}
		else if (msgString.startsWith("Get all teachers"))
		{
			sendMessage(getAllTeachers(),client);
		}
		else if (msgString.startsWith("Get all students"))
		{
			sendMessage(getAllStudents(),client);
		}
		else if (msgString.startsWith("Get all courses"))
		{
			sendMessage(getAllCourses(),client);
		}
		else if (msgString.startsWith("distribution"))
		{
			int nameStartIndex = msgString.indexOf(":") + 1; // Find the index of ':' and add 1 to exclude it
			int nameEndIndex = msgString.indexOf(" and"); // Find the index of ' and' to determine the end of the name
			String name = msgString.substring(nameStartIndex, nameEndIndex);

// Extract the value of i
			int numberStartIndex = msgString.lastIndexOf(":") + 1; // Find the index of the last ':' and add 1 to exclude it
			String i = msgString.substring(numberStartIndex);
			System.out.println("the name is:"+name+"and the i is "+i);
			sendMessage(getDistribution(name,i),client);

		}
		else if (msgString.startsWith("Specific exam with code:"))
		{
			String code = msgString.substring(msgString.indexOf(":") + 1).trim();
			sendMessage(getSpecificExam(code),client);
		}
	}
	public static String getSpecificExam(String code) throws Exception {
		String grades="all the grades are:";
		try {
			Session session = getSessionFactory().openSession();
			int intCode = Integer.parseInt(code);
			System.out.println("the code is:"+code);
			Query query = session.createNativeQuery("SELECT id  FROM Exams where exam_code_number='" + code + "'");
			List<String> id = query.getResultList();
			String idIs=id.toString();
			idIs=idIs.replace("[","").replace("]","");
			System.out.println("hereee");
			Query query2 = session.createNativeQuery("SELECT id  FROM ReadyExams where exam_id='" + Integer.parseInt(idIs) + "'");
			List<String> question2 = query2.getResultList();
			String readyExamsIds=question2.toString();
			readyExamsIds=readyExamsIds.replace("[","").replace("]","");
			String[] numbersArray = readyExamsIds.split(",");
			for (int i = 0; i < numbersArray.length; i++) {
				numbersArray[i] = numbersArray[i].trim();
			}
			for (int i = 0; i < numbersArray.length; i++) {
				Query query3 = session.createNativeQuery("SELECT the_grade  FROM Grades where readyExam_id='" + Integer.parseInt(numbersArray[i]) + "'");
				List<String> question3 = query3.getResultList();
				String allTheGrades = question3.toString();
				allTheGrades = allTheGrades.replace("[", "").replace("]", "");
				grades = grades + " "+allTheGrades;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("grades is:"+grades);
		return  grades;
	}

	public static String getAllStudents() throws Exception {
		String msg="Student names are:";
		String message = "";
		int count = 0;
		try {
			Session session = getSessionFactory().openSession();
			Query query = session.createNativeQuery("SELECT name  FROM Pupils");
			List<String> name = query.getResultList();
			String names=name.toString();
			names=names.replace(",", "(1)");
			System.out.println("student name" + names);
			msg=msg+names;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  msg;
	}
	public static String getDistribution(String names,String mid) throws Exception {
		String grades="" ;
		String summerize="All the distrubutions:";
		String forExam="";
		String allGrades="grades:";
		String allReadyExams="";
		System.out.println("hellloooo");
		System.out.println("the mid is:"+mid);
		String fixedString;
		String allExamsWithGrades;
		String[]allExamsOfTecher;
		try {

			Session session = getSessionFactory().openSession();
			if (mid.equals("1")) {
				System.out.println("hellloooo2");
				System.out.println("string is:" + names);
				Query query = session.createNativeQuery("SELECT teacher_id FROM Teachers where name='" + names + "'");
				List<String> question = query.getResultList();
				System.out.println("hellloooo3");
				fixedString = question.toString();
				fixedString = fixedString.replace("[", "").replace("]", "").replace(" ", "");
				System.out.println(fixedString);
				Query query2 = session.createNativeQuery("SELECT id FROM Exams where teacher_id=" + Integer.parseInt(fixedString) + "");
				List<String> question2 = query2.getResultList();
				String morf = question2.toString().replace("[", "").replace("]", "").replace(" ", "");
				String[] numbersArray = morf.split(",");

				for (int i = 0; i < numbersArray.length; i++) {
					numbersArray[i] = numbersArray[i].trim();
					System.out.println("exams ids of" + names + "are" + numbersArray[i]);
				}

				for (int i = 0; i < numbersArray.length; i++) {
					String scores = "";
					System.out.println("before fail?");
					int x = Integer.parseInt(numbersArray[i]);
					Query query7 = session.createNativeQuery("SELECT exam_code_number FROM Exams where id=" + x + "");
					List<String> question7 = query7.getResultList();
					System.out.println("fail");
					String extract = question7.toString();
					extract = extract.replace("]", "").replace("[", "");
					summerize = summerize + " code is: " + extract;
					Query query8 = session.createNativeQuery("SELECT exam_name FROM Exams where id=" + x + "");
					List<String> question8 = query8.getResultList();
					System.out.println("fail");
					String extract2 = question8.toString();
					extract2 = extract2.replace("]", "").replace("[", "");
					summerize = summerize + " name is: " + extract2;
					System.out.println(summerize);
					System.out.println("fail2");
					Query query3 = session.createNativeQuery("SELECT id FROM ReadyExams where exam_id=" + Integer.parseInt(numbersArray[i]) + "");
					List<String> question3 = query3.getResultList();
					String readyExamss = question3.toString();
					System.out.println("fail3");
					readyExamss = readyExamss.replace("[", "").replace("]", "").replace(" ", "");
					String[] numbersArr = readyExamss.split(",");// all the exams of teacher name
					for (int j = 0; j < numbersArr.length; j++) {
						numbersArr[j] = numbersArr[j].trim();
						System.out.println("value of equl to:" + numbersArr[j]);
					}
					System.out.println("fail4");
					for (int j = 0; j < numbersArr.length; j++) {
						Query query4 = session.createNativeQuery("SELECT the_grade FROM Grades where readyExam_id=" + Integer.parseInt(numbersArr[j]) + "");
						List<String> question4 = query4.getResultList();
						grades = question4.toString();
						grades = grades.replace("[", "").replace("]", "");
					}
					summerize = summerize + " all the scores are: " + grades;
				}
				System.out.println(summerize);
			}
			else if(mid.equals("2"))
			{
				String gr="";
				System.out.println("in mid 2");
				String grd="";
				System.out.println("here1");
				Query query = session.createNativeQuery("SELECT id FROM Courses where course_name='" + names + "'");
				List<String> question = query.getResultList();
				String courseId=question.toString();
				courseId=courseId.replace("[","").replace("]","");
				System.out.println("here3");
				Query query1 = session.createNativeQuery("SELECT id FROM Exams where course_id='" + courseId + "'");
				System.out.println("here4");
				List<String> question1 = query1.getResultList();
				String listOfExamIds=question1.toString();
				listOfExamIds=listOfExamIds.replace("[","").replace("]","");
				System.out.println("here2");
				String[] numbersArray = listOfExamIds.split(",");
				for (int i = 0; i < numbersArray.length; i++) {
					numbersArray[i] = numbersArray[i].trim();
					System.out.println("exams ids of" + names + "are" + numbersArray[i]);
				}
				for(int i=0;i<numbersArray.length;i++)
				{
					int x=Integer.parseInt(numbersArray[i]);
					Query query7 = session.createNativeQuery("SELECT exam_code_number FROM Exams where id=" + x + "");
					List<String> question7 = query7.getResultList();
					System.out.println("fail");
					String extract = question7.toString();
					extract = extract.replace("]", "").replace("[", "");
					summerize = summerize + " code is: " + extract;
					Query query8 = session.createNativeQuery("SELECT exam_name FROM Exams where id=" + x + "");
					List<String> question8 = query8.getResultList();
					System.out.println("fail");
					String extract2 = question8.toString();
					extract2 = extract2.replace("]", "").replace("[", "");
					summerize = summerize + " name is: " + extract2;
					Query query3 = session.createNativeQuery("SELECT id FROM ReadyExams where exam_id=" + Integer.parseInt(numbersArray[i]) + "");
					List<String> question3 = query3.getResultList();
					String listOfReayExams=question3.toString();
					listOfReayExams=listOfReayExams.replace("[","").replace("]","");
					String[] numbersArr = listOfReayExams.split(",");
					for (int j = 0; j < numbersArr.length; j++) {
						numbersArray[j] = numbersArray[j].trim();
						System.out.println("exams ids of" + names + "are" + numbersArr[i]);
					}
					for (int j = 0; j < numbersArr.length; j++) {
						Query query4 = session.createNativeQuery("SELECT the_grade FROM Grades where readyExam_id=" + Integer.parseInt(numbersArr[j]) + "");
						List<String> question4 = query4.getResultList();
						gr=question4.toString();
						gr=gr.replace("[","").replace("]","").replace(",","(1)");
					}
					summerize=summerize+" all the scores are: "+gr;

				}
				System.out.println(summerize);
			}
			else {
				String examName = "";
				String grade = "";
				String examCode = "";
				Query query = session.createNativeQuery("SELECT id FROM Pupils where name='" + names + "'");
				List<String> question = query.getResultList();
				String studeId = question.toString();
				System.out.println("student id is: "+studeId);
				studeId = studeId.replace("[", "").replace("]", "");
				Query query11 = session.createNativeQuery("SELECT id FROM Grades where pupil_id='" + studeId + "'");
				List<String> question10 = query11.getResultList();
				String allGradesIdSs=question10.toString();
				System.out.println("ALL GRADES::"+allGradesIdSs);
				allGradesIdSs = allGradesIdSs.replace("[", "").replace("]", "");
				String[] numbersArray = allGradesIdSs.split(",");
				for (int i = 0; i < numbersArray.length; i++) {
					numbersArray[i] = numbersArray[i].trim();
					System.out.println("exams ids of" + names + "are" + numbersArray[i]);
				}
				for (int i = 0; i < numbersArray.length; i++) {
					Query query6 = session.createNativeQuery("SELECT the_grade FROM Grades where id='" + Integer.parseInt(numbersArray[i]) + "'");
					List<String> question6 = query6.getResultList();
					grade = question6.toString();
					grade = grade.replace("[", "").replace("]", "");
					Query query2 = session.createNativeQuery("SELECT readyExam_id FROM Grades where id='" + Integer.parseInt(numbersArray[i]) + "'");
					List<String> question2 = query2.getResultList();
					String numberOfReadyExam = question2.toString();
					numberOfReadyExam = numberOfReadyExam.replace("[", "").replace("]", "");
					Query query3 = session.createNativeQuery("SELECT exam_id FROM ReadyExams where id='" + Integer.parseInt(numberOfReadyExam) + "'");
					List<String> question3 = query3.getResultList();
					String numberOfExamId = question3.toString();
					numberOfExamId = numberOfExamId.replace("[", "").replace("]", "");
					Query query4 = session.createNativeQuery("SELECT exam_code_number FROM Exams where id='" + Integer.parseInt(numberOfExamId) + "'");
					List<String> question4 = query4.getResultList();
					examCode = question4.toString();
					examCode = examCode.replace("[", "").replace("]", "");
					Query query5 = session.createNativeQuery("SELECT exam_name FROM Exams where id='" + Integer.parseInt(numberOfExamId) + "'");
					List<String> question5 = query5.getResultList();
					examName = question5.toString();
					examName=examName.replace("[", "").replace("]", "");
					summerize =summerize+ " code is: " + examCode + " name is: " + examName + " all the scores are:" + grade;
				}
				System.out.println(summerize);
			}

		}
		catch (Exception e)
		{
			System.out.println("error0");
			e.printStackTrace();
		}

		return summerize;

	}
	public static String getAllCourses() throws Exception {
		String msg="Courses names are:";
		String message = "";
		int count = 0;
		try {
			Session session = getSessionFactory().openSession();
			Query query = session.createNativeQuery("SELECT course_name  FROM Courses");
			List<String> name = query.getResultList();
			String names=name.toString();
			names=names.replace(",", "(1)");
			System.out.println("course name" + names);
			msg=msg+names;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  msg;
	}
	public static String getAllTeachers() throws Exception {
		String msg="Teachers names are:";
		String message = "";
		int count = 0;
		try {
			Session session = getSessionFactory().openSession();
			Query query = session.createNativeQuery("SELECT name  FROM Teachers");
			List<String> name = query.getResultList();
			String names=name.toString();
			names=names.replace(",", "(1)");
			System.out.println("teacher name" +names );
			msg=msg+names;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  msg;
	}
	public static String getQuesitonsFromExam(String id) throws Exception {// sub the ,
		String message = "question in exam:";
		int count=0;
		int length;
		try {
			Session session = getSessionFactory().openSession();


	/*working:Query query2 = session.createNativeQuery("SELECT answer_text  FROM Answers where id="+c+"");
		      List<String> gradesList2=query2.getResultList();
			  System.out.println("printt"+gradesList2);*/

			Query query2 = session.createNativeQuery("SELECT question_id  FROM exam_question where exam_id="+id+"");
			//Query query4 = session.createNativeQuery("SELECT subject_name FROM Subjects where subject_id = '" + 0 +"'");
			List<String> gradesList2=query2.getResultList();
			String idOfqQuestion=gradesList2.toString();
			idOfqQuestion=idOfqQuestion.replace("[", "").replace("]", "").replace(",","").replace(" ","");
			length=idOfqQuestion.length();
			System.out.println("the length is:"+length);
			message=message+String.valueOf(length);
			for(int i=0;i<length;i++)
			{
				count++;
				int numOfQuestion=idOfqQuestion.charAt(0)-48;
				Query query = session.createNativeQuery("SELECT question_text  FROM Questions where id="+numOfQuestion+"");
				List<String> question=query.getResultList();
				System.out.println("question is"+question);
				message=message+"next question:"+question;
				Query query3 = session.createNativeQuery("SELECT answer_text  FROM Answers where question_id="+numOfQuestion+"");
				List<String> answers=query3.getResultList();
				String _answers=answers.toString();
				_answers=_answers.replace(",","("+(1)+")");
				System.out.println("answers"+_answers);
				message=message+"and the answers:"+_answers;
				if(count<length) {
					idOfqQuestion = idOfqQuestion.substring(1);
				}
				System.out.println("now the string of question is:"+idOfqQuestion);


			}
		}
		catch (Exception e) {

			System.out.println("error");

		}

		System.out.println(message);
		return message;
	}
	public static String getExam() throws Exception {//i changed this::
		String message="";
		int count=0;
		try {
			Session session = getSessionFactory().openSession();


			/*Query query2 = session.createNativeQuery("SELECT id  FROM Exams");
			List<String> gradesList2=query2.getResultList();
			String idOfExams=gradesList2.toString();
			idOfExams=idOfExams.replace("[", "").replace("]", "").replace(",","").replace(" ","");
			int exam_number=idOfExams.length();
			System.out.println("a value is:"+idOfExams);
			message="number of Exams:"+idOfExams.length();
			for(int i = 0; i<exam_number; i++) {
				count++;
				int numOfExams = idOfExams.charAt(0) - 48;
				System.out.println("b value is:" + numOfExams);
				Query query = session.createNativeQuery("SELECT exam_code_number  FROM Exams where id=" + numOfExams + "");
				List<String> question = query.getResultList();
				System.out.println("exam code is:" + question);
				message = message + " exam code is:" + question;
				Query query1 = session.createNativeQuery("SELECT exam_name  FROM Exams where id=" + numOfExams + "");
				List<String> question1 = query1.getResultList();
				System.out.println("exam name is:" + question1);
				message = message + " exam name is:" + question1;
				if (count < exam_number) {
					idOfExams = idOfExams.substring(1);
				}
				System.out.println("now the string of question is:" + idOfExams);
			}*/
			Query query2 = session.createNativeQuery("SELECT id  FROM Exams");
			List<String> gradesList2=query2.getResultList();
			String idOfExams=gradesList2.toString();
			idOfExams=idOfExams.replace("]","").replace("[","");
			String[] numbersArray = idOfExams.split(",");
			for (int i = 0; i < numbersArray.length; i++) {
				numbersArray[i] = numbersArray[i].trim();
				System.out.println("Array value is:"+numbersArray[i]);
			}
			int exam_number=numbersArray.length;
			message="number of Exams:"+idOfExams.length();
			for(int i = 0; i<numbersArray.length; i++) {
				Query query = session.createNativeQuery("SELECT exam_code_number  FROM Exams where id=" + Integer.parseInt(numbersArray[i]) + "");
				List<String> question = query.getResultList();
				System.out.println("exam code is:" + question);
				message = message + " exam code is:" + question;
				Query query1 = session.createNativeQuery("SELECT exam_name  FROM Exams where id=" + Integer.parseInt(numbersArray[i]) + "");
				List<String> question1 = query1.getResultList();
				System.out.println("exam name is:" + question1);
				message = message + " exam name is:" + question1;
				/*if (count < exam_number) {
					idOfExams = idOfExams.substring(1);
				}
				System.out.println("now the string of question is:" + idOfExams);*/
			}



		}
		catch (Exception e) {

			System.out.println("error");

		}

		System.out.println(message);
		return message;
	}
	public static String getQuesitons() throws Exception {//have to check this----------------,
		int count = 0, question_number = 0;
		char c = '1';
		int d = c - 48;
		int x = c - 48;
		String message = "";
		try {
			Session session = getSessionFactory().openSession();

		/*    Query query2 = session.createNativeQuery("SELECT id  FROM Questions");
		    List<String> gradesList2=query2.getResultList();
		    String idOfqQuestion=gradesList2.toString();
			idOfqQuestion=idOfqQuestion.replace("[", "").replace("]", "").replace(",","").replace(" ","");
			question_number=idOfqQuestion.length();
			System.out.println("a value is:"+idOfqQuestion);
			message="number of questions:"+idOfqQuestion.length();
			for(int i=0;i<question_number;i++)
			{
				count++;
				int numOfQuestion=idOfqQuestion.charAt(0)-48;
				System.out.println("b value is:"+numOfQuestion);
				Query query = session.createNativeQuery("SELECT question_text  FROM Questions where id="+numOfQuestion+"");
				List<String> question=query.getResultList();
				System.out.println("question is"+question);
				message=message+"next question:"+question;
				Query query3 = session.createNativeQuery("SELECT answer_text  FROM Answers where question_id="+numOfQuestion+"");
				List<String> answers=query3.getResultList();
				String _answers=answers.toString();
				_answers=_answers.replace(",","("+(1)+")");
				System.out.println("answers"+_answers);
				message=message+"and the answers:"+_answers;
				if(count<question_number) {
					idOfqQuestion = idOfqQuestion.substring(1);
				}
				System.out.println("now the string of question is:"+idOfqQuestion);

			}*/
			Query query2 = session.createNativeQuery("SELECT id  FROM Questions");
			List<String> gradesList2=query2.getResultList();
			String idOfqQuestion=gradesList2.toString();
			idOfqQuestion=idOfqQuestion.replace("]","").replace("[","");
			String[] numbersArray = idOfqQuestion.split(",");
			for (int i = 0; i < numbersArray.length; i++) {
				numbersArray[i] = numbersArray[i].trim();
				System.out.println("Array value is:" + numbersArray[i]);
			}
			message="number of questions:"+numbersArray.length;
			for(int i=0;i<numbersArray.length;i++)
			{
				Query query = session.createNativeQuery("SELECT question_text  FROM Questions where id="+Integer.parseInt(numbersArray[i])+"");
				List<String> question=query.getResultList();
				System.out.println("question is"+question);
				message=message+"next question:"+question;
				Query query3 = session.createNativeQuery("SELECT answer_text  FROM Answers where question_id="+Integer.parseInt(numbersArray[i])+"");
				List<String> answers=query3.getResultList();
				String _answers=answers.toString();
				_answers=_answers.replace(",","("+(1)+")");
				System.out.println("answers"+_answers);
				message=message+"and the answers:"+_answers;
			}
		}
		catch (Exception e) {

			System.out.println("error");

		}

		System.out.println(message);
		return message;
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


