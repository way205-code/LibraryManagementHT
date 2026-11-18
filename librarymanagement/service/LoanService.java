package librarymanagement.service;

import librarymanagement.model.Loan;
import librarymanagement.repository.LoanRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LoanService {
    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = Objects.requireNonNull(
                loanRepository, "loanRepository must not be null");
    }

    public Optional<Loan> findByLoanId(String loanId) throws SQLException {
        return loanRepository.findByLoanId(loanId);
    }

    public List<Loan> findByReaderId(String readerId) throws SQLException {
        return loanRepository.findByReaderId(readerId);
    }

    public List<Loan> findAll() throws SQLException {
        return loanRepository.findAll();
    }

    public void create(Loan loan) throws SQLException {
        loanRepository.save(Objects.requireNonNull(loan, "loan must not be null"));
    }

    public boolean update(Loan loan) throws SQLException {
        return loanRepository.update(Objects.requireNonNull(loan, "loan must not be null"));
    }

    public boolean deleteByLoanId(String loanId) throws SQLException {
        return loanRepository.deleteByLoanId(loanId);
    }
}
