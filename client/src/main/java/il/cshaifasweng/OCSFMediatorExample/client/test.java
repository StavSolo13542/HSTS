package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Message;

public class test {
    private Message msg;
    private static mediator mediator;

    public static void setMediator(mediator mediator) {
        test.mediator = mediator;
    }

    public static void sendMessage(String msg) {
        if (mediator != null) {
            System.out.println("in TEST! " + msg);
            if (msg.startsWith("ToAddQuestion")) {
                msg = msg.replaceFirst("ToAddQuestion", "");
                mediator.sendMessageToTeacherAddQuestion(msg);
            }
            else if (msg.startsWith("ToBuildExam"))
            {
                msg = msg.replaceFirst("ToBuildExam", "");
                mediator.sendMessageToTeacherBuildExam(msg);
            }
            else if (msg.startsWith("ToPullExam"))
            {
                msg = msg.replaceFirst("ToPullExam", "");
                mediator.sendMessageToTeacherPullExam(msg);
            }
            else if (msg.startsWith("ToUpdateQuestion"))
            {
                msg = msg.replaceFirst("ToUpdateQuestion", "");
                mediator.sendMessageToTeacherUpdateQuestion(msg);
            }
            else if (msg.startsWith("ToCheckExam"))
            {
                msg = msg.replaceFirst("ToCheckExam", "");
                mediator.sendMessageToTeacherCheckExam(msg);
            }
            else if (msg.startsWith("ToTeacherExtendTime"))
            {
                msg = msg.replaceFirst("ToTeacherExtendTime", "");
                mediator.sendMessageToTeacherExtendTime(msg);
            }
            else{
                mediator.sendMessageToC(msg);
            }
        }
    }

    public static void sendMessageA(String msg,String []ques,String[]ans) {
        if (mediator != null) {
            mediator.sendMessageToA(msg,ques,ans);
        }
    }
    public void doSomethingWithButton() {

    }

    public test(Message msg)
    {

        this.msg=msg;
        mediator mediator = new mediator();
        this.setMediator(mediator);

        this.sendMessage(msg.toString()); // Send the message "hi" to ClassC
    }
    public test(String number,String []questions,String []answers)
    {

        this.msg=msg;
        mediator mediator = new mediator();
        this.setMediator(mediator);

        this.sendMessageA(number,questions,answers); // Send the message "hi" to ClassC
    }
}