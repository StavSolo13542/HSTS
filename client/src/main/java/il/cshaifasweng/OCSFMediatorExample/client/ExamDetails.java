package il.cshaifasweng.OCSFMediatorExample.client;

public class ExamDetails {
    private String id;
    private String code;
    private  String exam_name;

    public ExamDetails(String _code,String _examName)
    {
        code=_code;
        exam_name=_examName;
    }
    public ExamDetails(String _id,String _code,String _examName)
    {
        id=_id;
        code=_code;
        exam_name=_examName;
    }
    public ExamDetails()
    {
    }

    public void setId(String id)
    {
        this.id=id;
    }
    public String getId()
    {
        return  id;
    }
    public void setCode(String code)
    {
        this.code=code;
    }
    public String getCode()
    {
        return  code;
    }

    public void setExam_name(String exam_name)
    {
        this.exam_name=exam_name;
    }
    public String getExam_name()
    {
        return  exam_name;
    }
}