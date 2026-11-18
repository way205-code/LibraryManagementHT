package librarymanagement.service;

import librarymanagement.model.Book;
import librarymanagement.repository.BookRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BookService {

    private final BookRepository bookRepository;

    public BookService() {
        this(new BookRepository());
    }

    public BookService(BookRepository bookRepository) {
        this.bookRepository = Objects.requireNonNull(
                bookRepository, "bookRepository must not be null");
    }

    public Optional<Book> findByBookId(String bookId) throws SQLException {
        return bookRepository.findByBookId(bookId);
    }

    public List<Book> findByTitle(String title) throws SQLException {
        return bookRepository.findByTitle(title);
    }

    public List<Book> findAll() throws SQLException {
        return bookRepository.findAll();
    }

    public void create(Book book) throws SQLException {
        bookRepository.save(Objects.requireNonNull(book, "book must not be null"));

    }

    public boolean update(Book book) throws SQLException {
        return bookRepository.update(Objects.requireNonNull(book, "book must not be null"));
    }

    public boolean deleteByBookId(String bookId) throws SQLException {
        return bookRepository.deleteByBookId(bookId);
    }

    public static void main(String[] args) throws SQLException {
        BookService bookservice = new BookService();
        bookservice.create(new Book("01", "3 con heo", "Ha Thien",
                "kinh di", "Nha xuat ban", 2007, 20, 15, "truy tim con ma"));
    }
}
