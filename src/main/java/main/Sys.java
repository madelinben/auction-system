package main;

import jdk.vm.ci.meta.Local;
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

    public void entry() throws java.lang.Exception {
        importUsers();
        importAuctions();
        importBids();
        displayMenu();
    }

    public static CSVParser readCSV(String src) throws IOException {
        String path = System.getProperty("user.dir") + "/src/main/resources/";
        File modelCSV = new File(path + src);
        InputStream modelData = new FileInputStream(modelCSV);
        CSVParser parser = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(new InputStreamReader(new BOMInputStream(modelData), "UTF-8"));
        return parser;
    }

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
//                allAuctions.forEach(a -> a.item.description.equals(finalAuctionName) ? a.allBids.add(new Bid(finalBidAmount, finalBuyerObj)) : null);
            }
        }
    }

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

    public void importAuctions() throws IOException {
        String src = System.getProperty("user.dir") + "/src/main/resources/";
        File auctionCSV = new File(src + "auction.csv");
        InputStream auctionData = new FileInputStream(auctionCSV);
        CSVParser parser = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(new InputStreamReader(new BOMInputStream(auctionData), "UTF-8"));
        for (CSVRecord record : parser) {
            String sellerName = null;
            Item item = new Item();
            Double startPrice = null, reservePrice = null;
            Integer timeLimit = null;
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
            if (sellerName != null && startPrice != null && reservePrice != null && timeLimit != null) {
                allAuctions.add(new Auction(getSeller(sellerName), item, startPrice, reservePrice, timeLimit));
            }
        }
    }

    public static void displayMenu() throws Exception {
        boolean terminate = false;
        while (!terminate) {
            System.out.println("Main Menu:\nA - Account Management\nB - Browse Auctions\nC - Create Auction\nQ - Quit");
            String userInput = scanner.nextLine().trim().toLowerCase();
            char[] input = userInput.toCharArray();
            if (input.length != 1) {
                System.out.println("ERROR! Please select a valid input case.");
            } else {
                switch (input[0]) {
                    case 'a':
                        displayAccountMenu();
                        break;
                    case 'b':
                        viewAuctions();
                        break;
                    case 'c':
                        if ((accountSession != null) && (!allSellers.isEmpty())) {
                            placeAuction(getSeller(accountSession));
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

    public static void displayAccountMenu() throws IOException {
        int count = 0;
        boolean terminate = false;
        while (count<3 && !terminate) {
            String menuHeader = "Account Management Menu:\nA - ";
            if (accountSession == null) {menuHeader += "Sign In";} else {menuHeader += "Sign Out";}
            System.out.println(menuHeader + "\nB - Create Account\nQ - Return to Menu");
            String userInput = scanner.nextLine().trim().toLowerCase();
            char[] input = userInput.toCharArray();
            if (input.length != 1) {
                System.out.println("ERROR! Please select a valid input case.");
            } else {
                switch (input[0]) {
                    case 'a':
                        if (accountSession==null) {
                            accountAuth();
                        } else {
                            accountSession = null;
                        }
                        terminate = true;
                        break;
                    case 'b':
                        terminate = true;
                        accountSetup();
                    case 'q':
                        terminate = true;
                        break;
                    default:
                        System.out.println("ERROR! Please select a valid input case.");
                        count++;
                        break;
                }
            }
        }
        return;
    }

    public static void accountSetup() throws IOException {
        boolean valid = false;
        while (!valid) {
            System.out.println("Account Type [Buyer(B)/Seller(S)]: ");
            String inputType = scanner.nextLine().trim().toLowerCase();
            if (inputType.equals("b") || inputType.equals("s")) {
                String inputUser, inputPwd;
                while (true) {
                    System.out.print("Username: ");
                    inputUser = scanner.nextLine();
                    if (inputUser.matches("^[-\\\\w.]+$")) {
                        System.out.println("ERROR! Username should not include any special characters.");
                    } else {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Password: ");
                    inputPwd = scanner.nextLine();
                    if (inputPwd.length()<8) {
                        System.out.println("ERROR! Password must be 8 digits or longer.");
                    } else {
                        break;
                    }
                }
                FileWriter csvWriter = new FileWriter(System.getProperty("user.dir") + "/src/main/resources/user.csv", true);
                CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
                CSVPrinter csvPrinter = new CSVPrinter(csvWriter, csvFormat);
                if (inputType.equals("s")) {
                    csvPrinter.printRecord(inputUser, inputPwd, true, false);
                    allSellers.add(new Seller(inputUser, inputPwd, false));
                } else {
                    csvPrinter.printRecord(inputUser, inputPwd, false, false);
                    allBuyers.add(new Buyer(inputUser, inputPwd));
                }
                csvWriter.flush();
                csvWriter.close();
                csvPrinter.close();
                valid = true;
            } else {
                System.out.println("ERROR! Please select a valid Account Type.");
            }
        }
        return;
    }

    public static void accountAuth() {
        int count = 0;
        boolean terminate = false;
        while (count<3 && !terminate) {
            count++;
            System.out.print("Account Login\nUsername: ");
            String inputUser = scanner.nextLine();
            System.out.print("Password: ");
            String inputPwd = scanner.nextLine();
            boolean valid = false;
            for (Buyer account : allBuyers) {
                if (account.getUsername().equals(inputUser)) {
                    if (account.checkPassword(inputPwd)) {
                        valid = true;
                    } else {
                        System.out.println("ERROR! Invalid credentials.");
                    }
                }
            }
            if (!valid) {
                for (Seller account : allSellers) {
                    if (account.getUsername().equals(inputUser)) {
                        if (account.checkPassword(inputPwd)) {
                            valid = true;
                        } else {
                            System.out.println("ERROR! Invalid credentials.");
                        }
                    } else {
                        System.out.println("ERROR! User Account does not exist.");
                    }
                }
            }
            if (valid) {
                accountSession = inputUser;
                return;
            }
        }
        return;
    }

    public static void placeAuction(Seller seller) {
        System.out.println("Enter a description of the item: ");
        Item item = new Item();
        item.description = scanner.nextLine();
        double startPrice = getAnswerDouble("Enter starting price in £(x.xx): ", -1);
        if (startPrice < 0) {System.out.println("cancelling auction creation."); return;}
        double reservePrice = getAnswerDouble("Enter reserve price in £(x.xx): ", -1);
        if (reservePrice < 0) {System.out.println("cancelling auction creation."); return;}
        int daysTillClose = getAnswerInt("In how many days will the auction close (0-7 incl.): ", -1);
        if (daysTillClose < 0) {System.out.println("cancelling auction creation."); return;}
        allAuctions.add(new Auction(seller, item, startPrice, reservePrice, daysTillClose));

        ArrayList<String> auctionData = new ArrayList<String>();
        auctionData.add(item.description);
        auctionData.add(seller.getUsername());
        auctionData.add(String.valueOf(startPrice));
        auctionData.add(String.valueOf(reservePrice));
        auctionData.add(LocalDate.now().plusDays(daysTillClose).toString());
        try {
            writeCSV("auction.csv", auctionData);
        }
        catch (Exception exception){
            System.out.println("Couldn't write auction to file.");
        }
    }

    public static void viewAuctions() throws IOException {
        System.out.format("| %-5s | %-12s | %-12s | %-11s | %-17s |%n|=======|==============|==============|=============|===================|%n", "Index", "Item", "Seller", "Highest Bid", "Bidding Increment");
        for (int i=0; i<allAuctions.size(); i++) {
            Auction auction = allAuctions.get(i);
            Bid highestBid = auction.getHighestBid();
            double highestAmount;
            if (highestBid == null) { highestAmount = 0; }
            else {highestAmount = highestBid.amount;}
            System.out.format("| %-5d | %-12s | %-12s | £%-10.2f | £%-7.2f-£%-7.2f |%n", i+1, auction.item.description, auction.owner.getUsername(), highestAmount, auction.getLowerBidInc(), auction.getUpperBidInc());
        }

        if (accountSession!=null) {
            Buyer account = null;
            for (Buyer user : allBuyers) {
                if (user.getUsername().equals(accountSession)) { account = user; }
            }
            if (account!=null) {
                Auction selected = selectAuction();
                if (selected!=null) {
                    selected.placeBid(account);
                }
            } else {
                System.out.println("Must be signed into a Buyer Account in order to Place a Bid.");
            }

        } else {
            System.out.println("Must be signed into a Buyer Account in order to Place a Bid.");
        }



    }

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

    public static Seller getSeller(String username){
        Seller loggedSeller = allSellers.get(0);
        for (Seller seller : allSellers){
            if (username.equals(seller.getUsername())){
                return seller;
            }
        }
        return loggedSeller;
    }

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
        }
        return(defaultInt);
    }

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
        }
        return(defaultDouble);
    }
}
