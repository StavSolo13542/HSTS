package il.cshaifasweng.OCSFMediatorExample.server.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "StudentsWithGrades")
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
    private int isLoggedIn;

    @Column(name = "grades")
    private String grades;
//    private ArrayList<Integer> grades;
    public Pupil(int id, String name, String grades, String password, int isLoggedIn)
    {
        super();
        this.id=id;
        this.name = name;
        this.grades = grades;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
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

    public String getPassword() {
        return password;
    }

    public int getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(int loggedIn) {
        isLoggedIn = loggedIn;
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

    public void setPassword(String password) {
        this.password = password;
    }
}
