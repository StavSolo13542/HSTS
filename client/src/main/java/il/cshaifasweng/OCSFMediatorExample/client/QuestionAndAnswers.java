package il.cshaifasweng.OCSFMediatorExample.client;

public class QuestionAndAnswers {

    private  String[] questions;
    private  String[] answers;

    QuestionAndAnswers(String[] ans,String[] ques)
    {
        answers=ans;
        questions=ques;
    }

    public String[] getQuestions()
    {
        return  questions;
    }
    public  String[] getAnswers()
    {
        return answers;
    }
    public void setQuestion(String[] q)
    {
        questions=q;
    }
    public  void  setAnswers(String[] q)
    {
        answers=q;
    }
}
