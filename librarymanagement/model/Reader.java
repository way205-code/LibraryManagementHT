package librarymanagement.model;

public class Reader extends User {
    private String readerId;
    private String email;
    private String phoneNumber;
    private String address;

    public Reader() {
        super(null, null, null, null, null);
    }

    public Reader(String accountId, String username, String password,
            String fullName, AccountStatus accountStatus, String readerId,
            String email, String phoneNumber, String address) {
        super(accountId, username, password, fullName, accountStatus);
        this.readerId = readerId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    @Override
    public UserRole getRole() {
        return UserRole.READER;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
