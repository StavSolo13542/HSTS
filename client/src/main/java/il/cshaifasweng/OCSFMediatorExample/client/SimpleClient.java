package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Question;
import il.cshaifasweng.OCSFMediatorExample.entities.Subject;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;
	private String name = "";
	private Exam currExam;
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
			Question q1 = new Question("Which is the largest animal?", "Answer1: Lion Answer2: Hippo Answer3: Bird Answer4: Snake ", 2, new Subject("Biology"));
			Question q2 = new Question("Which is the smallest animal?", "Answer1: Ant Answer2: Bee Answer3: Humming Bird Answer4: Blue Whale ", 4, new Subject("Biology"));
			ArrayList<Question> questions = new ArrayList<>();
			questions.add(q1);
			questions.add(q2);
			List<Integer> scores = new ArrayList<Integer>();
			scores.add(30);
			scores.add(70);
			currExam = new Exam(0,questions,"123456", 1, true,"If nobody will submit the test in the first hour then ask for time addition",
					"Choose the correct answer in every question","Sara",scores);
			if (!currExam.getComputed()) {

			}
			EventBus.getDefault().post(new SwitchScreenEvent("exam"));

		}
		else if (msg_string.startsWith("StartExam")){
			EventBus.getDefault().post(new StartExamEvent("",currExam, name));

		}
		else if (msg_string.equals("UpdateSuc")){
			EventBus.getDefault().post(new UpdateSucEvent(new Message(msg_string)));
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
