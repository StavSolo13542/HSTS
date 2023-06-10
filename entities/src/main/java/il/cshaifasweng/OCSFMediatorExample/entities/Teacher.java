package il.cshaifasweng.OCSFMediatorExample.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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
    private int isLoggedIn;

    public Teacher(int teacher_id, String name, String password, int isLoggedIn)
    {
        super();
        this.teacher_id=teacher_id;
        this.name = name;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
    }

    public Teacher(){

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

    public int getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(int isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
}
