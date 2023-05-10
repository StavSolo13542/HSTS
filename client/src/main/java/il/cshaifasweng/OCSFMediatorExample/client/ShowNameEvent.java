package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class ShowNameEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public ShowNameEvent(Message message) {
        this.message = message;
    }
}
