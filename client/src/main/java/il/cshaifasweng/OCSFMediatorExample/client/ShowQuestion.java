package il.cshaifasweng.OCSFMediatorExample.client;

import java.util.List;

public class ShowQuestion {
    private String question_text;
    private String answers;

    public ShowQuestion(String question,String answer)
    {
        this.question_text=question;
        this.answers=answer;
    }
    public ShowQuestion()
    {
    }

    public void setQuestion_text(String question)
    {
        this.question_text=question;
    }
    public String getQuestion_text()
    {
        return  question_text;
    }
    public void setAnswers(String answer)
    {
        this.answers=answer;
    }
    public String getAnswers()
    {
        return  answers;
    }

}