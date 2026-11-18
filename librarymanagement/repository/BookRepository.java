package librarymanagement.repository;

import librarymanagement.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BookRepository {
    private static final String COLUMNS = "book_id, title, author, category, publisher, publication_year, "
            + "total_copies, available_copies, description";

    private final Connection connection;

    public BookRepository() {
        this(null);
    }

    public BookRepository(Connection connection) {
        this.connection = connection;
    }

    public Optional<Book> findByBookId(String bookId) throws SQLException {
        return findOne("SELECT " + COLUMNS + " FROM books WHERE book_id = ?", bookId);
    }

    public List<Book> findByTitle(String title) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT " + COLUMNS + " FROM books WHERE title LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + title + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    books.add(mapBook(resultSet));
                }
            }
        }
        return books;
    }

    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT " + COLUMNS + " FROM books";

        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                books.add(mapBook(resultSet));
            }
        }
        return books;
    }

    public void save(Book book) throws SQLException {
        Objects.requireNonNull(book, "book must not be null");
        String sql = "INSERT INTO books (" + COLUMNS
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            bindBook(statement, book, 1);
            statement.executeUpdate();
        }
    }

    public boolean update(Book book) throws SQLException {
        Objects.requireNonNull(book, "book must not be null");
        String sql = "UPDATE books SET title = ?, author = ?, category = ?, publisher = ?, "
                + "publication_year = ?, total_copies = ?, available_copies = ?, "
                + "description = ? WHERE book_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            bindBookFields(statement, book, 1);
            statement.setString(9, book.getBookId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteByBookId(String bookId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM books WHERE book_id = ?")) {
            statement.setString(1, bookId);
            return statement.executeUpdate() > 0;
        }
    }

    private Optional<Book> findOne(String sql, String value) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, value);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next()
                        ? Optional.of(mapBook(resultSet))
                        : Optional.empty();
            }
        }
    }

    private Book mapBook(ResultSet resultSet) throws SQLException {
        int publicationYear = resultSet.getInt("publication_year");
        Integer year = resultSet.wasNull() ? null : publicationYear;
        return new Book(resultSet.getString("book_id"), resultSet.getString("title"),
                resultSet.getString("author"), resultSet.getString("category"),
                resultSet.getString("publisher"), year, resultSet.getInt("total_copies"),
                resultSet.getInt("available_copies"), resultSet.getString("description"));
    }

    private void bindBook(PreparedStatement statement, Book book, int startIndex)
            throws SQLException {
        statement.setString(startIndex, book.getBookId());
        bindBookFields(statement, book, startIndex + 1);
    }

    private void bindBookFields(PreparedStatement statement, Book book, int startIndex)
            throws SQLException {
        statement.setString(startIndex, book.getTitle());
        statement.setString(startIndex + 1, book.getAuthor());
        statement.setString(startIndex + 2, book.getCategory());
        statement.setString(startIndex + 3, book.getPublisher());
        if (book.getPublicationYear() == null) {
            statement.setNull(startIndex + 4, Types.INTEGER);
        } else {
            statement.setInt(startIndex + 4, book.getPublicationYear());
        }
        statement.setInt(startIndex + 5, book.getTotalCopies());
        statement.setInt(startIndex + 6, book.getAvailableCopies());
        statement.setString(startIndex + 7, book.getDescription());
    }
}
