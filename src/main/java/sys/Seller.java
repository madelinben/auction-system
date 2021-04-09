package sys;

public class Seller extends User {
    private boolean isBlocked;

    public Seller(String username, String password) {
        super(username, password);
        this.isBlocked = false;
    }

    public static boolean isBlocked() {
        return true;
    }

    public static void setBlocked() {
    }
}
