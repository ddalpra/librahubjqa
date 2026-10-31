package it.acme.dalpra;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Member extends PanacheEntity {
    public String firstName;
    public String lastName;
    public String email;
}
