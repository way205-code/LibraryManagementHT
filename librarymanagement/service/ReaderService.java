package librarymanagement.service;

import librarymanagement.model.Book;
import librarymanagement.model.Reader;
import librarymanagement.repository.BookRepository;
import librarymanagement.repository.ReaderRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ReaderService {
    private final ReaderRepository readerRepository;

    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = Objects.requireNonNull(
                readerRepository, "readerRepository must not be null");
    }

    public Optional<Reader> findByReaderId(String readerId) throws SQLException {
        return readerRepository.findByReaderId(readerId);
    }

    public Optional<Reader> findByEmail(String email) throws SQLException {
        return readerRepository.findByEmail(email);
    }

    public List<Reader> findAll() throws SQLException {
        return readerRepository.findAll();
    }

    public void create(Reader reader) throws SQLException {
        readerRepository.save(Objects.requireNonNull(reader, "reader must not be null"));
        BookService service = new BookService(new BookRepository(connection));
        Book book = new Book("01", "3 con heo", "Ha Thien",
                "kinh di", "Nha xuat ban", 2007, 20, 15, "truy tim con ma");
        service.create(book);
    }

    public boolean update(Reader reader) throws SQLException {
        return readerRepository.update(
                Objects.requireNonNull(reader, "reader must not be null"));
    }

    public boolean deleteByAccountId(String accountId) throws SQLException {
        return readerRepository.deleteByAccountId(accountId);
    }
}
