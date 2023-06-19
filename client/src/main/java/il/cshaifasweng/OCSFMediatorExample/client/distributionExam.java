package il.cshaifasweng.OCSFMediatorExample.client;

public class distributionExam {
    private String test_id;
    private String test_name;
    private String avg;
    private String median;


    public distributionExam(String mTest_id,String mTest_name,String mAvg,String mMedian)
    {
        test_id=mTest_id;
        test_name=mTest_name;
        avg=mAvg;
        median=mMedian;
    }

    public String getTest_id()
    {
        return test_id;
    }
    public String getTest_name()
    {
        return test_name;
    }
    public String getAvg()
    {
        return avg;
    }
    public String getMedian()
    {
        return median;
    }

    public void  setTest_id(String mTest_id)
    {
        test_id=mTest_id;
    }
    public void  setmTest_name(String mTest_name)
    {
        test_name=mTest_name;
    }
    public void  setAvg(String mAvg)
    {
        avg=mAvg;
    }
    public void  setMedian(String mMedian)
    {
        median=mMedian;
    }

}