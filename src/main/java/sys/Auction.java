package sys;

import java.time.LocalDate;
import java.util.ArrayList;

public class Auction {
    public enum Status {
        OPEN,
        PENDING,
        CLOSED
    }

    private double startPrice;
    private double reservePrice;
    private LocalDate closeDate;

    public Seller owner;
    public Item item;
    public ArrayList<Bid> bids = new ArrayList<Bid>();
    public Status status;

    public Auction(Seller owner, Item item, double startPrice, double reservePrice, int timeLimit){
        this.status = Status.PENDING;
        this.owner = owner;
        this.item = item;
        this.startPrice = startPrice;
        this.reservePrice = reservePrice;
        this.closeDate = LocalDate.now().plusDays(timeLimit);
    }

    public static void placeBid() {
    }

    public Bid getHighestBid() {
        if (!this.bids.isEmpty()) {
            Bid highestBid = this.bids.get(0);
            for (Bid bid : this.bids) {
                if (bid.amount > highestBid.amount) {
                    highestBid = bid;
                }
            }
            return highestBid;
        }
        else{
            return null;
        }
    }

    public static void verify() {
    }

    public static void close() {
    }

    public static boolean isBlocked() {
        return true;
    }

    public static void setBlocked() {
    }
}
