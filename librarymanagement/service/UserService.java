package librarymanagement.service;

import librarymanagement.model.User;
import librarymanagement.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(
                userRepository, "userRepository must not be null");
    }

    public Optional<User> findByAccountId(String accountId) throws SQLException {
        return userRepository.findByAccountId(accountId);
    }

    public Optional<User> findByUsername(String username) throws SQLException {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() throws SQLException {
        return userRepository.findAll();
    }

    public void create(User user) throws SQLException {
        userRepository.save(Objects.requireNonNull(user, "user must not be null"));
    }

    public boolean update(User user) throws SQLException {
        return userRepository.update(Objects.requireNonNull(user, "user must not be null"));
    }

    public boolean deleteByAccountId(String accountId) throws SQLException {
        return userRepository.deleteByAccountId(accountId);
    }
}
