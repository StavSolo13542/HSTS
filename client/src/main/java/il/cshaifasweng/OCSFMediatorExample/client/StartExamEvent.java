package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
import il.cshaifasweng.OCSFMediatorExample.entities.ReadyExam;

public class StartExamEvent {
    private String message;
    private ReadyExam exam;
    public String getMessage() {
        return message;
    }

    public StartExamEvent(String message, ReadyExam exam) {
        this.message = message;
        this.exam = exam;
    }

    public ReadyExam getExam() {
        return exam;
    }
}
