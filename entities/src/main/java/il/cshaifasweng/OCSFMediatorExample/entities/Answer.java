package il.cshaifasweng.OCSFMediatorExample.entities;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "answer_generator")
    @GenericGenerator(name = "answer_generator", strategy = "increment")
    private int id;
    private String answer_text;
    @ManyToOne
    @JoinColumn(name = "question_id"/*, nullable = false*/)
    private Question to_question;

    @Column(name = "is_correct")
    private boolean is_correct;

    public Answer(String answer_text, Boolean corr) {
        this.answer_text = answer_text;
        this.to_question = null;
        this.is_correct = corr;
    }
    public boolean getIs_correct() {
        return is_correct;
    }

    public void setIs_correct(boolean is_correct) {
        this.is_correct = is_correct;
    }

    public void setQuestion(Question to_question) {
        this.to_question = to_question;
//        to_question.addAnswer(this);      // this is commented because it creates a circle
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public Question getQuestion() {
        return to_question;
    }
    public Answer(String[] desc, int index) {
        int i = 1;
        String s = "";
        while(!desc[index + i].equals("is_correct")) {
            s += desc[i + index] + " ";
            i++;
        }
        this.answer_text = s;
        this.is_correct = Boolean.parseBoolean(desc[index + i + 1]);
    }
    @Override
    public String toString() {
        String s = "";
        s += "answer_starts_here " + this.answer_text;
        s += " is_correct " + this.is_correct + " ";
        return s;
    }

    public Answer() {}
}