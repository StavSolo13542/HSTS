package il.cshaifasweng.OCSFMediatorExample.entities;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "Principals")
public class Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "principal_generator")
    @GenericGenerator(name = "principal_generator", strategy = "increment")
    @Column(name = "principal_id")
    private int principal_id;

    @Column (name = "name")
    private String name;

    @Column (name = "password")
    private String password;

    @Column (name = "isLoggedIn")
    private Boolean isLoggedIn;

    public Principal(String name, String password, Boolean isLoggedIn)
    {
//        super()
        this.name = name;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
    }

    public Principal() {

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

    public int getPrincipal_id() {
        return principal_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}