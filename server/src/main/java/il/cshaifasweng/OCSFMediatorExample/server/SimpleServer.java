package il.cshaifasweng.OCSFMediatorExample.server;

//import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.*;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

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
			//TODO Uncomment the if (and remove the current one) after adding logout option
			if (true) //if(!isLoggedIn)
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
	@Override
	//Treating the message from the client
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) throws Exception {
		String msgString = msg.toString();
		System.out.println("in handleMessageFromClient");
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
		else if (msgString.startsWith("get all subjects")) {
			System.out.println("in handleMessageFromClient -> if (get all subjects)");
			String data = connectToDatabase(msgString.replaceFirst("get all subjects", ""));
			sendMessage("Here are all subjects" + data.substring(3), client);
		}
		else if (msgString.startsWith("get all courses")) {
			String data = connectToDatabase_Courses(msgString.replaceFirst("get all courses", ""));
			sendMessage("Here are all courses" + data.substring(3), client);
		}
		else if (msgString.startsWith("Get All Courses For Exam"))
		{
			System.out.println("after Get All Courses For Exam");
			String data = connectToDatabase_Courses_for_building_exam(msgString.replaceFirst("Get All Courses For Exam", ""));
			sendMessage("Here are all courses1" + data.substring(3), client);
		}
		else if (msgString.startsWith("Get All Questions For Exam"))
		{
			System.out.println("after Get All Questions For Exam");
			String data = connectToDatabase_Questions(msgString.replaceFirst("Get All Questions For Exam", ""));
			sendMessage("Here are all questions1" + data.substring(3), client);
		}
		else if (msgString.startsWith("save basic question"))
		{
			System.out.println("Entered saving a basic question");
			addQuestion(msgString.substring(19));
		}
		else if (msgString.startsWith("save course-question"))
		{
			System.out.println("Entered saving a Course to a question");
			addCourseToQuestion(msgString.substring(20));
		}
		else if (msgString.startsWith("save basic exam"))
		{
			System.out.println("Entered saving a basic exam");
			addExam(msgString.substring(15));
		}
		else if (msgString.startsWith("save exam-question"))
		{
			System.out.println("Entered saving a question to an exam");
			addQuestionToExam(msgString.substring(18));
		}
	}

	private String connectToDatabase(String teacher_name) {
		try (Session session = getSessionFactory().openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			///////////////////////////////////////////////// locate appropriate subjects to current teachers
			CriteriaQuery<Teacher> new_query = builder.createQuery(Teacher.class);
			new_query.from(Teacher.class);
			List<Teacher> new_data = session.createQuery(new_query).getResultList();
			List<Subject> allowed_subjects;
			for (Teacher t : new_data)
			{
				if (t.getName().equals(teacher_name))
				{
					allowed_subjects = t.getSubjects();
					String strings = "";
					for (Subject sub : allowed_subjects)
					{
						strings += ("___" + sub.getName());
					}
					return strings;
				}
			}
			/////////////////////////////////////////////////
			/*CriteriaQuery<Subject> query = builder.createQuery(Subject.class);
			query.from(Subject.class);
			List<Subject> data = session.createQuery(query).getResultList();
			String strings = "";
			for (Subject sub : data)
			{
				strings += ("___" + sub.getName());
			}
			return strings;*/
			return null;
		}
	}

	private String connectToDatabase_Courses(String subject_name) {
		try (Session session = getSessionFactory().openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			/////////////////////////////////////////
			/*CriteriaQuery<Teacher> new_query = builder.createQuery(Teacher.class);
			new_query.from(Teacher.class);
			List<Teacher> new_data = session.createQuery(new_query).getResultList();
			List<Subject> allowed_subjects;
			for (Teacher t : new_data)
			{
				if (t.getName().equals(teacher_name))
				{
					allowed_subjects = t.getSubjects();
					List<Course> allowed_courses = new ArrayList<Course>();
					for (Subject sub : allowed_subjects)
					{
						allowed_courses.addAll(sub.getCourses());
					}
					String strings = "";
					for (Course c : allowed_courses)
					{
						strings += ("___" + c.getName());
					}
					return strings;
				}
			}*/
			CriteriaQuery<Subject> new_query = builder.createQuery(Subject.class);
			new_query.from(Subject.class);
			List<Subject> new_data = session.createQuery(new_query).getResultList();
			for (Subject s : new_data)
			{
				if (s.getName().equals(subject_name))
				{
					String strings = "";
					List<Course> allowed_courses = s.getCourses();
					for (Course c : allowed_courses)
					{
						strings += ("___" + c.getName());
					}
					return strings;
				}
			}
			/////////////////////////////////////////
			/*CriteriaQuery<Course> query = builder.createQuery(Course.class);
			query.from(Course.class);
			List<Course> data = session.createQuery(query).getResultList();
			String strings = "";
			for (Course sub : data)
			{
				strings += ("___" + sub.getName());
			}
			return strings;*/
			return null;
		}
	}

	private String connectToDatabase_Courses_for_building_exam(String teacher_name) {
		try (Session session = getSessionFactory().openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			/////////////////////////////////////////
			CriteriaQuery<Teacher> new_query = builder.createQuery(Teacher.class);
			new_query.from(Teacher.class);
			List<Teacher> new_data = session.createQuery(new_query).getResultList();
			List<Subject> allowed_subjects;
			for (Teacher t : new_data)
			{
				if (t.getName().equals(teacher_name))
				{
					allowed_subjects = t.getSubjects();
					List<Course> allowed_courses = new ArrayList<Course>();
					for (Subject sub : allowed_subjects)
					{
						allowed_courses.addAll(sub.getCourses());
					}
					String strings = "";
					for (Course c : allowed_courses)
					{
						strings += ("___" + c.getName());
					}
					System.out.println("about to return string: " + strings);
					return strings;
				}
			}
			/*CriteriaQuery<Subject> new_query = builder.createQuery(Subject.class);
			new_query.from(Subject.class);
			List<Subject> new_data = session.createQuery(new_query).getResultList();
			for (Subject s : new_data)
			{
				if (s.getName().equals(subject_name))
				{
					String strings = "";
					List<Course> allowed_courses = s.getCourses();
					for (Course c : allowed_courses)
					{
						strings += ("___" + c.getName());
					}
					return strings;
				}
			}*/
			/////////////////////////////////////////
			/*CriteriaQuery<Course> query = builder.createQuery(Course.class);
			query.from(Course.class);
			List<Course> data = session.createQuery(query).getResultList();
			String strings = "";
			for (Course sub : data)
			{
				strings += ("___" + sub.getName());
			}
			return strings;*/
			return null;
		}
	}

	private String connectToDatabase_Questions(String course_name) {
		try (Session session = getSessionFactory().openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			////////////////////////////////////////////
			CriteriaQuery<Course> new_query = builder.createQuery(Course.class);
			new_query.from(Course.class);
			List<Course> new_data = session.createQuery(new_query).getResultList();
			for (Course c : new_data)
			{
				if (c.getName().equals(course_name))
				{
					List<Question> questions = c.getQuestions();
					String strings = "";
					for (Question q : questions)
					{
						strings += ("___" + q.getText());
					}
					return strings;
				}
			}
			////////////////////////////////////////////
			/*CriteriaQuery<Question> query = builder.createQuery(Question.class);
			query.from(Question.class);
			List<Question> data = session.createQuery(query).getResultList();
			String strings = "";
			for (Question q : data)
			{
				strings += ("___" + q.getText());
			}
			return strings;*/
			return null;
		}
	}

	private void addQuestion(String description_string)
	{
		try (Session session = getSessionFactory().openSession()) {
			session.getTransaction().begin();
			String[] substrings = description_string.split("---");
			String text = substrings[0];
			Answer ans1 = new Answer(substrings[1]);          // new answers
			Answer ans2 = new Answer(substrings[2]);
			Answer ans3 = new Answer(substrings[3]);
			Answer ans4 = new Answer(substrings[4]);
			session.save(ans1);
			session.save(ans2);
			session.save(ans3);
			session.save(ans4);
			session.flush();
			String subject_name = substrings[5];
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Subject> query = builder.createQuery(Subject.class);
			query.from(Subject.class);
			List<Subject> data = session.createQuery(query).getResultList();
			Subject subject = null;
			for (Subject sub : data)
			{
				if (sub.getName().equals(subject_name))
				{
					subject = sub;
					break;
				}
			}
			Question question = new Question(text, ans1, ans2, ans3, ans4, subject);
			session.save(question);
			session.flush();
			question.updateCode();
			session.save(question);
			session.flush();
			session.getTransaction().commit();
		}
	}

	private void addExam(String description_string)
	{
		try (Session session = getSessionFactory().openSession()) {
			session.getTransaction().begin();
			String[] substrings = description_string.split("@@@");
			String name = substrings[0];
			String course_name = substrings[1];

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Course> query1 = builder.createQuery(Course.class);
			query1.from(Course.class);
			List<Course> data = session.createQuery(query1).getResultList();
			Course course = null;
			for (Course c : data)
			{
				if (c.getName().equals(course_name))
				{
					course = c;
					break;
				}
			}
//        TypedQuery<Course> query1 = getSessionFactory().createEntityManager().createQuery()//.createQuery("from Course where name = " + course_name);
//        List<Course> courses = query1.getResultList();
//        Course course = courses.get(0);
			int duration_in_minutes = Integer.valueOf(substrings[2]);
			String note_to_students = substrings[3];
			String note_to_teacher = substrings[4];
			String teacher_name = substrings[5];

			CriteriaQuery<Teacher> query2 = builder.createQuery(Teacher.class);
			query2.from(Teacher.class);
			List<Teacher> data1 = session.createQuery(query2).getResultList();
			Teacher teacher = null;
			for (Teacher t : data1)
			{
				if (t.getName().equals(teacher_name))
				{
					teacher = t;
					break;
				}
			}

			Exam exam = new Exam(name, course, duration_in_minutes, note_to_students, note_to_teacher, teacher);
			session.save(exam);
			session.flush();
			exam.updateCode();
			session.save(exam);
			session.flush();
			session.getTransaction().commit();
		}
	}

	private void addCourseToQuestion(String description_string)
	{
		try (Session session = getSessionFactory().openSession()) {
			session.getTransaction().begin();
			String[] substrings = description_string.split("```");
			String question_text = substrings[0];
			String course_text = substrings[1];
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Question> query1 = builder.createQuery(Question.class);
			query1.from(Question.class);
			List<Question> data1 = session.createQuery(query1).getResultList();
			Question question = null;
			for (Question q : data1)
			{
				if (q.getText().equals(question_text))
				{
					question = q;
					break;
				}
			}

			CriteriaQuery<Course> query2 = builder.createQuery(Course.class);
			query2.from(Course.class);
			List<Course> data2 = session.createQuery(query2).getResultList();
			Course course = null;
			for (Course c : data2)
			{
				if (c.getName().equals(course_text))
				{
					course = c;
					break;
				}
			}
			question.addCourse(course);
			session.save(question);
			session.flush();
			session.getTransaction().commit();
		}
	}

	private void addQuestionToExam(String description_string)
	{
		try (Session session = getSessionFactory().openSession()) {
			session.getTransaction().begin();
			String[] substrings = description_string.split("&&&");
			String exam_name = substrings[0];
			String question_name = substrings[1];
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Exam> query1 = builder.createQuery(Exam.class);
			query1.from(Exam.class);
			List<Exam> data = session.createQuery(query1).getResultList();
			Exam exam = null;
			for (Exam e : data)
			{
				if (e.getName().equals(exam_name))
				{
					exam = e;
					break;
				}
			}
			CriteriaQuery<Question> query2 = builder.createQuery(Question.class);
			query2.from(Question.class);
			List<Question> data1 = session.createQuery(query2).getResultList();
			Question question = null;
			for (Question q : data1)
			{
				if (q.getText().equals(question_name))
				{
					question = q;
					break;
				}
			}
			session.save(exam.addQuestion(question, Integer.valueOf(substrings[2])));
			session.save(exam);
			session.flush();
			session.getTransaction().commit();
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


