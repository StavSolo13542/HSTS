package il.cshaifasweng.OCSFMediatorExample.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Pupils")
public class Pupil {
    //    private HashMap<String, String> grades;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "pupil_generator")
    @GenericGenerator(name = "pupil_generator", strategy = "increment")
    @Column (name = "id")
    private int id;

    @Column (name = "name")
    private String name;

    @Column (name = "password")
    private String password;

    @Column (name = "isLoggedIn")
    private Boolean isLoggedIn;

    //    @Column(name = "grades")
//    private String grades;
//    //    private ArrayList<Integer> grades;
    @OneToMany(mappedBy = "pupil")
    private List<Grade> grades;
    public Pupil(String name, String password, Boolean isLoggedIn)
    {
//        super();
        this.name = name;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
        this.grades = new ArrayList<Grade>();
    }

    public Pupil() {

    }
    public List<Grade> getGrades()
    {
        return this.grades;
    }

    public void addGrade(Grade g)
    {
        this.grades.add(g);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(Boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setPassword(String password) {
        this.password = password;
    }
}