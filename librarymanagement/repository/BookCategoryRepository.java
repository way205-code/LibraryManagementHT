package librarymanagement.repository;

import librarymanagement.model.BookCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BookCategoryRepository {
    private static final String COLUMNS = "category_id, name, description";

    private final Connection connection;

    public BookCategoryRepository(Connection connection) {
        this.connection = Objects.requireNonNull(connection, "connection must not be null");
    }

    public Optional<BookCategory> findByCategoryId(String categoryId) throws SQLException {
        return findOne("SELECT " + COLUMNS
                + " FROM book_categories WHERE category_id = ?", categoryId);
    }

    public List<BookCategory> findAll() throws SQLException {
        List<BookCategory> categories = new ArrayList<>();
        String sql = "SELECT " + COLUMNS + " FROM book_categories";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                categories.add(mapCategory(resultSet));
            }
        }
        return categories;
    }

    public void save(BookCategory category) throws SQLException {
        Objects.requireNonNull(category, "category must not be null");
        String sql = "INSERT INTO book_categories (" + COLUMNS + ") VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getCategoryId());
            statement.setString(2, category.getName());
            statement.setString(3, category.getDescription());
            statement.executeUpdate();
        }
    }

    public boolean update(BookCategory category) throws SQLException {
        Objects.requireNonNull(category, "category must not be null");
        String sql = "UPDATE book_categories SET name = ?, description = ? "
                + "WHERE category_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setString(3, category.getCategoryId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteByCategoryId(String categoryId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM book_categories WHERE category_id = ?")) {
            statement.setString(1, categoryId);
            return statement.executeUpdate() > 0;
        }
    }

    private Optional<BookCategory> findOne(String sql, String value) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, value);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next()
                        ? Optional.of(mapCategory(resultSet))
                        : Optional.empty();
            }
        }
    }

    private BookCategory mapCategory(ResultSet resultSet) throws SQLException {
        return new BookCategory(resultSet.getString("category_id"),
                resultSet.getString("name"), resultSet.getString("description"));
    }
}
