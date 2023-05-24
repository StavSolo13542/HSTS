package il.cshaifasweng.OCSFMediatorExample.server.entities;

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

    public Teacher(int teacher_id, String name)
    {
        super();
        this.teacher_id=teacher_id;
        this.name = name;
    }

    public Teacher(){

    }

    public String getName() {
        return name;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
