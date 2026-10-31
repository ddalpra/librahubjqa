package it.acme.dalpra;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Book extends PanacheEntity {
    public String title;
    public String isbn;
    public Integer publicationYear;
    public boolean available = true;

    @ManyToOne
    public Author author;

    @ManyToOne
    public Category category;
}
