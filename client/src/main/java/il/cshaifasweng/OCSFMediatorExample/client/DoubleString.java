package il.cshaifasweng.OCSFMediatorExample.client;

public class DoubleString {
    String question;
    String grade;

    public DoubleString(String question, String grade)
    {
        this.question = question;
        this.grade = grade;
        System.out.println("after DoubleString ctor");
    }

    public String getGrade() {
        return grade;
    }

    public String getQuestion() {
        return question;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
