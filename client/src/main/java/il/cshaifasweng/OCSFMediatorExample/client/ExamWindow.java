package il.cshaifasweng.OCSFMediatorExample.client;

public class ExamWindow {
    private String numberOfQuestoins;

    private String[] questions;

    private String[] answers;


    public ExamWindow(String numberOfQuestoins,String[] questions,String[]answers)
    {
        this.numberOfQuestoins=numberOfQuestoins;
        this.questions=questions;
        this.answers=answers;
    }

    public void setNummberOfQuestions(String numberOfQuestoins)
    {
        this.numberOfQuestoins=numberOfQuestoins;
    }
    public void setQuestions(String []questions)
    {
        this.questions=questions;
    }
    public void setAnswers(String [] answers)
    {
        this.answers=answers;
    }
    public String getNummberOfQuestions()
    {
        return numberOfQuestoins;
    }
    public String[] getQuestions()
    {
        return questions;
    }
    public String[] getAnswers()
    {
        return answers;
    }
}