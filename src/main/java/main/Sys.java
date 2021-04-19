package main;

import sys.*;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.apache.commons.csv.*;
import org.apache.commons.io.input.*;

public class Sys {

    public static Scanner scanner = new Scanner(System.in);

    public static String accountSession = null;

    private static ArrayList<Buyer> allBuyers = new ArrayList<Buyer>();
    private static ArrayList<Seller> allSellers = new ArrayList<Seller>();
    private static ArrayList<Auction> allAuctions = new ArrayList<Auction>();

    /**
     * Main project Structure and Entry Point.
     */
    public void entry() throws java.lang.Exception {
        importUsers();
        importAuctions();
        importBids();
        displayMenu();
    }

    /**
     * Service method that formats CSV resource to obtain contained row data.
     * * @return CSV Parser Object to increment through collected records.
     * @throws IOException
     */
    public static CSVParser readCSV(String src) throws IOException {
        String path = System.getProperty("user.dir") + "/src/main/resources/";
        File modelCSV = new File(path + src);
        InputStream modelData = new FileInputStream(modelCSV);
        CSVParser parser = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(new InputStreamReader(new BOMInputStream(modelData), "UTF-8"));
        return parser;
    }

    /**
     * Service method that formats String array of data as a CSV row.
     * @throws IOException
     */
    public static void writeCSV(String src, ArrayList<String> csvData) throws IOException {
        try {
            String path = System.getProperty("user.dir") + "/src/main/resources/";
            FileWriter csvWriter = new FileWriter((path + src), true);
            CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
            CSVPrinter csvPrinter = new CSVPrinter(csvWriter, csvFormat);
            csvPrinter.printRecord(csvData);
            csvWriter.flush();
            csvWriter.close();
            csvPrinter.close();
        } catch (Exception e) {
            System.out.println("ERROR! Parsing CSV Record.");
        }
    }

    /**
     * Imports Bids from resource file and implements them into the relevant Auction Object in allAuctions array.
     * @throws IOException
     */
    public void importBids() throws IOException {
        CSVParser parser = readCSV("bid.csv");
        for (CSVRecord record : parser) {
            String auctionName = null, buyerString = null;
            double bidAmount = -1;
            LocalDate bidDate = null;
            if (record.isSet("Auction")) {
                if (!record.get("Auction").isEmpty()) {
                    auctionName = record.get("Auction");
                }
            }
            if (record.isSet("Buyer")) {
                if (!record.get("Buyer").isEmpty()) {
                    buyerString = record.get("Buyer");
                }
            }
            Buyer buyerObj = null;
            if (buyerString!=null) {
                for (Buyer user : allBuyers) {
                    if (user.getUsername().equals(buyerString)) {
                        buyerObj = user;
                    }
                }
            }
            if (record.isSet("Price")) {
                if (!record.get("Price").isEmpty()) {
                    bidAmount = Double.parseDouble(record.get("Price"));
                }
            }
            if (record.isSet("Date")) {
                if (!record.get("Date").isEmpty()) {
                    bidDate = LocalDate.parse(record.get("Date"));
                }
            }
            if (auctionName!=null && buyerObj!=null && bidAmount!=-1 && bidDate!=null) {
                for (Auction auction : allAuctions) {
                    if (auction.item.description.equals(auctionName)) {
                        auction.allBids.add(new Bid(bidAmount, buyerObj, bidDate));
                    }
                }
            }
        }
    }

    /**
     * Imports Users from resource file and puts them into allBuyers or allSellers array based on Account Type.
     * @throws IOException
     */
    public void importUsers() throws IOException {
        String src = System.getProperty("user.dir") + "/src/main/resources/";
        File accountCSV = new File(src + "user.csv");
        InputStream accountData = new FileInputStream(accountCSV);
        CSVParser parser = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(new InputStreamReader(new BOMInputStream(accountData), "UTF-8"));
        for (CSVRecord record : parser) {
            String accountName = null, accountPassword = null;
            if (record.isSet("Name")) {
                if (!record.get("Name").isEmpty()) {
                    accountName = record.get("Name");
                }
            }
            if (record.isSet("Password")) {
                if (!record.get("Password").isEmpty()) {
                    accountPassword = record.get("Password");
                }
            }
            if (record.isSet("isSeller")) {
                if (!record.get("isSeller").isEmpty()) {
                    if (Boolean.parseBoolean(record.get("isSeller")) == true) {
                        if (record.isSet("Blocked")) {
                            if (!record.get("Blocked").isEmpty()) {
                                boolean accountBlocked = Boolean.parseBoolean(record.get("Blocked"));
                                allSellers.add(new Seller(accountName, accountPassword, accountBlocked));
                            }
                        }
                    } else {
                        allBuyers.add(new Buyer(accountName, accountPassword));
                    }
                }
            }
        }
    }

    /**
     * Imports Auctions from resource file and puts them into allAuctions array.
     * @throws IOException
     */
    public void importAuctions() throws IOException {
        String src = System.getProperty("user.dir") + "/src/main/resources/";
        File auctionCSV = new File(src + "auction.csv");
        InputStream auctionData = new FileInputStream(auctionCSV);
        CSVParser parser = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(new InputStreamReader(new BOMInputStream(auctionData), "UTF-8"));
        for (CSVRecord record : parser) {
            String sellerName = null;
            Item item = new Item();
            Double startPrice = null;
            Double reservePrice = null;
            Integer timeLimit = null;
            String status = null;
            if (record.isSet("Item")) {
                if (!record.get("Item").isEmpty()) {
                    item.description = record.get("Item");
                }
            }
            if (record.isSet("Seller")) {
                if (!record.get("Seller").isEmpty()) {
                    sellerName = record.get("Seller");
                }
            }
            if (record.isSet("StartPrice")) {
                if (!record.get("StartPrice").isEmpty()) {
                    startPrice = Double.valueOf(record.get("StartPrice"));
                }
            }
            if (record.isSet("ReservePrice")) {
                if (!record.get("ReservePrice").isEmpty()) {
                    reservePrice = Double.valueOf(record.get("ReservePrice"));
                }
            }
            if (record.isSet("CloseDate")) {
                if (!record.get("CloseDate").isEmpty()) {
                    LocalDate closeDate = LocalDate.parse(record.get("CloseDate"));
                    timeLimit = (int)ChronoUnit.DAYS.between(LocalDate.now(), closeDate);
                }
            }
            if (record.isSet("Status")) {
                if (!record.get("Status").isEmpty()) {
                    status = record.get("Status");
                }
            }
            if (sellerName != null && startPrice != null && reservePrice != null && timeLimit != null && status != null) {
                Auction newAuction = new Auction(getSeller(sellerName), item, startPrice, reservePrice, timeLimit);
                newAuction.status = Auction.Status.valueOf(status);
                allAuctions.add(newAuction);
            }
        }
    }

    /**
     * Displays a formatted Menu to navigate project features.
     */
    public static void displayMenu() throws Exception {
        boolean terminate = false;
        while (!terminate) {
            updateAuctionTimers();
            String header = "\nMenu:\nA - Create Account\nB - ";
            if (accountSession == null) {header += "Sign In";} else {header += "Sign Out";}
            System.out.print(header + "\nC - Create Auction\nD - Browse Auctions\nV - Verify Auction\nQ - Quit\n\nInput: ");
            String userInput = scanner.nextLine().trim().toLowerCase();
            char[] input = userInput.toCharArray();
            if (input.length != 1) {
                System.out.println("ERROR! Please select a valid input case.");
            } else {
                switch (input[0]) {
                    case 'a':
                        accountSetup();
                        break;
                    case 'b':
                        if (accountSession==null) {
                            accountAuth();
                        } else {
                            accountSession = null;
                        }
                        break;
                    case 'c':
                        if ((accountSession != null) && (!allSellers.isEmpty())) {
                            placeAuction(getSeller(accountSession));
                        }
                        else{
                            System.out.println("Not logged in.");
                        }
                        break;
                    case 'd':
                        viewAuctions();
                        break;
                    case 'v':
                        if ((accountSession != null) && (!allSellers.isEmpty())) {
                            verifyAuction(getSeller(accountSession));
                        }
                        else{
                            System.out.println("Not logged in.");
                        }
                        break;
                    case 'q':
                        scanner.close();
                        terminate = true;
                        break;
                    default:
                        System.out.println("ERROR! Please select a valid input case.");
                        break;
                }
            }
        }
        System.exit(0);
    }

    /**
     * Formats an User Object based on the Account Type.
     * @throws IOException
     */
    public static void accountSetup() throws IOException {
        boolean terminate = false;
        int count = 0;
        while (count<3 && !terminate) {
            System.out.print("Account Type [Buyer(B)/Seller(S)]: ");
            String inputType = scanner.nextLine().trim().toLowerCase();
            if (inputType.equals("b") || inputType.equals("s")) {
                String inputUser = null, inputPwd = null;
                boolean valid = false;
                while (!valid) {
                    System.out.print("Enter Username: ");
                    inputUser = scanner.nextLine();
                    if (inputUser.matches("^[-\\\\w.]+$")) {
                        count++;
                        System.out.println("ERROR! Username should not include any special characters.");
                    } else {
                        valid = true;
                    }
                }
                valid = false;
                count = 0;
                while (!valid) {
                    System.out.print("Enter Password: ");
                    inputPwd = scanner.nextLine();
                    if (inputPwd.length()<8) {
                        count++;
                        System.out.println("ERROR! Password must be 8 digits or longer.");
                    } else {
                        valid = true;
                    }
                }
                try {
                    FileWriter csvWriter = new FileWriter(System.getProperty("user.dir") + "/src/main/resources/user.csv", true);
                    CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
                    CSVPrinter csvPrinter = new CSVPrinter(csvWriter, csvFormat);
                    if (inputType.equals("s")) {
                        csvPrinter.printRecord(inputUser, inputPwd, true, false);
                        allSellers.add(new Seller(inputUser, inputPwd, false));
                        System.out.println("Successfully Created New Seller Account.");
                    } else {
                        csvPrinter.printRecord(inputUser, inputPwd, false, false);
                        allBuyers.add(new Buyer(inputUser, inputPwd));
                        System.out.println("Successfully Created New Buyer Account.");
                    }
                    csvWriter.flush();
                    csvWriter.close();
                    csvPrinter.close();
                } catch (IOException e) {
                    System.out.println("ERROR! Parsing data to CSV resource.");

                }
                terminate = true;
            } else {
                System.out.println("ERROR! Please select a valid Account Type.");
            }
        }
    }

    /**
     * Authenticates Account Credentials and sets ID as Global Session.
     */
    public static void accountAuth() {
        int count = 0;
        boolean terminate = false;
        boolean found = false;
        while (count<3 && !terminate) {
            count++;
            System.out.print("Enter Username: ");
            String inputUser = scanner.nextLine();
            System.out.print("Enter Password: ");
            String inputPwd = scanner.nextLine();
            boolean valid = false;
            for (Buyer account : allBuyers) {
                if (account.getUsername().equals(inputUser)) {
                    found = true;
                    if (account.checkPassword(inputPwd)) {
                        valid = true;
                        break;
                    } else {
                        System.out.println("ERROR! Invalid credentials.");
                        count++;
                    }
                }
            }
            if (!found) {
                count = 0;
                for (Seller account : allSellers) {
                    if (account.getUsername().equals(inputUser)) {
                        found = true;
                        if (account.checkPassword(inputPwd)) {
                            valid = true;
                            break;
                        } else {
                            System.out.println("ERROR! Invalid credentials.");
                            count++;
                        }
                    }
                }
            }
            if (!found) {
                System.out.println("ERROR! User Account does not exist.");
            }
            if (valid) {
                accountSession = inputUser;
                System.out.println("Successfully Signed into Account.");
            }
            terminate = true;
        }
    }

    /**
     * Updates the timers for auctions, closes them when necessary.
     */
    public static void updateAuctionTimers(){
        for (Auction auction : allAuctions){
            if (auction.status == Auction.Status.OPEN){
                if (auction.closeDate.isBefore(LocalDate.now())){
                    auction.close();
                }
            }
        }
    }

    /**
     * Allows the user to create an auction and add item information.
     * @param seller the seller selling the item to be sold.
     */
    public static void placeAuction(Seller seller) {
        System.out.print("Enter a description of the item: ");
        Item item = new Item();
        item.description = scanner.nextLine();
        double startPrice = getAnswerDouble("Enter starting price in £(x.xx): ", -1);
        if (startPrice < 0) {System.out.println("cancelling auction creation."); return;}
        double reservePrice = getAnswerDouble("Enter reserve price in £(x.xx): ", -1);
        if (reservePrice < 0) {System.out.println("cancelling auction creation."); return;}
        int daysTillClose = getAnswerInt("In how many days will the auction close (0-7 incl.): ", -1);
        if (daysTillClose < 0 || daysTillClose > 7) {System.out.println("cancelling auction creation."); return;}
        Auction newAuction = new Auction(seller, item, startPrice, reservePrice, daysTillClose);
        allAuctions.add(newAuction);

        ArrayList<String> auctionData = new ArrayList<String>();
        auctionData.add(item.description);
        auctionData.add(seller.getUsername());
        auctionData.add(String.valueOf(startPrice));
        auctionData.add(String.valueOf(reservePrice));
        auctionData.add(LocalDate.now().plusDays(daysTillClose).toString());
        auctionData.add(newAuction.status.name());
        try {
            writeCSV("auction.csv", auctionData);
        }
        catch (Exception exception){
            System.out.println("Couldn't write auction to file.");
        }
    }

    /**
     * Allows the user to change the status of their pending auctions.
     * @param seller the logged in seller.
     */
    public static void verifyAuction(Seller seller){
        int i=0;
        while (i<3) {
            i++;
            System.out.println("Num|Item|Seller|Status");
            ArrayList<Auction> relevantAuctions = new ArrayList<Auction>();
            int j=0;
            for (Auction auction : allAuctions){
                if (auction.owner == seller && auction.status == Auction.Status.PENDING) {
                    relevantAuctions.add(auction);
                    System.out.printf("%d|%s|%s|%s\n", j, auction.item.description, auction.owner.getUsername(), auction.status.toString());
                    j++;
                }
            }
            int choice = getAnswerInt("Enter the number of the auction you want to select: ", -1);
            if (choice >= 0) {
                try {
                    relevantAuctions.get(choice).verifyStatus();
                    return;
                } catch (Exception exception) {
                    System.out.println("Number is out of bounds.");
                }
            }
        }
        System.out.println("Nothing changed.");
    }

    /**
     * Displays all auctions to the user.
     */
    public static void viewAuctions() throws IOException {
        System.out.format("%n| %-5s | %-12s | %-12s | %-11s | %-11s | %-17s |%n|=======|==============|==============|=============|=============|===================|%n", "Index", "Item", "Seller", "Highest Bid", "Start Price", "Bidding Increment");
        for (int i=0; i<allAuctions.size(); i++) {
            Auction auction = allAuctions.get(i);
            Bid highestBid = auction.getHighestBid();
            double highestAmount;
            if (highestBid == null) { highestAmount = 0; }
            else {highestAmount = highestBid.amount;}
            System.out.format("| %-5d | %-12s | %-12s | £%-10.2f | £%-10.2f | £%-7.2f-£%-7.2f |%n", i+1, auction.item.description, auction.owner.getUsername(), highestAmount, auction.startPrice, auction.getLowerBidInc(), auction.getUpperBidInc());
        }
        if (accountSession!=null) {
            Buyer account = null;
            for (Buyer user : allBuyers) {
                if (user.getUsername().equals(accountSession)) { account = user; }
            }
            if (account!=null) {
                int count = 0;
                boolean terminate = false;
                while (count<3 && !terminate) {
                    System.out.print("\nWould you like to place a bid [Y/N]?");
                    String userInput = new Scanner(System.in).nextLine().trim().toLowerCase();
                    if (userInput.equals("y")) {
                        Auction selected = selectAuction();
                        if (selected != null) {
                            selected.placeBid(account);
                            terminate = true;
                        }
                    } else if (userInput.equals("n")) {
                        System.out.println("Returning to Main Menu.");
                        terminate = true;
                    } else {
                        System.out.println("ERROR! Invalid value provided.");
                        count++;
                    }
                }
            } else {
                System.out.println("\nMust be signed into a Buyer Account in order to Place a Bid.");
            }
        } else {
            System.out.println("\nMust be signed into a Buyer Account in order to Place a Bid.");
        }
    }

    /**
     * Allows user to select an auction from allAuctions array.
     * @return Auction selected by user.
     */
    public static Auction selectAuction() {
        int count = 0;
        while (count<3) {
            int choice = getAnswerInt("Enter the Auction Index you want to select: ", -1);
            if (choice >= 0) {
                try {
                    return allAuctions.get(choice-1);
                } catch (Exception exception) {
                    System.out.println("ERROR! Auction Index is out of bounds.");
                }
            }
            count++;
        }
        System.out.println("No Auction Selected.");
        return null;
    }

    /**
     * Finds the seller object for a given username.
     * @param username of the seller in question.
     * @return seller object with matching username
     */
    public static Seller getSeller(String username){
        Seller loggedSeller = allSellers.get(0);
        for (Seller seller : allSellers){
            if (username.equals(seller.getUsername())){
                return seller;
            }
        }
        return loggedSeller;
    }

    /**
     * Helper function to get input from user, with 3 attempts.
     * @param question String containing question.
     * @param defaultInt Default return value.
     * @return either default or successful user input.
     */
    public static int getAnswerInt(String question, int defaultInt){
        int i=0;
        while (i<3){
            System.out.print(question);
            try {
                int answer = Integer.parseInt(scanner.nextLine());
                return(answer);
            }
            catch (Exception NumberFormatException){
                System.out.println("Cannot parse number, try again.");
            }
            i++;
        }
        return(defaultInt);
    }

    /**
     * Helper function to get input from user, with 3 attempts.
     * @param question String containing question.
     * @param defaultDouble Default return value.
     * @return either default or successful user input.
     */
    public static double getAnswerDouble(String question, double defaultDouble){
        int i=0;
        while (i<3){
            System.out.print(question);
            try {
                double answer = Double.parseDouble(scanner.nextLine());
                return(answer);
            }
            catch (Exception NumberFormatException){
                System.out.println("Cannot parse number, try again.");
            }
            i++;
        }
        return(defaultDouble);
    }
}
