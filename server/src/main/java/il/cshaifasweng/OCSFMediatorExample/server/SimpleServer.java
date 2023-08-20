package il.cshaifasweng.OCSFMediatorExample.server;

//import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import org.greenrobot.eventbus.EventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import static il.cshaifasweng.OCSFMediatorExample.server.App.getSessionFactory;

public class SimpleServer extends AbstractServer {
	public ConnectionToClient principalClient;
	public static HashMap<Integer, Vector<ConnectionToClient>> test_connections = new HashMap<>();
	public static HashMap<String, ConnectionToClient> connected_users = new HashMap<>();
	public SimpleServer(int port) {
		super(port);

	}
	public static boolean isReadyExam(String code) throws Exception {
		try (Session session = getSessionFactory().openSession()) {
			session.getTransaction().begin();

			Query query = session.createNativeQuery("select count(*) from readyexams where four_digit_code ='" + code +"';");
			int count = ((Number) query.getSingleResult()).intValue();
			session.getTransaction().commit();
			if (count == 1)
			{
				// Commit the transaction
				return true;
			}
			else
			{
				// Commit the transaction
				return false;
			}
		}
	}

	//success message format: LogIn <<role>> <<username>> (role is one of the following options: "student", "teacher, "principal")
	//error message format: InputError <<error description>>

	private static String getTableName(String role)
	{
		if(role.equals("principal")) {
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
	private static String getIdName(String role)
	{
		if(role.equals("principal")) {
			return "principal_id";
		}
		else if(role.equals("teacher")) {
			return "teacher_id";
		}
		else if(role.equals("student"))
		{
			return "real_id";
		}
		else
		{
			return "";
		}
	}
	private String logIn(String role, String username, String password, ConnectionToClient client)
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
				loginResultMessage = "LogIn@"+  role + "@" + username;
				try (Session session = getSessionFactory().openSession()) {
					session.getTransaction().begin();

					Query query3 = session.createNativeQuery("UPDATE " + getTableName(role) + " SET isLoggedIn = true" + " where name = '" + username + "' and password = '" + password + "';");
					int rowCount = query3.executeUpdate();
					//getting the real_id of the user
					String query = "";
					if (role.equals("student")){
						query = "SELECT p.real_id FROM Pupil p WHERE p.password = :password AND p.name = :name";
					}
					else if (role.equals("teacher")){
						query = "SELECT p.teacher_id FROM Teacher p WHERE p.password = :password AND p.name = :name";
					}
					else if (role.equals("principal")){
						query = "SELECT p.principal_id FROM Principal p WHERE p.password = :password AND p.name = :name";
					}
					Query sqlQuery = session.createQuery(query);
					sqlQuery.setParameter("password", password);
					sqlQuery.setParameter("name", username);
					Object real_id = ((org.hibernate.query.Query<?>) sqlQuery).uniqueResult();
					loginResultMessage += "@" + real_id;
					connected_users.put(String.valueOf(real_id), client);
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

	// Success message: EnterExam <<exam_description>>
	// Error message: InputError <<error_description>>
	private String connectToExam(String code, ConnectionToClient client, String pupil_id) throws Exception {
		String message;
		try {
			if (isReadyExam(code)){
				message = "EnterExam ";

				Session session = getSessionFactory().openSession();
				Transaction transaction = null;
				ReadyExam readyExam = null;

				try {
					transaction = session.beginTransaction();

					// Retrieve a row by id
					//getting the id
					String queryForId = "SELECT id FROM readyexams WHERE four_digit_code = '" + code + "';";
					int exam_id = (int) session.createNativeQuery(queryForId).getSingleResult();
					readyExam = session.get(ReadyExam.class, exam_id);
					SimpleDateFormat sdf
							= new SimpleDateFormat(
							"dd/MM/yyyy HH:mm:ss");
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
					LocalDateTime now = LocalDateTime.now();
					Date the_time_now = sdf.parse(dtf.format(now));
					Date start_date = sdf.parse(readyExam.getTime_start() + ":00");
					Long difference = the_time_now.getTime() - start_date.getTime();
					System.out.println("the difference is: " + TimeUnit.MILLISECONDS.toMinutes(difference) + ".");
					if (TimeUnit.MILLISECONDS.toMinutes(difference) >= readyExam.getActual_solving_time() || TimeUnit.MILLISECONDS.toMinutes(difference) < 0) {
						message = "InputError The exam isn't available right now";
					}
					else{
						Query query;
						if (readyExam.getOnline()) query = session.createNativeQuery("select count(*) from grades where readyExam_id ='" + exam_id +"' AND pupil_id ='" + pupil_id +"';");
						else query = session.createNativeQuery("select count(*) from word_submission where readyExam_id ='" + exam_id +"' AND pupil_id ='" + pupil_id +"';");
						int count = ((Number) query.getSingleResult()).intValue();
						if (count > 0) message = "InputError You already did the exam";
						else{
							message += readyExam.toString();
							if(test_connections.get(exam_id) == null) test_connections.put(exam_id, new Vector<>());
							test_connections.get(exam_id).add(client);
						}
						transaction.commit();
					}
				} catch (Exception e) {
					if (transaction != null) {
						transaction.rollback();
					}
					e.printStackTrace();
				} finally {
					session.close();
				}


			}
			else {
				message = "InputError The code you entered does not exist";
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return message;
	}

	private void LogOut(String id, String role){
		try (Session session = getSessionFactory().openSession()) {
			session.getTransaction().begin();
			Query query = session.createNativeQuery("UPDATE "+ getTableName(role) + " SET isLoggedIn = false where " + getIdName(role) + " = '" + id + "';");
			query.executeUpdate();
			// Commit the transaction
			session.getTransaction().commit();
			connected_users.remove(id);
		}
	}

	private String GetStudentGrades(String id)
	{
		String message = "StudentGrades ";
		Session session = getSessionFactory().openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			String primarey_key_query = "SELECT id FROM Pupils WHERE real_id = '" + id + "';";
			String primarey_key =  session.createNativeQuery(primarey_key_query).getSingleResult().toString();
			// Retrieve a row by id
			String queryString = "SELECT * FROM Grades WHERE pupil_id = " + primarey_key + ";";
			List<Grade> grades = session.createNativeQuery(queryString, Grade.class).list();
			for (Grade grade : grades)
			{
				message += grade.toString();
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return message;
	}
	private void CheckExam(String student_id, String exam_id, String answers){
		ReadyExam readyExam = null;
		Pupil pupil = null;
		List<Question> correctly_answered_questions = new ArrayList<Question>();
		String note_from_teacher = "";

		try {
			Session session = getSessionFactory().openSession();
			session.beginTransaction();

			readyExam = session.get(ReadyExam.class, Integer.parseInt(exam_id));

			String q_pupil = "FROM Pupil p where p.real_id = :realId";
			org.hibernate.Query<Pupil> pupilQuery = session.createQuery(q_pupil, Pupil.class);
			pupilQuery.setParameter("realId", student_id);
			pupil = pupilQuery.uniqueResult();

			// getting the correctly answered questions

			Exam exam = readyExam.getExam();
			List<Question> questionList = exam.getQuestions();
			for(int i =0; i<questionList.size(); i++) {
				Question question = questionList.get(i);
				List<Answer> answerList = question.getAnswers();
				if (Character.getNumericValue(answers.charAt(i)) >= 1
				&& Character.getNumericValue(answers.charAt(i)) <= 4
				&& answerList.get(Character.getNumericValue(answers.charAt(i))-1).getIs_correct()) {
					correctly_answered_questions.add(question);
				}
			}

			Grade grade = new Grade(readyExam, pupil, correctly_answered_questions, note_from_teacher);

			session.save(grade);

			session.getTransaction().commit();
			if(connected_users.containsKey(connected_users.get(grade.getPupil()))) sendMessage("RefreshGrades", connected_users.get(grade.getPupil()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void SaveSubmission(String student_id, String exam_id, String content){
		ReadyExam readyExam = null;
		Pupil pupil = null;
		try {
			Session session = getSessionFactory().openSession();
			session.beginTransaction();

			readyExam = session.get(ReadyExam.class, Integer.parseInt(exam_id));

			String q_pupil = "FROM Pupil p where p.real_id = :realId";
			org.hibernate.Query<Pupil> pupilQuery = session.createQuery(q_pupil, Pupil.class);
			pupilQuery.setParameter("realId", student_id);
			pupil = pupilQuery.uniqueResult();
			WordSubmission submission = new WordSubmission(readyExam, pupil,content);

			session.save(submission);

			session.getTransaction().commit();
		}
			catch (Exception e) {
				e.printStackTrace();
			}
	}

	// Method to determine the user's role based on their username
	private String determineUserRole(String username, String password) {
		String role = null;

		try (Session session = getSessionFactory().openSession()) {
			session.getTransaction().begin();

			// Query to determine the role based on name and password
			String query = "SELECT " +
					"		CASE " +
					"        WHEN EXISTS (SELECT * FROM Pupils WHERE name = :username AND password = :password) THEN 'student' " +
					"        WHEN EXISTS (SELECT * FROM Teachers WHERE name = :username AND password = :password) THEN 'teacher' " +
					"        WHEN EXISTS (SELECT * FROM Principals WHERE name = :username AND password = :password) THEN 'principal' " +
					"        ELSE NULL " +
					"    END AS role";



			Query sqlQuery = session.createNativeQuery(query);
			//Query sqlQuery = session.createNativeQuery(query2);
			sqlQuery.setParameter("username", username);
			sqlQuery.setParameter("password", password);

			// Execute the query
			List<String> roles = sqlQuery.getResultList();

			if (!roles.isEmpty()) {
				role = roles.get(0);    // The first role in the list (only one should be present)
			}

			session.getTransaction().commit();
		}
		return role;
	}


	@Override
	//Treating the message from the client
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) throws Exception {
		String msgString = msg.toString();
		//validates the entered information and either lets the respective user to proceed to the rest of the system
		//(based on its role) or sends a relevant error message
		if (msgString.startsWith("LogIn")) {
			String[] parts = msgString.split("@");

			String username = parts[1];
			String password = parts[2];
//			String role = parts[3];

			// Search for the role of the user based on the username in the appropriate tables
			String role = determineUserRole(username, password);;

			if (role != null)
			{
				System.out.println("Checking login"); // for debugging
				String message = logIn(role, username, password,client);
				System.out.println("Finished checking login"); // for debugging

				sendMessage(message,client);
			} else {
				// User role couldn't be determined, send an error message
				sendMessage("InputError The identification details are incorrect", client);
			}
		}
		else if(msgString.startsWith("hello i am principal"))
		{
			this.principalClient=client;
		}
		else if (msgString.startsWith("EnterExam")) {
			String[] parts = msgString.split(" ");
			String code = parts[1];
			String pupil_id = parts[2];
			String message = connectToExam(code, client, pupil_id);
			sendMessage(message,client);
		}
		else if (msgString.startsWith("SubmitAnswersWord")) {

			String[] parts = msgString.split("@");
			String student_id = parts[1];
			String exam_id = parts[2];
			String content = parts[5];
			SaveSubmission(student_id, exam_id, content);
		}
		else if (msgString.startsWith("SubmitAnswers")) {

			String[] parts = msgString.split(" ");

			String student_id = parts[1];
			String exam_id = parts[2];
			String answers = parts[3];
			CheckExam(student_id, exam_id, answers);
		}
		else if (msgString.startsWith("LogOut")) {
			String[] parts = msgString.split(" ");
			String id = parts[1];
			String role = parts[2];
			LogOut(id,role);
			sendMessage("LogOut",client);
		}
		else if (msgString.startsWith("GetStudentGrades")) {
			String[] parts = msgString.split(" ");
			String id = parts[1];
			String grades = GetStudentGrades(id);
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
		else if (msgString.startsWith("get all subjects")) {
			System.out.println("in handleMessageFromClient -> if (get all subjects)");
			String data = connectToDatabase(msgString.replaceFirst("get all subjects", ""));
			sendMessage("Here are all subjects" + data.substring(3), client);
		}
		else if (msgString.startsWith("get all SUbjects")){
			System.out.println("in handleMessageFromClient -> if (get all subjects) v2");
			String data = connectToDatabase(msgString.replaceFirst("get all SUbjects", ""));
			sendMessage("Here are all SUbjects" + data.substring(3), client);
		}
		else if (msgString.startsWith("gET all SUbjects")){
			System.out.println("in handleMessageFromClient -> if (gET all SUbjects) v2");
			String data = connectToDatabase(msgString.replaceFirst("gET all SUbjects", ""));
			sendMessage("Here are all SUbJects" + data.substring(3), client);
		}
		else if (msgString.startsWith("gET aLl SUbjects")){
			System.out.println("in handleMessageFromClient -> if (gET aLl SUbjects) v2");
			String data = connectToDatabase(msgString.replaceFirst("gET aLl SUbjects", ""));
			sendMessage("HerE Are all SUbJects" + data.substring(3), client);
		}
		else if (msgString.startsWith("get all courses")) {
			String data = connectToDatabase_Courses(msgString.replaceFirst("get all courses", ""));
			sendMessage("Here are all courses" + data.substring(3), client);
		}
		else if (msgString.startsWith("geT all Courses")) {
			String data = connectToDatabase_Courses(msgString.replaceFirst("geT all Courses", ""));
			sendMessage("HerE arE all courses" + data.substring(3), client);
		}
		else if (msgString.startsWith("geT aLl Courses")) {
			System.out.println("after geT aLl Courses for ask extension");
			String data = connectToDatabase_Courses(msgString.replaceFirst("geT aLl Courses", ""));
			sendMessage("HerE ARE all courses" + data.substring(3), client);
		}
		else if (msgString.startsWith("Get All exAms"))
		{
			System.out.println("after Get All Exams For readyExam");
			String data = connectToDatabase_Exams(msgString.replaceFirst("Get All exAms", ""));
			sendMessage("Here are All exams1" + data.substring(3), client);
		}
		else if (msgString.startsWith("Gget All exAms"))
		{
			System.out.println("after Gget All Exams For readyExam");
			String data = connectToDatabase_Exams(msgString.replaceFirst("Gget All exAms", ""));
			sendMessage("Hhere are All exams1" + data.substring(3), client);
		}
		else if (msgString.startsWith("get all REaDy Exams"))
		{
			System.out.println("after get all REaDy Exams");
			String data = connectToDatabase_ReadyExams(msgString.replaceFirst("get all REaDy Exams", ""));
			sendMessage("HerE are All REaDy Exams" + data.substring(3), client);
		}
		else if (msgString.startsWith("get aLl REaDy Exams"))
		{
			System.out.println("after get aLl REaDy Exams for ask extension");
			String data = connectToDatabase_ReadyExams_another(msgString.replaceFirst("get aLl REaDy Exams", ""));
			System.out.println("the final string is" + data.substring(3));
			sendMessage("HerE ARe All REaDy Exams" + data.substring(3), client);
		}
		else if (msgString.startsWith("RequestMoreTime"))
		{
			System.out.println("after time extension request: "+ msgString);
			if(msgString.startsWith("RequestMoreTime"))
			{
				System.out.println("yey");
			}
			else
			{
				System.out.println("neyyy and the message is:"+msgString);
			}
			sendMessage(msgString,this.principalClient);
		}
		else if (msgString.startsWith("get all details relevant to ReadyEXAm"))
		{
			System.out.println("after get all details relevant to ReadyEXAm");
			String data = getReadyExamDetails(msgString.replaceFirst("get all details relevant to ReadyEXAm", ""));
			sendMessage("HerE are all details relevant to ReadyEXAm" + data, client);
		}
		else if (msgString.startsWith("save readyExam"))
		{
			System.out.println("Entered saving a readyExam");
			System.out.println("message is: " + msgString.substring(14));
			addReadyExam(msgString.substring(14));
		}
		else if (msgString.startsWith("get all QUestions"))
		{
			System.out.println("after get all QUestions");
			String data = connectToDatabase_Questions_according_to_subject(msgString.replaceFirst("get all QUestions", ""));
			sendMessage("Here are all QUestioNs" + data.substring(3), client);
		}
		else if (msgString.startsWith("gget all QUestions"))
		{
			System.out.println("after gget all QUestions");
			String data = connectToDatabase_Questions_according_to_subject(msgString.replaceFirst("gget all QUestions", ""));
			sendMessage("Hhere are all QUestioNs" + data.substring(3), client);
		}
		else if (msgString.startsWith("save Update Question"))
		{
			System.out.println("after save Update Question");
			updateQuestion(msgString.replaceFirst("save Update Question", ""));
		}
		else if (msgString.startsWith("SaVe UPdAted GRADes"))
		{
			System.out.println("after SaVe UPdAted GRADes");
			updateNewGrades(msgString.replaceFirst("SaVe UPdAted GRADes", ""));
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
		else if (msgString.startsWith("Gget All Questions For Exam"))
		{
			System.out.println("after Gget All Questions For Exam");
			String data = connectToDatabase_Questions(msgString.replaceFirst("Gget All Questions For Exam", ""));
			sendMessage("Hhere are all questions1" + data.substring(3), client);
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

		else if (msgString.startsWith("Indeed approved"))
		{
			System.out.println("indeed approved");
			changeExamTime(msgString);
		}
		else if (msgString.startsWith("new question added"))
		{
			for (ConnectionToClient i : connected_users.values()) {
				sendMessage("new question", i);
			}
		}
		else if (msgString.startsWith("new exam added"))
		{
			for (ConnectionToClient i : connected_users.values()) {
				sendMessage("new exam", i);
			}
		}
	}

	public static void changeExamTime(String msg)
	{

		try (Session session = getSessionFactory().openSession()) {
			session.getTransaction().begin();
			String[] parts = msg.split("_");

			// Extract the two numbers from the second and third parts of the array
			int examId = Integer.parseInt(parts[1]);
			System.out.println("exam id is "+examId);
			int minutes = Integer.parseInt(parts[2]);
			System.out.println("minutes is "+minutes);

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ReadyExam> new_query = builder.createQuery(ReadyExam.class);
			new_query.from(ReadyExam.class);
			List<ReadyExam> new_data = session.createQuery(new_query).getResultList();
			for (ReadyExam r : new_data)
			{
				if (r.getId() == examId)
				{
					r.setActual_solving_time(r.getActual_solving_time() + minutes);
					session.flush();
				}
			}
			session.getTransaction().commit();
			Vector<ConnectionToClient> students = test_connections.get(examId);
			for (int i = 0; i < students.size(); i++){
				sendMessage("TimeExtension " + minutes,students.get(i));
			}
		}
		catch (Exception ex){

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
				System.out.println("course id is:"+courseId);
				Query query1 = session.createNativeQuery("SELECT id FROM Exams where course_id='" + courseId + "'");
				System.out.println("here4");
				List<String> question1 = query1.getResultList();
				String listOfExamIds=question1.toString();
				System.out.println("the idof exams are:"+listOfExamIds);
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
						System.out.println("exams ids of" + names + "are" + numbersArr[j]);
					}
					for (int k = 0; k < numbersArr.length; k++) {
						Query query4 = session.createNativeQuery("SELECT the_grade FROM Grades where readyExam_id=" + Integer.parseInt(numbersArr[k]) + "");
						List<String> question4 = query4.getResultList();
						gr=question4.toString();
						gr=gr.replace("[","").replace("]","");
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

			System.out.println("int beggining the id is"+id);
			Query query1 = session.createNativeQuery("SELECT id  FROM Exams where exam_code_number="+Integer.parseInt(id)+"");
			//Query query4 = session.createNativeQuery("SELECT subject_name FROM Subjects where subject_id = '" + 0 +"'");
			List<String> gradesList1=query1.getResultList();
			String idOfqQuestion1=gradesList1.toString();
			idOfqQuestion1=idOfqQuestion1.replace("[", "").replace("]", "").replace(",","").replace(" ","");
			System.out.println("idOfqQuestion1 is"+idOfqQuestion1);



			Query query2 = session.createNativeQuery("SELECT question_id  FROM exam_question where exam_id="+Integer.parseInt(idOfqQuestion1)+"");
			//Query query4 = session.createNativeQuery("SELECT subject_name FROM Subjects where subject_id = '" + 0 +"'");
			List<String> gradesList2=query2.getResultList();
			String idOfqQuestion=gradesList2.toString();
			System.out.println("the info is:"+idOfqQuestion);
			idOfqQuestion=idOfqQuestion.replace("[", "").replace("]", "").replace(" ","");
			String[] numbersArray = idOfqQuestion.split(",");
			for (int i = 0; i < numbersArray.length; i++) {
				numbersArray[i] = numbersArray[i].trim();
				System.out.println("Array value is:"+numbersArray[i]);
			}
			System.out.println("the length is:"+numbersArray.length);
			message=message+String.valueOf(numbersArray.length);
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

	private void updateQuestion(String description_string)
	{
		try (Session session = getSessionFactory().openSession()) {
			session.getTransaction().begin();
			String original_question_name = description_string.split("```")[0];
			String rest = description_string.split("```")[1];

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Question> new_query = builder.createQuery(Question.class);
			new_query.from(Question.class);
			List<Question> new_data = session.createQuery(new_query).getResultList();
			for (Question q : new_data)
			{
				if (q.getText().equals(original_question_name))
				{
					String[] substrings = rest.split("---");
					q.setText(substrings[0]);
					q.getAnswers().get(0).setAnswer_text(substrings[1].split("///")[0]);
					q.getAnswers().get(0).setIs_correct(Boolean.parseBoolean(substrings[1].split("///")[1]));
					q.getAnswers().get(1).setAnswer_text(substrings[2].split("///")[0]);
					q.getAnswers().get(1).setIs_correct(Boolean.parseBoolean(substrings[2].split("///")[1]));
					q.getAnswers().get(2).setAnswer_text(substrings[3].split("///")[0]);
					q.getAnswers().get(2).setIs_correct(Boolean.parseBoolean(substrings[3].split("///")[1]));
					q.getAnswers().get(3).setAnswer_text(substrings[4].split("///")[0]);
					q.getAnswers().get(3).setIs_correct(Boolean.parseBoolean(substrings[4].split("///")[1]));
					session.flush();
				}
			}


			session.getTransaction().commit();
		}
	}

	private void updateNewGrades(String description_string)
	{
		try (Session session = getSessionFactory().openSession()) {
			session.getTransaction().begin();
			System.out.println("description string: " + description_string);
			String[] split_description_string = description_string.split("___");
			String exam_code = split_description_string[0];
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Grade> new_query = builder.createQuery(Grade.class);
			new_query.from(Grade.class);
			List<Grade> data = session.createQuery(new_query).getResultList();
			for (int i=1; i < split_description_string.length; i++)
			{
				for (Grade grade: data)
				{
					if (grade.getReadyExam().getFour_digit_code().equals(exam_code) && grade.getPupil().getReal_id().equals(split_description_string[i].split("```")[0]))
					{
						grade.setThe_grade(Integer.valueOf(split_description_string[i].split("```")[1]));
						grade.setNote_from_teacher(split_description_string[i].split("```")[2]);
						session.flush();
						if(connected_users.containsKey(grade.getPupil().getReal_id())){
							ConnectionToClient client = connected_users.get(grade.getPupil().getReal_id());
							sendMessage("RefreshGrades", client);
						}
						break;
					}
				}
			}

			session.getTransaction().commit();
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

	private String connectToDatabase_Exams(String teacher_name) {
		try (Session session = getSessionFactory().openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			////////////////////////////////////////////
			CriteriaQuery<Teacher> new_query = builder.createQuery(Teacher.class);
			new_query.from(Teacher.class);
			List<Teacher> new_data = session.createQuery(new_query).getResultList();
			for (Teacher t : new_data)
			{
				if (t.getName().equals(teacher_name))
				{
					List<Exam> exams = t.getExams();
					String strings = "";
					for (Exam e : exams)
					{
						strings += ("___" + e.getName());
					}
					return strings.equals("") ? "___None": strings;
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

	private String connectToDatabase_ReadyExams(String course_name) {
		try (Session session = getSessionFactory().openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			////////////////////////////////////////////
			CriteriaQuery<ReadyExam> query1 = builder.createQuery(ReadyExam.class);
			query1.from(ReadyExam.class);
			List<ReadyExam> data = session.createQuery(query1).getResultList();
			String final_string = "";
			for (ReadyExam r : data)
			{
				if (r.getExam().getCourse().getName().equals(course_name))
				{
					final_string += ("~~~" + r.getExam().getName() + "@" + r.getFour_digit_code());
				}
			}
			if (final_string.equals(""))
			{
				final_string = "~~~";
			}
			System.out.println("the final string is" + final_string);
			return final_string;
		}
	}

	private String connectToDatabase_ReadyExams_another(String course_name) {
		try (Session session = getSessionFactory().openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			////////////////////////////////////////////
			CriteriaQuery<ReadyExam> query1 = builder.createQuery(ReadyExam.class);
			query1.from(ReadyExam.class);
			List<ReadyExam> data = session.createQuery(query1).getResultList();
			String final_string = "";

			SimpleDateFormat sdf
					= new SimpleDateFormat(
					"dd/MM/yyyy HH:mm:ss");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

			for (ReadyExam r : data)
			{
				try {
					LocalDateTime now = LocalDateTime.now();
					Date the_time_now = sdf.parse(dtf.format(now));
					Date start_date = sdf.parse(r.getTime_start() + ":00");
					Long difference = the_time_now.getTime() - start_date.getTime();
					System.out.println("the difference is: " + TimeUnit.MILLISECONDS.toMinutes(difference) + ".");
					if (r.getExam().getCourse().getName().equals(course_name) && TimeUnit.MILLISECONDS.toMinutes(difference) < r.getActual_solving_time() && TimeUnit.MILLISECONDS.toMinutes(difference) >= 0) {
						final_string += ("~~~" + r.getExam().getName() + "@" + r.getFour_digit_code()) + "@" + r.getId();
					}
				}
				catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (final_string.equals(""))
			{
				final_string = "___";
			}
			return final_string;
		}
	}
	private String getReadyExamDetails(String readyExamCode) {
		try (Session session = getSessionFactory().openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			////////////////////////////////////////////
			CriteriaQuery<Pupil> query1 = builder.createQuery(Pupil.class);
			query1.from(Pupil.class);
			List<Pupil> data = session.createQuery(query1).getResultList();
			String final_string = "";
			for (Pupil p: data)
			{
				List<Grade> grades = p.getGrades();
				for (Grade g: grades)
				{
					if (g.getReadyExam().getFour_digit_code().equals(readyExamCode))
					{
						final_string += ("~~~" + p.getName() + " " + p.getReal_id() + "___" + g.getGrade() + "___" + g.getNote_from_teacher() + "___");
						List<Question> questions = g.getReadyExam().getExam().getQuestions();
						List<Question> correct_questions = g.getCorrectly_answered_questions();
						List<Question> incorrect_questions = new ArrayList<Question>();
						for (Question q: questions)
						{
							if (!correct_questions.contains(q))
							{
								incorrect_questions.add(q);
							}
						}
						String correct_questions_string = "";
						List<Exam_Question_points> points = g.getReadyExam().getExam().getPoints();
						for (Question q: correct_questions)
						{
							int curr_points = 0;
							for (Exam_Question_points po: points)
							{
								if (po.getExam().getExam_code_number().equals(g.getReadyExam().getExam().getExam_code_number()) && po.getQuestion().getQuestion_code_number().equals(q.getQuestion_code_number()))
								{
									curr_points = po.getPoints();
								}
							}
							correct_questions_string += ("```" + q.getText() + "---" + String.valueOf(curr_points));
						}
						String incorrect_questions_string = "";
						for (Question q: incorrect_questions)
						{
							int curr_points = 0;
							for (Exam_Question_points po: points)
							{
								if (po.getExam().getExam_code_number().equals(g.getReadyExam().getExam().getExam_code_number()) && po.getQuestion().getQuestion_code_number().equals(q.getQuestion_code_number()))
								{
									curr_points = po.getPoints();
								}
							}
							incorrect_questions_string += ("```" + q.getText() + "---" + String.valueOf(curr_points));
						}
						String questions_string = (correct_questions_string.equals("") ? " " : correct_questions_string.substring(3)) + "§§§" + (incorrect_questions_string.equals("") ? " " : incorrect_questions_string.substring(3));
						final_string += questions_string;
					}
				}
			}
			return final_string.substring(3);
		}
	}

	private String connectToDatabase_Questions_according_to_subject(String subject_name) {
		try (Session session = getSessionFactory().openSession()) {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			////////////////////////////////////////////
			CriteriaQuery<Subject> new_query = builder.createQuery(Subject.class);
			new_query.from(Subject.class);
			List<Subject> new_data = session.createQuery(new_query).getResultList();
			for (Subject sub : new_data) {
				if (sub.getName().equals(subject_name)) {
					List<Exam> exams = new ArrayList<Exam>();
					List<Course> all_good_courses = sub.getCourses();
					List<Question> all_good_questions = new ArrayList<Question>();
					all_good_questions.addAll(sub.getQuestions());
//					for (Course c : all_good_courses){
//						all_good_questions.addAll(c.getQuestions());
//					}
					String strings = "";
					for (Question question : all_good_questions) {

//						strings += ("___" + question.getText());
						List<Answer> answers = question.getAnswers();
						strings += ("___" + question.getText() + "---" + answers.get(0).getAnswer_text() + "///" + String.valueOf(answers.get(0).getIs_correct()) +
								"---" + answers.get(1).getAnswer_text() + "///" + String.valueOf(answers.get(1).getIs_correct()) +
								"---" + answers.get(2).getAnswer_text() + "///" + String.valueOf(answers.get(2).getIs_correct()) +
								"---" + answers.get(3).getAnswer_text() + "///" + String.valueOf(answers.get(3).getIs_correct()) +
								"---" + question.getSubject().getName());
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

	private void addReadyExam(String description_string)
	{
		try (Session session = getSessionFactory().openSession()) {
			session.getTransaction().begin();
			String[] substrings = description_string.split("@@@");
			String name = substrings[0];

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Exam> query1 = builder.createQuery(Exam.class);
			query1.from(Exam.class);
			List<Exam> data = session.createQuery(query1).getResultList();
			Exam exam = null;
			for (Exam e : data)
			{
				if (e.getName().equals(name))
				{
					exam = e;
					break;
				}
			}
			String code = substrings[1];
			String mode = substrings[2];
			Boolean real_mode = true;
			if (mode.equals("offline"))
			{
				real_mode = false;
			}
			String time = substrings[3];
			session.save(new ReadyExam(exam, code, real_mode, time));
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
			String[] substrings = description_string.split("```");
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
	protected static void sendMessage(String msg, ConnectionToClient client){
		Message message = new Message(msg);
		try {
			client.sendToClient(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}


