package il.cshaifasweng.OCSFMediatorExample.entities;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ReadyExams")
public class ReadyExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ready_exam_generator")
    @GenericGenerator(name = "ready_exam_generator", strategy = "increment")
    private int id;
    @ManyToOne
    @JoinColumn(name="exam_id", nullable=false)
    private Exam exam;

    private String four_digit_code;
    private Boolean online;

    private String time_start;

    private int actual_solving_time;

    @OneToMany(mappedBy="readyExam")
    private List<Grade> grades;

    public ReadyExam(Exam exam, String four_digit_code, Boolean online, String time_start) {
        this.exam = exam;
        exam.addReadyExam(this);
        this.four_digit_code = four_digit_code;
        this.online = online;
        this.time_start = time_start;
        this.actual_solving_time = exam.getDuration_in_minutes();       // need to update at the end of the exam
        this.grades = new ArrayList<Grade>();
    }
    public List<Grade> getGrades() {
        return grades;
    }

    public void addGrade(Grade g)
    {
        this.grades.add(g);
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public int getActual_solving_time() {
        return actual_solving_time;
    }

    public void setActual_solving_time(int actual_solving_time) {
        this.actual_solving_time = actual_solving_time;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public int getId() {
        return id;
    }

    public Exam getExam() {
        return exam;
    }

    public String getFour_digit_code() {
        return four_digit_code;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public void setFour_digit_code(String four_digit_code) {
        this.four_digit_code = four_digit_code;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public ReadyExam()
    {

    }

    @Override
    public String toString() {
        String s = "";
        s += "id: "+id+" online: "+online+ "exam code: " + exam.toString()+"\n";
        return s;
    }
}
