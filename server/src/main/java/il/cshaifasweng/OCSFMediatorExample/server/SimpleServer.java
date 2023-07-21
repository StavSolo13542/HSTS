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
		else if (msgString.startsWith("get all courses")) {
			String data = connectToDatabase_Courses(msgString.replaceFirst("get all courses", ""));
			sendMessage("Here are all courses" + data.substring(3), client);
		}
		else if (msgString.startsWith("geT all Courses")) {
			String data = connectToDatabase_Courses(msgString.replaceFirst("geT all Courses", ""));
			sendMessage("HerE arE all courses" + data.substring(3), client);
		}
		else if (msgString.startsWith("Get All exAms"))
		{
			System.out.println("after Get All Exams For readyExam");
			String data = connectToDatabase_Exams(msgString.replaceFirst("Get All exAms", ""));
			sendMessage("Here are All exams1" + data.substring(3), client);
		}
		else if (msgString.startsWith("get all REaDy Exams"))
		{
			System.out.println("after get all REaDy Exams");
			String data = connectToDatabase_ReadyExams(msgString.replaceFirst("get all REaDy Exams", ""));
			sendMessage("HerE are All REaDy Exams" + data.substring(3), client);
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
			System.out.println("the final string is" + final_string);
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
					for (Course c : all_good_courses){
						all_good_questions.addAll(c.getQuestions());
					}
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


