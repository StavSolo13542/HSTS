package il.cshaifasweng.OCSFMediatorExample.server.entities;

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

    public Principal(int principal_id, String name)
    {
        super();
        this.principal_id=principal_id;
        this.name = name;
    }

    public Principal() {

    }

    public String getName() {
        return name;
    }

    public int getPrincipal_id() {
        return principal_id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
