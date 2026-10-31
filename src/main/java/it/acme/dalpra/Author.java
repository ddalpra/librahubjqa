package it.acme.dalpra;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Author extends PanacheEntity {
    public String name;
    public String surname;

    @OneToMany(mappedBy = "author")
    public List<Book> books;
}
