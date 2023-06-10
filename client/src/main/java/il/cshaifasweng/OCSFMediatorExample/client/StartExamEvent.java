package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Exam;

public class StartExamEvent {
    private String message;
    private Exam exam;
    private String name;
    public String getMessage() {
        return message;
    }

    public StartExamEvent(String message, Exam exam, String name) {
        this.message = message;
        this.exam = exam;
        this.name = name;
    }

    public Exam getExam() {
        return exam;
    }

    public String getName() {
        return name;
    }
}
