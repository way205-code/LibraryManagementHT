package librarymanagement.model;

public class Admin extends User {
    public Admin(String accountId, String username, String password,
                 String fullName, AccountStatus accountStatus) {
        super(accountId, username, password, fullName, accountStatus);
    }

    @Override
    public UserRole getRole() {
        return UserRole.ADMIN;
    }
}
