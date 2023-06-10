package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.List;

public class Exam {
    private int id;
    private String code;
    private int time;
    private String teacher_desc;
    private String student_desc;
    private String teacher_name;
    private Boolean isComputed;
    private List<Question> questions;
    private List<Integer> scores;
    public Exam(int id, List<Question> questions, String code, int time, Boolean isComputed, String teacher_desc, String student_desc, String teacher_name, List<Integer> scores) {
        this.id = id;
        this.questions = questions;
        this.code = code;
        this.time = time;
        this.isComputed = isComputed;
        this.teacher_desc = teacher_desc;
        this.student_desc = student_desc;
        this.teacher_name = teacher_name;
        this.scores = scores;
    }
    @Override
    public String toString(){
        String str = "";
        str += "id: " + id;
        str += " code: " + code;
        str += " teacher_desc: " + teacher_desc;
        str += " student_desc: " + student_desc;
        str += " teacher_name" + teacher_name;
        str += " isComputed:" + String.valueOf(isComputed);
        str += " questions: ";
        for (int i = 0; i < questions.size(); i++){
            str += "Question" + String.valueOf(i + 1) + " " + questions.get(i).toString();
        }
        str += " scores: ";
        for (int i = 0; i < questions.size(); i++){
            str += " Score" + String.valueOf(i + 1) + " " + String.valueOf(scores.get(i));
        }
        return str;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getStudentDesc() {
        return student_desc;
    }
    public List<Question> getQuestions() {
        return questions;
    }
    public Boolean getComputed() {
        return isComputed;
    }

    public void setComputed(Boolean computed) {
        isComputed = computed;
    }

    public int getTime() {
        return time;
    }
    public int getId() {
        return id;
    }
}
