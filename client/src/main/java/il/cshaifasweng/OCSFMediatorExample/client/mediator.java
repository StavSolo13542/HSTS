package il.cshaifasweng.OCSFMediatorExample.client;

public class mediator {
    public static void sendMessageToC(String message) {
        PrinciplePrimaryController.receiveMessage(message);
    }

    public static void sendMessageToA(String number,String[] questios,String[]answers) {
        HiController.recieveMessage(number,questios,answers);
    }
}