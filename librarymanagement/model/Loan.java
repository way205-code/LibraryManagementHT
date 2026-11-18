package librarymanagement.model;

import java.time.LocalDate;

public class Loan {
    private String loanId;
    private Reader reader;
    private Book book;
    private int quantity;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(String loanId, Reader reader, Book book, int quantity,
                LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate) {
        this.loanId = loanId;
        this.reader = reader;
        this.book = book;
        this.quantity = quantity;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public LoanStatus getStatus() {
        if (returnDate != null) {
            return LoanStatus.RETURNED;
        }
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            return LoanStatus.OVERDUE;
        }
        return LoanStatus.BORROWED;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
