package il.cshaifasweng.OCSFMediatorExample.client;

public class ThreeStringArray {
    private String []exam_names;
    private String []exam_code;
    private String []scores;

    public  ThreeStringArray(String []exam_names,String []exam_code,String []scores)
    {
        this.exam_names=exam_names;
        this.exam_code=exam_code;
        this.scores=scores;
    }
    public void setExam_names(String[] names)
    {
        exam_names=names;
    }
    public void setExam_code(String[] code)
    {
        exam_code=code;
    }
    public void setScores(String[] score)
    {
        scores=score;
    }
    public String[] getExam_names()
    {
        return exam_names;
    }
    public String[] getExam_code()
    {
        return exam_code;
    }
    public String[]getScores()
    {
        return scores;
    }


}