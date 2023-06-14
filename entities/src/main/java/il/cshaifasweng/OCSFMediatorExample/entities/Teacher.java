package il.cshaifasweng.OCSFMediatorExample.entities;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "teacher_generator")
    @GenericGenerator(name = "teacher_generator", strategy = "increment")
    @Column(name = "teacher_id")
    private int teacher_id;

    @Column (name = "name")
    private String name;

    @Column (name = "password")
    private String password;

    @Column (name = "isLoggedIn")
    private Boolean isLoggedIn;
    @OneToMany(mappedBy = "teacher")
    private List<Exam> exams;
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "teacher_subject",
            joinColumns = { @JoinColumn(name = "teacher_id") },
            inverseJoinColumns = { @JoinColumn(name = "subject_id") }
    )
    private List<Subject> subjects;

    public Teacher(String name, String password, Boolean isLoggedIn)
    {
//        super();
        this.name = name;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
        exams = new ArrayList<Exam>();
        this.subjects = new ArrayList<Subject>();
    }

    public void addSubject(Subject subject)
    {
        this.subjects.add(subject);
        subject.addTeacher(this);
    }

    public Teacher()
    {

    }

    public List<Exam> getExams() {
        return exams;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public Boolean getLoggedIn() {
        return isLoggedIn;
    }

    public void addExam(Exam e)
    {
        this.exams.add(e);
    }

    public String getName() {
        return name;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
}