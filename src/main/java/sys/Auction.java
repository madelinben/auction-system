package sys;

import main.Sys;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

    public class Auction {
        public double startPrice;
        public enum Status {
            OPEN,
            PENDING,
            CLOSED,
            BLOCKED
        }
        public Buyer winner;
        private double reservePrice;
        public LocalDate closeDate;
        public Seller owner;
        public Item item;
        public ArrayList<Bid> allBids = new ArrayList<Bid>();
        public sys.Auction.Status status;

        public Auction(Seller owner, Item item, double startPrice, double reservePrice, int timeLimit){
            this.status = sys.Auction.Status.PENDING;
            this.owner = owner;
            this.item = item;
            this.startPrice = startPrice;
            this.reservePrice = reservePrice;
            this.closeDate = LocalDate.now().plusDays(timeLimit);
        }

        /**
         * Method stores formatted Bid for the selected Auction Object.
         * @param account Buyer User Object to identify and map Bid Owner.
         * @throws IOException
         */
        public void placeBid(Buyer account) throws IOException {
            int count = 0;
            boolean terminate = false;
            while (count<3 && !terminate) {
                double price = Sys.getAnswerDouble("Enter Bid Amount: ", -1);
                boolean valid = verifyIncrement(price);
                if (valid) {
                    System.out.format("Successful! %s your Bid has been placed.", account.getUsername());
                    allBids.add(new Bid(price, account, LocalDate.now()));
                    ArrayList<String> csvRow = new ArrayList<String>(Arrays.asList(this.item.description, account.getUsername(), Double.toString(price), LocalDate.now().toString()));
                    Sys.writeCSV("bid.csv", csvRow);
                    terminate = true;
                } else {
                    count++;
                    System.out.println("Error placing your Bid! System enforces an upper/lower bidding increment of 20% and 10% for the Item Starting Price.");
                }
            }
        }

        /**
         * Method identifies the highest bid made on the selected Auction.
         * @return Highest Bid Object.
         */
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

        /**
         * Method verifies if the new Bid Amount made is a valid bid increment.
         * @param price Bid Amount.
         * @return Boolean representing the result.
         */
        public boolean verifyIncrement(double price) {
            double upper = this.startPrice + 2*(this.startPrice/10);
            double lower = this.startPrice + this.startPrice/10;
            if ((price >= lower) && (price <= upper)) {
                return true;
            } else {
                return false;
            }
        }

        public double getLowerBidInc() { return this.startPrice/10; }
        public double getUpperBidInc() { return 2*(this.startPrice/10); }

        public void verifyStatus() {
            this.status = sys.Auction.Status.OPEN;
        }

        public void close() {
            this.status = sys.Auction.Status.CLOSED;
            if (!this.allBids.isEmpty()) {
                Bid highestBid = this.allBids.get(0);
                for (Bid bid : this.allBids) {
                    if (bid.amount >= highestBid.amount){
                        highestBid = bid;
                    }
                }
                if (highestBid.amount >= reservePrice){
                    this.winner = highestBid.who;
                    highestBid.who.victory(this);
                }
                for (Bid bid : this.allBids){
                    if (bid != highestBid){
                        bid.who.loss(this);
                    }
                }
            }
            this.winner = null;
        }

        public boolean isBlocked() {
            if (this.status == sys.Auction.Status.BLOCKED) {
                return true;
            } else {
                return false;
            }
        }

        public void setBlocked() {
            if (this.status == sys.Auction.Status.BLOCKED) {
                this.status = sys.Auction.Status.OPEN;
            } else {
                this.status = sys.Auction.Status.BLOCKED;
            }
        }
    }