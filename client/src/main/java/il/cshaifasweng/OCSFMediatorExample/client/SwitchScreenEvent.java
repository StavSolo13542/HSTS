package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class SwitchScreenEvent {
    private String message;

    public String getMessage() {
        return message;
    }

    public SwitchScreenEvent(String message) {
        this.message = message;
    }
}
