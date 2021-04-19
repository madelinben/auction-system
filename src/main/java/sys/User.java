package sys;

public abstract class User {
    protected String username;
    protected String password;

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return this.username; }

    public boolean checkPassword(String password) {
        if (password.equals(this.password)) { return true; } else { return false; }
    }
}
