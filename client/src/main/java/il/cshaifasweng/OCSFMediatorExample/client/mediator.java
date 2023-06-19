package il.cshaifasweng.OCSFMediatorExample.client;

public class mediator {
    public static void sendMessageToTeacherAddQuestion(String message) {
        TeacherAddQuestion.receiveMessage(message);
    }
    public static void sendMessageToTeacherBuildExam(String message) {
        TeacherBuildExam.receiveMessage(message);
    }
}