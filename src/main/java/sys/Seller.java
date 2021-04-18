package sys;

public class Seller extends User {
    private boolean isBlocked;

    public Seller(String username, String password, boolean blocked) {
        super(username, password);
        this.isBlocked = blocked;
    }

    public void setBlocked() { this.isBlocked = !this.isBlocked; }
    public boolean isBlocked() { return this.isBlocked; }
}
