package il.cshaifasweng.OCSFMediatorExample.entities;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "question_generator")
    @GenericGenerator(name = "question_generator", strategy = "increment")
    private int id;
    @Column(name = "question_text")
    private String text;

//    @Column(name = "answers_list")
//    private List<String> answers;

    @Column(name = "question_answer")
    private String answer;

//    @Column(name = "question_answer2")
//    private String answer2;
//    @Column(name = "question_answer3")
//    private String answer3;
//    @Column(name = "question_answer4")
//    private String answer4;
    private int correct_answer;
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "question_subject",
            joinColumns = { @JoinColumn(name = "question_id") },
            inverseJoinColumns = { @JoinColumn(name = "course_id") }
    )
    private List<Course> courses;
    private String question_code_number;


    public Question(String text, String answer, int correct_answer, Subject subject) {
        this.text = text;
//        this.answer1 = answer1;
//        this.answer2 = answer2;
//        this.answer3 = answer3;
//        this.answer4 = answer4;

//        this.answers = List.copyOf(answers);
        this.answer=answer;

        this.correct_answer = correct_answer;
        this.subject = subject;
        subject.addQuestion(this);
        this.courses = new ArrayList<Course>();
        String first_part = String.valueOf(this.subject.getId());;
        String next_part = String.valueOf(this.id);;
        if (this.subject.getId() < 10)
            first_part = "0" + first_part;
        if (this.id < 10)
            next_part = "00" + next_part;
        else if (this.id < 100)
            next_part = "0" + next_part;
        this.question_code_number = first_part + next_part;
    }

    public void addCourse(Course course)
    {
        this.courses.add(course);
        course.addQuestion(this);
    }

    public int getId() {
        return id;
    }

    public int getCorrect_answer() {
        return correct_answer;
    }

//    public List<String> getAnswers() {
//        return answers;
//    }

    public String getAnswers() {
        return answer;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public String getQuestion_code_number() {
        return question_code_number;
    }

    public String getText() {
        return text;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setCorrect_answer(int correct_answer) {
        this.correct_answer = correct_answer;
    }

//    public void setAnswers(List<String> answers) {
//        this.answers = answers;
//    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void setQuestion_code_number(String question_code_number) {
        this.question_code_number = question_code_number;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        String courses = "Courses' Name: ";
        for (Course course : this.courses)
        {
            courses += course.getName() +", ";
        }
        String answers = "Answers: " + this.answer + "Correct Answer: " + String.valueOf(this.correct_answer);
//        String answers = "Answers: ";
//        for(String answer : this.answers)
//        {
//            answers += answer + ", ";
//        }

        String str = "Subject: " + this.subject.getName() + "\nQuestion: " +
                this.text + "\n" + answers +"\n" + courses +"\n";

        return str;
    }
    public Question()
    {

    }
}
