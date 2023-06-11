package org.example;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "course_generator")
    @GenericGenerator(name = "course_generator", strategy = "increment")
    private int id;
    private String answer_text;
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question to_question;

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
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public Question getQuestion() {
        return to_question;
    }

    @Override
    public String toString() {
        return this.answer_text;
    }

    public Answer() {}
}