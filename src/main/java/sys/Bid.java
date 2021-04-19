package sys;

import java.time.LocalDate;

public class Bid {
    public double amount;
    public Buyer who;
    public LocalDate when;

    public Bid(double bidAmount, Buyer account) {
        this.amount = bidAmount;
        this.who = account;
        this.when = LocalDate.now();
    }
}
