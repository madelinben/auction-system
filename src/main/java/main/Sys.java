package main;

import sys.Auction;
import sys.Buyer;
import sys.Seller;
import sys.User;

import java.io.*;
import java.util.*;
import org.apache.commons.csv.*;
import org.apache.commons.io.input.*;

public class Sys {

    public static Scanner scanner = new Scanner(System.in);

    private ArrayList<Buyer> allBuyers = new ArrayList<Buyer>();
    private ArrayList<Seller> allSellers = new ArrayList<Seller>();
    private ArrayList<Auction> auctions = new ArrayList<Auction>();

    public void entry() throws java.lang.Exception {
        importUsers();
        displayMenu();
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
            System.out.println("Main Menu:\nQ - Quit");
            String operation = new Scanner(System.in).nextLine().trim().toLowerCase();
            char[] input = operation.toCharArray();
            if(input.length != 1){
                System.out.println("Please only enter 1 character to select a menu item.");
                displayMenu();
            }
            switch (input[0]) {
                case 'q':
                    scanner.close();
                    terminate = true;
                    break;
                default:
                    System.out.println("ERROR! User didn't select valid input case!");
                    break;
            }
        }
        System.exit(0);
    }

    public static void placeAuction() {
    }

    public static void browseAuction() {
    }

    public static void setupAccount() {
    }
}
