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
	public static String role = "";
	public static String real_id = "";
	public static ReadyExam currExam;
	public static Grade currGrade;
	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	//Use EventBus to activate the relevant method based on the message
	protected void handleMessageFromServer(Object msg) {
		if(!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
		String msg_string = msg.toString();
		msg_string = msg_string.replace("[", "").replace("]", "").replace(",","");
		if (msg_string.startsWith("InputError")){
			EventBus.getDefault().post(new InputErrorEvent(msg_string));
		}
		else if (msg_string.startsWith("LogIn")){
			String[] parts = msg_string.split(" ");

			real_id = parts[3];

			name = parts[2];
			role = parts[1];
			System.out.println("in login from server message"); // for debugging

			EventBus.getDefault().post(new SwitchScreenEvent(role+"_primary"));
		}
		else if (msg_string.startsWith("EnterExam")){
			String[] parts = msg_string.split(" ");
			currExam = new ReadyExam(parts,1);  // new "Out of the drawer" exam
			if (!currExam.getOnline()) {
				EventBus.getDefault().post(new SwitchScreenEvent("word_exam"));
			}
			else EventBus.getDefault().post(new SwitchScreenEvent("exam"));

		}
		else if (msg_string.startsWith("LogOut")) {
			name = "";
			role = "";
			real_id = "";
			EventBus.getDefault().post(new SwitchScreenEvent("log_in"));
		}
		else if (msg_string.startsWith("StudentGrades")) {
			ArrayList<Grade> grades = new ArrayList<>();
			String[] parts = msg_string.split(" ");
			for(int i = 0; i < parts.length; i++){
				if (parts[i].equals("grade_starts_here")){
					Grade grade = new Grade(parts,i);
					grades.add(grade);
				}
			}
			EventBus.getDefault().post(new StudentGradesEvent(grades));
		}
		else if(msg_string.startsWith("number of question"))
		{
			EventBus.getDefault().post(new test(new Message(msg_string)));
		}
		else if(msg_string.startsWith("number of Exams"))
		{
			EventBus.getDefault().post(new test(new Message(msg_string)));
		}
		else if(msg_string.startsWith("question in exam"))
		{
			EventBus.getDefault().post(new test(new Message(msg_string)));
		}
		else if(msg_string.startsWith("Student names are"))
		{
			EventBus.getDefault().post(new test(new Message(msg_string)));
		}
		else if(msg_string.startsWith("Courses names are"))
		{
			EventBus.getDefault().post(new test(new Message(msg_string)));
		}

		else if(msg_string.startsWith("Teachers names are"))
		{
			EventBus.getDefault().post(new test(new Message(msg_string)));
		}
		else if(msg_string.startsWith("All the distrubutions"))
		{
			EventBus.getDefault().post(new test(new Message(msg_string)));
		}
		else if(msg_string.startsWith("all the grades are:"))
		{
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
			EventBus.getDefault().post(new StartExamEvent(real_id,currExam));

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
	public static void Validate(String msg){
		if (msg.startsWith("StartExam")){
			String[] parts = msg.split(" ");
			String id = parts[1];
			if (id.equals(real_id)) postMessage("StartExam");
			else EventBus.getDefault().post(new InputErrorEvent("InputError identification details are incorrect"));


		}
	}
}