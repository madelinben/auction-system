package main;

import sys.Auction;
import sys.User;
import sys.Seller;
import sys.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Sys {

    public static Scanner scanner = new Scanner(System.in);

    private ArrayList<User> users = new ArrayList<User>();
//    private ArrayList<Buyer> buyers = new ArrayList<Buyer>();
//    private ArrayList<Seller> sellers = new ArrayList<Seller>();
    private ArrayList<Auction> auctions = new ArrayList<Auction>();

    public static void displayMenu() throws java.lang.Exception{
        boolean terminate = false;
        while (!terminate) {
            System.out.println("Main Menu:\nA - Create Auction\nQ - Quit");
            String operation = new Scanner(System.in).nextLine().trim().toLowerCase();
            char[] input = operation.toCharArray();
            if(input.length != 1){
                System.out.println("Please only enter 1 character to select a menu item.");
                displayMenu();
            }
            switch (input[0]) {
                case 'a':
                    placeAuction();
                    break;
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
        System.out.println("placing auction");
        Seller testseller = new Seller("testname", "testpass");

        System.out.println("seller, input item description: ");
        String item_desc = scanner.nextLine();

        Item item = new Item();
        item.description = item_desc;

        double startPrice = getAnswerDouble("input start price in £: ", 0);
        double reservePrice = getAnswerDouble("input reserve price: ", 0);
        int daysTillClose = getAnswerInt("In how many days will the auction close: ", 1);

        Auction new_auction = new Auction(testseller, item, startPrice, reservePrice, daysTillClose);
    }

    public static void browseAuction() {
    }

    public static void setupAccount() {
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
