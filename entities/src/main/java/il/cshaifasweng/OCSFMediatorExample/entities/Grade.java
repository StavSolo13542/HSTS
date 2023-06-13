package org.example;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "grade_generator")
    @GenericGenerator(name = "grade_generator", strategy = "increment")
    private int id;
    private int the_grade;
    @ManyToOne
    @JoinColumn(name="exam_id", nullable=false)
    private Exam exam;
    @ManyToOne
    @JoinColumn(name="pupil_id", nullable=false)
    private Pupil pupil;

    public Grade(int grade, Exam exam, Pupil pupil) {
        this.the_grade = grade;
        this.exam = exam;
        exam.addGrade(this);
        this.pupil = pupil;
        pupil.addGrade(this);
    }

    public int getGrade() {
        return the_grade;
    }

    public void changeGrade(int the_grade) {
        this.the_grade = the_grade;
    }

    public int getId() {
        return id;
    }

    public Exam getExam() {
        return exam;
    }

    public Pupil getPupil() {
        return pupil;
    }

    public Grade(){

    }
}