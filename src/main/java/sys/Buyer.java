package sys;

import java.util.ArrayList;

public class Buyer extends User {

    public ArrayList<Auction> wins = new ArrayList<Auction>();
    public ArrayList<Auction> losses = new ArrayList<Auction>();

    public Buyer(String username, String password) {
        super(username, password);
    }

    public void victory(Auction auction) {
        wins.add(auction);
    }
    public void loss(Auction auction){
        losses.add(auction);
    }
}
