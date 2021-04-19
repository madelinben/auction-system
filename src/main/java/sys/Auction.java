package sys;

import main.Sys;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Auction {
    private double startPrice;
    private double reservePrice;
    private LocalDate closeDate;
    private char status;

    public Seller owner;
    public Item item;
    public ArrayList<Bid> allBids = new ArrayList<Bid>();

    public Auction(Seller owner, Item item, double startPrice, double reservePrice, int timeLimit){
        this.owner = owner;
        this.item = item;
        this.startPrice = startPrice;
        this.reservePrice = reservePrice;
        this.closeDate = LocalDate.now().plusDays(timeLimit);
    }

    public void placeBid(Buyer account) throws IOException {
        int count = 0;
        boolean terminate = false;
        while (count<3 && !terminate) {
            System.out.print("Would you like to place a bid [Y/N]?");
            String userInput = new Scanner(System.in).nextLine().trim().toLowerCase();
            if (userInput.equals("y")) {
                count = 0;
                boolean valid = false;
                while (count<3 && !valid) {
                    double price = Sys.getAnswerDouble("Enter Bid Amount: ", -1);
                    valid = verify(price);
                    if (valid) {
                        System.out.printf("Successful! %s your Bid has been placed.", account.getUsername());
                        allBids.add(new Bid(price, account));
                        ArrayList<String> csvRow = new ArrayList<String>(Arrays.asList(this.item.description, account.getUsername(), Double.toString(price), LocalDate.now().toString()));
                        Sys.writeCSV("bid.csv", csvRow);
                        terminate = true;
                    } else {
                        System.out.println("Error placing your Bid! System enforces an upper/lower bidding increment of 20% and 10% for the Item Starting Price.");
                    }
                }
            } else if (userInput.equals("n")) {
                System.out.println("Returning to Main Menu.");
                terminate = true;
            } else {
                System.out.println("ERROR! Invalid value provided.");
                count++;
            }
        }
        return;
    }

    public Bid getHighestBid() {
        if (!this.allBids.isEmpty()) {
            Bid highestBid = this.allBids.get(0);
            for (Bid bid : this.allBids) {
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

    public boolean verify(double price) {
        double upper = price + 2*(this.startPrice/10);//1.2;
        double lower = price + this.startPrice/10;//0.9;
        if ((price >= lower) && (price <= upper)) {
            return true;
        } else {
            return false;
        }
    }

    public double getLowerBidInc() { return this.startPrice/10; }
    public double getUpperBidInc() { return 2*(this.startPrice/10); }

    public static void close() {
    }

    public static boolean isBlocked() {
        return true;
    }

    public static void setBlocked() {
    }
}
