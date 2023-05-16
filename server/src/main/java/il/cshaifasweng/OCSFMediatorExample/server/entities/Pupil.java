package il.cshaifasweng.OCSFMediatorExample.server.entities;

import java.util.ArrayList;
import java.util.HashMap;
import javax.persistence.*;
import javax.persistence.CascadeType;

@Entity
@Table(name = "StudentsWithGrades")
public class Pupil {
//    private HashMap<String, String> grades;
    @Id
    @Column (name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "name")
    private String name;

    @Column(name = "grades")
    private String grades;
//    private ArrayList<Integer> grades;
    public Pupil(int id, String name, String grades)
    {
        super();
        this.id=id;
        this.name = name;
        this.grades = grades;
    }

    public Pupil() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGrades() {
        return grades;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrades(String grades) {
        this.grades = grades;
    }
}
