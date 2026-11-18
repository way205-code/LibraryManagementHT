package librarymanagement.repository;

import librarymanagement.model.AccountStatus;
import librarymanagement.model.Admin;
import librarymanagement.model.Librarian;
import librarymanagement.model.Reader;
import librarymanagement.model.User;
import librarymanagement.model.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserRepository {
    private static final String COLUMNS =
            "account_id, username, password, full_name, account_status, role, "
                    + "reader_id, email, phone_number, address";

    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = Objects.requireNonNull(connection, "connection must not be null");
    }

    public Optional<User> findByAccountId(String accountId) throws SQLException {
        return findOne("SELECT " + COLUMNS + " FROM users WHERE account_id = ?", accountId);
    }

    public Optional<User> findByUsername(String username) throws SQLException {
        return findOne("SELECT " + COLUMNS + " FROM users WHERE username = ?", username);
    }

    public Optional<Reader> findReaderByReaderId(String readerId) throws SQLException {
        return findReader("SELECT " + COLUMNS
                + " FROM users WHERE reader_id = ? AND role = 'READER'", readerId);
    }

    public Optional<Reader> findReaderByEmail(String email) throws SQLException {
        return findReader("SELECT " + COLUMNS
                + " FROM users WHERE email = ? AND role = 'READER'", email);
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT " + COLUMNS + " FROM users";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }
        }

        return users;
    }

    public void save(User user) throws SQLException {
        Objects.requireNonNull(user, "user must not be null");
        String sql = "INSERT INTO users (" + COLUMNS
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            bindUser(statement, user);
            statement.executeUpdate();
        }
    }

    public boolean update(User user) throws SQLException {
        Objects.requireNonNull(user, "user must not be null");
        String sql = "UPDATE users SET username = ?, password = ?, full_name = ?, "
                + "account_status = ?, role = ?, reader_id = ?, email = ?, "
                + "phone_number = ?, address = ? WHERE account_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            bindUserFields(statement, user, 1);
            statement.setString(10, user.getAccountId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteByAccountId(String accountId) throws SQLException {
        String sql = "DELETE FROM users WHERE account_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountId);
            return statement.executeUpdate() > 0;
        }
    }

    private Optional<User> findOne(String sql, String value) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, value);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next()
                        ? Optional.of(mapUser(resultSet))
                        : Optional.empty();
            }
        }
    }

    private Optional<Reader> findReader(String sql, String value) throws SQLException {
        Optional<User> user = findOne(sql, value);
        return user.isPresent()
                ? Optional.of((Reader) user.get())
                : Optional.empty();
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        String accountId = resultSet.getString("account_id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String fullName = resultSet.getString("full_name");
        AccountStatus status = parseEnum(
                AccountStatus.class, resultSet.getString("account_status"), "account_status");
        UserRole role = parseEnum(UserRole.class, resultSet.getString("role"), "role");

        switch (role) {
            case READER:
                return new Reader(accountId, username, password, fullName, status,
                        resultSet.getString("reader_id"), resultSet.getString("email"),
                        resultSet.getString("phone_number"), resultSet.getString("address"));
            case ADMIN:
                return new Admin(accountId, username, password, fullName, status);
            case LIBRARIAN:
                return new Librarian(accountId, username, password, fullName, status);
            default:
                throw new SQLException("Unsupported user role: " + role);
        }
    }

    private <T extends Enum<T>> T parseEnum(Class<T> enumType, String value, String columnName)
            throws SQLException {
        if (value == null) {
            throw new SQLException("Column " + columnName + " must not be null");
        }
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException exception) {
            throw new SQLException("Invalid value for column " + columnName + ": " + value,
                    exception);
        }
    }

    private void bindUser(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getAccountId());
        bindUserFields(statement, user, 2);
    }

    private void bindUserFields(PreparedStatement statement, User user, int startIndex)
            throws SQLException {
        statement.setString(startIndex, user.getUsername());
        statement.setString(startIndex + 1, user.getPassword());
        statement.setString(startIndex + 2, user.getFullName());
        statement.setString(startIndex + 3,
                user.getAccountStatus() == null ? null : user.getAccountStatus().name());
        statement.setString(startIndex + 4, user.getRole().name());

        if (user instanceof Reader) {
            Reader reader = (Reader) user;
            statement.setString(startIndex + 5, reader.getReaderId());
            statement.setString(startIndex + 6, reader.getEmail());
            statement.setString(startIndex + 7, reader.getPhoneNumber());
            statement.setString(startIndex + 8, reader.getAddress());
        } else {
            statement.setString(startIndex + 5, null);
            statement.setString(startIndex + 6, null);
            statement.setString(startIndex + 7, null);
            statement.setString(startIndex + 8, null);
        }
    }
}
