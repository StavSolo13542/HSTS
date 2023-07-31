package il.cshaifasweng.OCSFMediatorExample.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="word_submission")
public class WordSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "word_submission_generator")
    @GenericGenerator(name = "word_submission_generator", strategy = "increment")
    private int id;
    @ManyToOne
    @JoinColumn(name="readyExam_id", nullable=false)
    private ReadyExam readyExam;
    @ManyToOne
    @JoinColumn(name="pupil_id", nullable=false)
    private Pupil pupil;
    private String content;
    public WordSubmission(ReadyExam readyExam, Pupil pupil, String content){
        this.readyExam = readyExam;
        this.pupil = pupil;
        this.content = content;
    }

    public WordSubmission() {

    }
}
