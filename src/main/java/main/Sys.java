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

    public static String accountSession = null;

    private static ArrayList<Buyer> allBuyers = new ArrayList<Buyer>();
    private static ArrayList<Seller> allSellers = new ArrayList<Seller>();
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
            String userInput = scanner.nextLine().trim().toLowerCase();
            char[] input = userInput.toCharArray();
            if (input.length != 1) {
                System.out.println("ERROR! Please select a valid input case.");
            } else {
                switch (input[0]) {
                    case 'a':
                        displayAccountMenu();
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

    public static void displayAccountMenu() {
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
                        accountAuth();
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

    public static void accountSetup() {
        boolean valid = false;
        while (!valid) {
            System.out.println("Account Type [Buyer(B)/Seller(S)]: ");
            String inputType = scanner.nextLine().trim().toLowerCase();
            if (inputType.equals("b") || inputType.equals("s")) {
                String inputUser, inputPwd;

                while (true) {
                    System.out.println("Username: ");
                    inputUser = scanner.nextLine();
                    if (inputUser.matches("^[-\\\\w.]+$")) {
                        System.out.println("ERROR! Username should not include any special characters.");
                    } else {
                        break;
                    }
                }

                while (true) {
                    System.out.println("Password: ");
                    inputPwd = scanner.nextLine();
                    if (inputPwd.length()<8) {
                        System.out.println("ERROR! Password must be 8 digits or longer.");
                    } else {
                        break;
                    }
                }

                if (inputType.equals("s")) {

                } else {

                }


            } else {
                System.out.println("ERROR! Please select a valid Account Type.");
            }






        }
    }

    public static void placeAuction() {
    }

    public static void browseAuction() {
    }

    public static void setupAccount() {
    }
}
