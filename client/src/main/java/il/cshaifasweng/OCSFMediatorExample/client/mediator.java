package il.cshaifasweng.OCSFMediatorExample.client;

public class mediator {
    public static void sendMessageToC(String message) {
        TeacherAddQuestion.receiveMessage(message);
    }
}