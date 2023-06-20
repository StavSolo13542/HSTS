package il.cshaifasweng.OCSFMediatorExample.entities;
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
    @JoinColumn(name="readyExam_id", nullable=false)
    private ReadyExam readyExam;
    @ManyToOne
    @JoinColumn(name="pupil_id", nullable=false)
    private Pupil pupil;
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "grade_correctQuestions",
            joinColumns = { @JoinColumn(name = "grade_id") },
            inverseJoinColumns = { @JoinColumn(name = "question_id") }
    )
    private List<Question> correctly_answered_questions;
    private String note_from_teacher;
    public Grade(/*int grade, */ReadyExam readyExam, Pupil pupil, List<Question> correctly_answered_questions, String note_from_teacher) {
//        this.the_grade = grade;
        this.the_grade = 0;
        List<Exam_Question_points> scores = readyExam.getExam().getPoints();
        for (Question q : correctly_answered_questions)
        {
            for (Exam_Question_points p : scores)
            {
                if (p.getExam().getId() == readyExam.getExam().getId() && p.getQuestion().getId() == q.getId())
                {
                    this.the_grade += p.getPoints();
                }
            }
        }

        this.readyExam = readyExam;
        readyExam.addGrade(this);
        this.pupil = pupil;
        pupil.addGrade(this);
        this.correctly_answered_questions = correctly_answered_questions;
        for (Question q : correctly_answered_questions)             // update question side too
        {
            q.addGrade(this);
        }
        this.note_from_teacher = note_from_teacher;
    }

    public String getNote_from_teacher() {
        return note_from_teacher;
    }

    public int getThe_grade() {
        return the_grade;
    }

    public void setThe_grade(int the_grade) {
        this.the_grade = the_grade;
    }

    public void setNote_from_teacher(String note_from_teacher) {
        this.note_from_teacher = note_from_teacher;
    }

    public List<Question> getCorrectly_answered_questions() {
        return correctly_answered_questions;
    }

    public void setCorrectly_answered_questions(List<Question> correctly_answered_questions) {
        this.correctly_answered_questions = correctly_answered_questions;
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

    public ReadyExam getReadyExam() {
        return readyExam;
    }

    public Pupil getPupil() {
        return pupil;
    }

    public Grade(){

    }
    public Grade(String[] desc, int index){
        correctly_answered_questions = new ArrayList<>();
        this.id = Integer.parseInt(desc[index + 2]);
        this.the_grade = Integer.parseInt(desc[index + 4]);
        index += 6;
        this.readyExam = new ReadyExam(desc,index);
        while (!desc[index].equals("correctly_answered_questions")) index++;
        while(!desc[index].equals("note_from_teacher")){
            if (desc[index].equals("question_starts_here")){
                correctly_answered_questions.add(new Question(desc,index));
            }
            index++;
        }
        String s = "";
        index++;
        while (!desc[index].equals("end_grade")) s += desc[index++] + " ";
        this.note_from_teacher = s;
    }
    @Override
    public String toString() {
        String s =
                "grade_starts_here id " + id +
                " the_grade " + the_grade +
                " readyExam " + readyExam.toString() +
                " correctly_answered_questions ";

        for (Question question:correctly_answered_questions)
        {
            s += question.toString();
        }
        s += " note_from_teacher " + note_from_teacher + " end_grade ";
        return s;
    }
}