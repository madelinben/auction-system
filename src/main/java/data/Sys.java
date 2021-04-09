package data;

import sys.Auction;
import sys.User;

import java.io.*;
import java.util.*;
import org.apache.commons.csv.*;
import org.apache.commons.io.input.*;

public class Sys {

    public static Scanner scanner = new Scanner(System.in);

    private ArrayList<User> users = new ArrayList<User>();
//    private ArrayList<Buyer> buyers = new ArrayList<Buyer>();
//    private ArrayList<Seller> sellers = new ArrayList<Seller>();
    private ArrayList<Auction> auctions = new ArrayList<Auction>();

    public static void entry() throws java.lang.Exception {
        importStorage();
        displayMenu();
    }

    public static void importStorage() throws IOException {
        String src = System.getProperty("user.dir") + "/src/main/resources/";

/*        File dir = new File(src);
        File[] fileList = dir.listFiles();
        for(File file : fileList) {
            System.out.println(file.toString());
        }*/

        File accountCSV = new File(src + "user.csv");
        InputStream accountData = new FileInputStream(accountCSV);
        CSVParser parser = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(new InputStreamReader(new BOMInputStream(accountData), "UTF-8"));

//        Name,Password,Type,Blocked
        for (CSVRecord record : parser) {
            if (record.isSet("Name")) {
                if (!record.get("Name").isEmpty()) {
                    System.out.println(record.get("Name"));
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
