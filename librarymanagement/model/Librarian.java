package librarymanagement.model;

public class Librarian extends User {
    public Librarian(String accountId, String username, String password,
                     String fullName, AccountStatus accountStatus) {
        super(accountId, username, password, fullName, accountStatus);
    }

    @Override
    public UserRole getRole() {
        return UserRole.LIBRARIAN;
    }
}
