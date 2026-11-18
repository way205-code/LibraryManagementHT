package librarymanagement.repository;

import librarymanagement.model.Reader;
import librarymanagement.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ReaderRepository {
    private final UserRepository userRepository;

    public ReaderRepository(Connection connection) {
        this.userRepository = new UserRepository(
                Objects.requireNonNull(connection, "connection must not be null"));
    }

    public Optional<Reader> findByReaderId(String readerId) throws SQLException {
        return userRepository.findReaderByReaderId(readerId);
    }

    public Optional<Reader> findByEmail(String email) throws SQLException {
        return userRepository.findReaderByEmail(email);
    }

    public List<Reader> findAll() throws SQLException {
        List<Reader> readers = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            if (user instanceof Reader) {
                readers.add((Reader) user);
            }
        }
        return readers;
    }

    public void save(Reader reader) throws SQLException {
        userRepository.save(Objects.requireNonNull(reader, "reader must not be null"));
    }

    public boolean update(Reader reader) throws SQLException {
        return userRepository.update(Objects.requireNonNull(reader, "reader must not be null"));
    }

    public boolean deleteByAccountId(String accountId) throws SQLException {
        return userRepository.deleteByAccountId(accountId);
    }
}
