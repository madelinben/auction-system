package sys;

import java.time.LocalDate;
import java.util.ArrayList;

public class Auction {
    private double startPrice;
    private double reservePrice;
    private LocalDate closeDate;
    private char status;

    private Seller owner;
    private Item item;
    private ArrayList<Bid> bids = new ArrayList<Bid>();

    public Auction(Seller owner, Item item, double startPrice, double reservePrice, int timeLimit){
        this.owner = owner;
        this.item = item;
        this.startPrice = startPrice;
        this.reservePrice = reservePrice;
        //code to convert timeLimit to closeDate
    }

    public static void placeBid() {
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
