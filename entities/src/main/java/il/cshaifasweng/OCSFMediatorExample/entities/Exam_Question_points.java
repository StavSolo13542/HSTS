package il.cshaifasweng.OCSFMediatorExample.entities;
/*
 * this class is intended to specify how many point go to each question in a specific exam
 */
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Exam_question_points")
public class Exam_Question_points {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "Exam_question_points_generator")
    @GenericGenerator(name = "Exam_question_points_generator", strategy = "increment")
    private int id;
    @ManyToOne
    @JoinColumn(name="exam_id", nullable=false)
    private Exam exam;
    @ManyToOne
    @JoinColumn(name="question_id", nullable=false)
    private Question question;

    private int points;

    public Exam_Question_points(Exam exam, Question question, int points) {
        this.exam = exam;
        exam.addPoints(this);
        this.question = question;
        question.addPoints(this);
        this.points = points;
    }

    public Exam_Question_points() {

    }

    public int getId() {
        return id;
    }

    public Exam getExam() {
        return exam;
    }

    public Question getQuestion() {
        return question;
    }

    public int getPoints() {
        return points;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}