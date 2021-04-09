package sys;

public abstract class User {
    protected String username;
    protected String password;

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static boolean checkPassword() {
        return true;
    }
}
