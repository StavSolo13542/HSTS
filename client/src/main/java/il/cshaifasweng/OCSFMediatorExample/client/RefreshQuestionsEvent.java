package il.cshaifasweng.OCSFMediatorExample.client;

public class RefreshQuestionsEvent {
    private String code;
    private String message;
    public String getCode()
    {
        return this.code;
    }

    public String getMessage()
    {
        return this.message;
    }

    public RefreshQuestionsEvent(String _code, String _message)
    {
        this.code = _code;
        this.message = _message;
    }
}
