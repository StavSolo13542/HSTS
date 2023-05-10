package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class InputErrorEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public InputErrorEvent(Message message) {
        this.message = message;
    }
}
