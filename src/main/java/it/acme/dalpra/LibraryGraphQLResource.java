package it.acme.dalpra;

import jakarta.transaction.Transactional;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import java.time.LocalDate;
import java.util.List;

@GraphQLApi
public class LibraryGraphQLResource {

    @Query
    public String sayHello() {
        return "Library API ready";
    }

    @Query
    public List<Book> books() {
        return Book.listAll();
    }

    @Query
    public List<Author> authors() {
        return Author.listAll();
    }

    @Query
    public List<Member> members() {
        return Member.listAll();
    }

    @Query
    public List<Loan> loans() {
        return Loan.listAll();
    }

    @Query
    public List<Book> availableBooks() {
        return Book.find("available = true").list();
    }

    @Mutation
    @Transactional
    public Book createBook(String title, String isbn, Long authorId, Long categoryId) {
        Author author = Author.findById(authorId);
        Category category = Category.findById(categoryId);

        if (author == null || category == null) {
            throw new IllegalArgumentException("Author or category not found");
        }

        Book book = new Book();
        book.title = title;
        book.isbn = isbn;
        book.publicationYear = LocalDate.now().getYear();
        book.author = author;
        book.category = category;
        book.available = true;
        book.persist();

        return book;
    }

    @Mutation
    @Transactional
    public Author createAuthor(String name, String surname) {
        Author author = new Author();
        author.name = name;
        author.surname = surname;
        author.persist();
        return author;
    }

    @Mutation
    @Transactional
    public Member createMember(String firstName, String lastName, String email) {
        Member member = new Member();
        member.firstName = firstName;
        member.lastName = lastName;
        member.email = email;
        member.persist();
        return member;
    }

    @Mutation
    @Transactional
    public Loan loanBook(Long bookId, Long memberId) {
        Book book = Book.findById(bookId);
        Member member = Member.findById(memberId);

        if (book == null || member == null) {
            throw new IllegalArgumentException("Book or member not found");
        }

        if (!book.available) {
            throw new IllegalArgumentException("Book is not available");
        }

        Loan loan = new Loan();
        loan.book = book;
        loan.member = member;
        loan.loanDate = LocalDate.now();
        loan.dueDate = LocalDate.now().plusDays(14);
        loan.status = "ACTIVE";
        loan.persist();

        book.available = false;
        book.persist();

        return loan;
    }

    @Mutation
    @Transactional
    public Loan returnBook(Long loanId) {
        Loan loan = Loan.findById(loanId);
        if (loan == null) {
            throw new IllegalArgumentException("Loan not found");
        }

        loan.returnDate = LocalDate.now();
        loan.status = "RETURNED";
        loan.persist();

        if (loan.book != null) {
            loan.book.available = true;
            loan.book.persist();
        }

        return loan;
    }
}
