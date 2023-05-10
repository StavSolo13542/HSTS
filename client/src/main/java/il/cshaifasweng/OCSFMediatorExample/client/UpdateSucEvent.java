package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class UpdateSucEvent {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public UpdateSucEvent(Message message) {
        this.message = message;
    }
}
