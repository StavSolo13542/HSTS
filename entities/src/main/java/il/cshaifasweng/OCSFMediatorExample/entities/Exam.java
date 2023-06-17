package il.cshaifasweng.OCSFMediatorExample.entities;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "exam_generator")
    @GenericGenerator(name = "exam_generator", strategy = "increment")
    private int id;
    @Column(name = "exam_name")
    private String name;
    @ManyToOne
    @JoinColumn(name="course_id", nullable=false)
    private Course course;
    //    @ManyToOne
//    @JoinColumn(name="subject_id", nullable=false)
//    private Subject subject_id;
    private int duration_in_minutes;
    private String note_to_students;
    private String note_to_teacher;
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "exam_question",
            joinColumns = { @JoinColumn(name = "exam_id") },
            inverseJoinColumns = { @JoinColumn(name = "question_id") }
    )
    private List<Question> questions;
    @ManyToOne
    @JoinColumn(name="teacher_id", nullable=false)
    private Teacher teacher;
//    @OneToMany(mappedBy="exam")
//    private List<Grade> grades;

    private String exam_code_number;
    @OneToMany(mappedBy = "exam")
    private List<ReadyExam> readyExams;
    @OneToMany(mappedBy = "exam")
    private List<Exam_Question_points> points;

    public Exam(String name, Course course, int duration_in_minutes, String note_to_students, String note_to_teacher, Teacher teacher) {
        this.name = name;
        this.course = course;
        course.addExam(this);
//        this.subject_id = subject_id;
//        subject_id.addExam(this);
//        course.getSubject_id().addExam(this);
        this.duration_in_minutes = duration_in_minutes;
        this.note_to_students = note_to_students;
        this.note_to_teacher = note_to_teacher;
        this.questions = new ArrayList<Question>();
        this.teacher = teacher;
        teacher.addExam(this);
//        grades = new ArrayList<Grade>();
        exam_code_number = "need to update- check appropriate function- updateCode()";
        this.readyExams = new ArrayList<ReadyExam>();
        this.points = new ArrayList<Exam_Question_points>();
    }

    public List<Exam_Question_points> getPoints() {
        return points;
    }

    public void addPoints(Exam_Question_points points)
    {
        this.points.add(points);
    }

    public void updateCode()
    {
        String first_part = String.valueOf(this.course.getSubject_id().getId());
        String second_part = String.valueOf(this.course.getId());
        String third_part = String.valueOf(this.id);
        if (this.course.getSubject_id().getId() < 10)
            first_part = "0" + first_part;
        if (this.course.getId() < 10)
            second_part = "0" + second_part;
        else if (this.id < 10)
            third_part = "0" + third_part;
        this.exam_code_number = first_part + second_part + third_part;
    }

    public void addReadyExam(ReadyExam ready_exam)
    {
        this.readyExams.add(ready_exam);
    }

    public List<ReadyExam> getReadyExams() {
        return readyExams;
    }

    public String getExam_code_number() {
        return exam_code_number;
    }

//    public List<Grade> getGrades() {
//        return grades;
//    }
//
//    public void addGrade(Grade g)
//    {
//        this.grades.add(g);
//    }
//
//    public void setGrades(List<Grade> grades) {
//        this.grades = grades;
//    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Exam_Question_points addQuestion(Question q, int points)
    {
        this.questions.add(q);
        q.addExam(this);
        Exam_Question_points p = new Exam_Question_points(this, q, points);
//        this.points.add(p);
        return p;
    }

    public String getName() {
        return name;
    }

    public Course getCourse() {
        return course;
    }

//    public Subject getSubject_id() {
//        return subject_id;
//    }

    public int getDuration_in_minutes() {
        return duration_in_minutes;
    }

    public String getNote_to_students() {
        return note_to_students;
    }

    public String getNote_to_teacher() {
        return note_to_teacher;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

//    public void setSubject_id(Subject subject_id) {
//        this.subject_id = subject_id;
//    }

    public void setDuration_in_minutes(int duration_in_minutes) {
        this.duration_in_minutes = duration_in_minutes;
    }

    public void setNote_to_students(String note_to_students) {
        this.note_to_students = note_to_students;
    }

    public void setNote_to_teacher(String note_to_teacher) {
        this.note_to_teacher = note_to_teacher;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getId() {
        return id;
    }
    public Exam()
    {

    }
}
