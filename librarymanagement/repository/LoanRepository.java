package librarymanagement.repository;

import librarymanagement.model.Book;
import librarymanagement.model.Loan;
import librarymanagement.model.Reader;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LoanRepository {
    private static final String COLUMNS =
            "loan_id, reader_id, book_id, quantity, borrow_date, due_date, return_date";

    private final Connection connection;
    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;

    public LoanRepository(Connection connection) {
        this.connection = Objects.requireNonNull(connection, "connection must not be null");
        this.readerRepository = new ReaderRepository(connection);
        this.bookRepository = new BookRepository(connection);
    }

    public Optional<Loan> findByLoanId(String loanId) throws SQLException {
        List<LoanData> rows = query("SELECT " + COLUMNS
                + " FROM loans WHERE loan_id = ?", loanId);
        return rows.isEmpty() ? Optional.empty() : Optional.of(mapLoan(rows.get(0)));
    }

    public List<Loan> findByReaderId(String readerId) throws SQLException {
        return mapLoans(query("SELECT " + COLUMNS
                + " FROM loans WHERE reader_id = ?", readerId));
    }

    public List<Loan> findAll() throws SQLException {
        return mapLoans(query("SELECT " + COLUMNS + " FROM loans", null));
    }

    public void save(Loan loan) throws SQLException {
        Objects.requireNonNull(loan, "loan must not be null");
        String sql = "INSERT INTO loans (" + COLUMNS + ") VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, loan.getLoanId());
            bindLoanFields(statement, loan, 2);
            statement.executeUpdate();
        }
    }

    public boolean update(Loan loan) throws SQLException {
        Objects.requireNonNull(loan, "loan must not be null");
        String sql = "UPDATE loans SET reader_id = ?, book_id = ?, quantity = ?, "
                + "borrow_date = ?, due_date = ?, return_date = ? WHERE loan_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            bindLoanFields(statement, loan, 1);
            statement.setString(7, loan.getLoanId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteByLoanId(String loanId) throws SQLException {
        try (PreparedStatement statement =
                     connection.prepareStatement("DELETE FROM loans WHERE loan_id = ?")) {
            statement.setString(1, loanId);
            return statement.executeUpdate() > 0;
        }
    }

    private List<LoanData> query(String sql, String readerId) throws SQLException {
        List<LoanData> rows = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (readerId != null) {
                statement.setString(1, readerId);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rows.add(new LoanData(resultSet.getString("loan_id"),
                            resultSet.getString("reader_id"), resultSet.getString("book_id"),
                            resultSet.getInt("quantity"), readDate(resultSet, "borrow_date"),
                            readDate(resultSet, "due_date"), readDate(resultSet, "return_date")));
                }
            }
        }
        return rows;
    }

    private List<Loan> mapLoans(List<LoanData> rows) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        for (LoanData row : rows) {
            loans.add(mapLoan(row));
        }
        return loans;
    }

    private Loan mapLoan(LoanData row) throws SQLException {
        Reader reader = readerRepository.findByReaderId(row.readerId)
                .orElseThrow(() -> new SQLException("Reader not found: " + row.readerId));
        Book book = bookRepository.findByBookId(row.bookId)
                .orElseThrow(() -> new SQLException("Book not found: " + row.bookId));
        return new Loan(row.loanId, reader, book, row.quantity, row.borrowDate,
                row.dueDate, row.returnDate);
    }

    private void bindLoanFields(PreparedStatement statement, Loan loan, int startIndex)
            throws SQLException {
        if (loan.getReader() == null || loan.getBook() == null) {
            throw new IllegalArgumentException("loan must have a reader and a book");
        }
        statement.setString(startIndex, loan.getReader().getReaderId());
        statement.setString(startIndex + 1, loan.getBook().getBookId());
        statement.setInt(startIndex + 2, loan.getQuantity());
        setDate(statement, startIndex + 3, loan.getBorrowDate());
        setDate(statement, startIndex + 4, loan.getDueDate());
        setDate(statement, startIndex + 5, loan.getReturnDate());
    }

    private LocalDate readDate(ResultSet resultSet, String columnName) throws SQLException {
        Date value = resultSet.getDate(columnName);
        return value == null ? null : value.toLocalDate();
    }

    private void setDate(PreparedStatement statement, int index, LocalDate value)
            throws SQLException {
        if (value == null) {
            statement.setNull(index, Types.DATE);
        } else {
            statement.setDate(index, Date.valueOf(value));
        }
    }

    private static class LoanData {
        private final String loanId;
        private final String readerId;
        private final String bookId;
        private final int quantity;
        private final LocalDate borrowDate;
        private final LocalDate dueDate;
        private final LocalDate returnDate;

        private LoanData(String loanId, String readerId, String bookId, int quantity,
                         LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate) {
            this.loanId = loanId;
            this.readerId = readerId;
            this.bookId = bookId;
            this.quantity = quantity;
            this.borrowDate = borrowDate;
            this.dueDate = dueDate;
            this.returnDate = returnDate;
        }
    }
}
