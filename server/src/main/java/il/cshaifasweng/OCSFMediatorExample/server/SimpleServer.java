package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class SimpleServer extends AbstractServer {

	public SimpleServer(int port) {
		super(port);
		
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String msgString = msg.toString();
		if (msgString.equals("GetNames")) {
			Message message = new Message("Student names: Alon Michail Tomer");
			try {
				client.sendToClient(message);
				System.out.format("Sent message to client %s\n", client.getInetAddress().getHostAddress());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
