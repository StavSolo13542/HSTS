package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;
	public static String name = "";
	private static ReadyExam currExam;
	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	//Use EventBus to activate the relevant method based on the message
	protected void handleMessageFromServer(Object msg) {
		if(!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
		String msg_string = msg.toString();
		msg_string = msg_string.replace("[", "").replace("]", "").replace(",","");
		if (msg_string.startsWith("Student names:")){
			EventBus.getDefault().post(new ShowNameEvent(new Message(msg_string)));
		}
		else if (msg_string.startsWith("Grades:")){
			EventBus.getDefault().post(new ShowGradeEvent(new Message(msg_string)));
		}
		else if (msg_string.startsWith("InputError")){
			EventBus.getDefault().post(new InputErrorEvent(msg_string));
		}
		else if (msg_string.startsWith("LogIn")){
			String[] parts = msg_string.split(" ");
			name = parts[2];
			System.out.println("in login from server message"); // for debugging

			EventBus.getDefault().post(new SwitchScreenEvent(parts[1]+"_primary"));
		}
		else if (msg_string.startsWith("EnterExam")){
			Subject astro = new Subject("Astrophysics");
			Course speeds = new Course("Velocity in space", astro);
			Answer ans1 = new Answer("1 km/h", false);          // new answers
			Answer ans2 = new Answer("2 km/h", false);
			Answer ans3 = new Answer("3 km/h", false);
			Answer ans4 = new Answer("4 km/h", true);
			Answer ans5 = new Answer("laptop", false);
			Answer ans6 = new Answer("table", false);
			Answer ans7 = new Answer("Cruise ship", true);
			Answer ans8 = new Answer("tent", false);
			Question q1 = new Question("which is bigger?", ans1, ans2, ans3, ans4, astro, speeds);// new questions
			Question q2 = new Question("which number is bigger?", ans5, ans6, ans7, ans8, astro, speeds);
			Teacher t1 = new Teacher("Malki", "Malki_password", true);
			// new exam
			Exam ex1 = new Exam("first exam", speeds, 1, "hello students!", "hello teacher!", t1);
			ex1.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
            /*
            This part is important and tricky. normally you would think that adding a question to the exam would look like:
            ex1.addQuestion(q1, 70);
            (BTW: now adding a question also requires to give that question points- 70 points in this case)
            BUT YOU SHOULD NOT ADD QUESTIONS LIKE THIS!!!!!!!!!!
            the points of this question on this exam are loaded into a different entity called Exam_question_points.
            this entity is created automatically each time you add a question, but in order for it to be saved in the tables
            I made the addQuestion function return this entity- and then it has to be saved by the session.
            SO THE OVERALL COMMAND TO ADD A QUESTION TO AN EXAM IS:
            session.save(ex1.addQuestion(q1, 70));
             */
			ex1.addQuestion(q1, 70);
			ex1.addQuestion(q2, 30);

			currExam = new ReadyExam(ex1, "10a4", false, "14/6/2023 13:30");  // new "Out of the drawer" exam
			if (!currExam.getOnline()) {
				EventBus.getDefault().post(new SwitchScreenEvent("word_exam"));
			}
			else EventBus.getDefault().post(new SwitchScreenEvent("exam"));

		}
		else if (msg_string.startsWith("StartExam")){
			EventBus.getDefault().post(new StartExamEvent(name,currExam));

		}
		else if (msg_string.equals("UpdateSuc")){
			EventBus.getDefault().post(new UpdateSucEvent(new Message(msg_string)));
		}
		else if (msg_string.startsWith("Here are all subjects")){
			msg_string = msg_string.replaceFirst("Here are all subjects", "");
			EventBus.getDefault().post(new test(new Message(msg_string)));
		}
		else if (msg_string.startsWith("Here are all courses")){
			msg_string = msg_string.replaceFirst("Here are all courses", "");
			EventBus.getDefault().post(new test(new Message(msg_string)));
		}

	}
	//Send received message to the server
	static public void sendMessage(String msg){
		try {
			Message message = new Message(msg);
			System.out.println("SimpleClient.getClient host, port: " + SimpleClient.getClient().getHost() + ", " +SimpleClient.getClient().getPort());

			SimpleClient.getClient().sendToServer(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	static public void postMessage(String msg) {
		if (msg.startsWith("StartExam")){
			EventBus.getDefault().post(new StartExamEvent(name,currExam));

		}
	}
	public static SimpleClient getClient() {
		if (client == null) {

			// from Michael's pc, works for pcs in different places (different LANs)
//			 client = new SimpleClient("0.tcp.eu.ngrok.io", 13010);

			 // works only for pcs in the same LAN
			 client = new SimpleClient("localhost", 3100);
		}
		return client;
	}
	@Subscribe
	//Display input error
	public void DisplayInputError(InputErrorEvent event) {
		String error_str = event.getMessage();
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.ERROR,
					String.format(error_str.substring(error_str.indexOf(" ") + 1))
			);
			alert.setTitle("Error!");
			alert.setHeaderText("Error:");
			alert.show();
		});
	}
	@Subscribe
	//Display success message
	public void DisplaySuccess(SuccessEvent event) {
		String success_str = event.getMessage();
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION,
					String.format(success_str)
			);
			alert.setTitle("Success!");
			alert.setHeaderText("Success:");
			alert.show();
		});
	}
}
