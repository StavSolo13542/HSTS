package il.cshaifasweng.OCSFMediatorExample.client;

public class mediator {
    public static void sendMessageToTeacherAddQuestion(String message) {
        TeacherAddQuestion.receiveMessage(message);
    }
    public static void sendMessageToTeacherBuildExam(String message) {
        TeacherBuildExam.receiveMessage(message);
    }

    public static void sendMessageToTeacherPullExam(String message)
    {
        TeacherPullExamFromDrawer.receiveMessage(message);
    }

    public static void sendMessageToTeacherUpdateQuestion(String message)
    {
        TeacherChangeQuestion.receiveMessage(message);
    }

    public static void sendMessageToTeacherCheckExam(String message)
    {
        TeacherCheckExam.receiveMessage(message);
    }
    public static void sendMessageToC(String message) {
        PrinciplePrimaryController.receiveMessage(message);
    }

    public static void sendMessageToA(String number,String[] questios,String[]answers) {
        HiController.recieveMessage(number,questios,answers);
    }
}