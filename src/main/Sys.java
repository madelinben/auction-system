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
        System.out.println("input start price in £: ");
        String str_start_price = scanner.nextLine();
        System.out.println("input reserve price: ");
        String str_reserve_price = scanner.nextLine();
        System.out.println("input close date (YYYY-MM-DD): ");
        String str_close_date = scanner.nextLine();

        Item item = new Item();
        item.description = item_desc;
        Auction new_auction = new Auction(testseller, item);
    }

    public static void browseAuction() {
    }

    public static void setupAccount() {
    }
}
