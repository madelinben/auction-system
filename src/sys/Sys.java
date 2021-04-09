package sys;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Sys {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        displayMenu();
    }

    public static void displayMenu() throws java.lang.Exception{
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
