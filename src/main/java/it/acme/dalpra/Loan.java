package it.acme.dalpra;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class Loan extends PanacheEntity {
    @ManyToOne
    public Book book;

    @ManyToOne
    public Member member;

    public LocalDate loanDate;
    public LocalDate dueDate;
    public LocalDate returnDate;
    public String status;
}
