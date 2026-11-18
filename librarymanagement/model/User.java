package librarymanagement.model;

public abstract class User {
    private String accountId;
    private String username;
    private String password;
    private String fullName;
    private AccountStatus accountStatus;

    public User() {
        super();
    }

    protected User(String accountId, String username, String password,
            String fullName, AccountStatus accountStatus) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.accountStatus = accountStatus;
    }

    public abstract UserRole getRole();

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
}
