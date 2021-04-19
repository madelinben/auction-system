package main;

import sys.*;

import java.io.*;
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

    public static void displayMenu() throws Exception {
        boolean terminate = false;
        while (!terminate) {
            System.out.println("Main Menu:\nA - Account Management\nB - Create Auction\nC - Browse Auctions\nQ - Quit");
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
                        if ((accountSession != null) && (!allSellers.isEmpty())) {
                            Seller loggedSeller = allSellers.get(0);
                            for (Seller seller : allSellers){
                                if (accountSession.equals(seller.getUsername())){
                                    loggedSeller = seller;
                                }
                            }
                            placeAuction(loggedSeller);
                        }
                        else{
                            System.out.println("Not logged in.");
                        }
                        break;
                    case 'c':
                        viewAuctions();
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
                        scanner.close();
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
    }

    public static void viewAuctions() {
        int i=0;
        System.out.println("Num|Item|Seller|Highest bid");
        for (Auction auction : allAuctions){
            Bid highestBid = auction.getHighestBid();
            double highestAmount;
            if (highestBid == null) { highestAmount = 0; }
            else {highestAmount = highestBid.amount;}
            System.out.printf("%d|%s|%s|£%.2f\n", i, auction.item.description, auction.owner.getUsername(), highestAmount);
            i++;
        }
    }

    public static Auction selectAuction() {
        int i=0;
        while (i<3) {
            i++;
            viewAuctions();
            int choice = getAnswerInt("Enter the number of the auction you want to select: ", -1);
            if (choice >= 0) {
                try {
                    return allAuctions.get(choice);
                } catch (Exception exception) {
                    System.out.println("Number is out of bounds.");
                }
            }
        }
        System.out.println("No auction selected");
        return null;
    }

    public static int getAnswerInt(String question, int defaultInt){
        int i=0;
        while (i<3){
            System.out.println(question);
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
            System.out.println(question);
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
