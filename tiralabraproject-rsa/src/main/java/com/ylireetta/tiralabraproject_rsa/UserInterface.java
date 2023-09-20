package com.ylireetta.tiralabraproject_rsa;

import java.util.Scanner;

public class UserInterface {
    private final FileHelper fileHelper = new FileHelper();
    
    /**
     * Start reading user input and executing commands based on it.
     */
    public void start() {
        System.out.println("Welcome!");
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("");
            System.out.println("Please choose from the following commands:");
            System.out.println("    1: generate public and private keys");
            System.out.println("    2: read public and private keys");
            System.out.println("    x: end program");
            
            String command = scanner.nextLine();
            System.out.println("");
            
            if (command.equals("1")) {
                writeUserKeys(scanner);
            } else if (command.equals("2")) {
                readFromUserFile(scanner);
            } else if (command.toLowerCase().equals("x")) {
                System.out.println("Thank you for visiting!");
                scanner.close();
                break;
            }
            
            System.out.println(".+*+.+*+.");
        }
    }
    
    /**
     * Generate new keys for the specified user and write them to files.
     * @param scanner  The scanner used to read user input in the UserInterface class.
     */
    private void writeUserKeys(Scanner scanner) {
        String username = validateUsername(scanner);
        
        if (username != null) {
            fileHelper.writeKeys(username);
        } else {
            // TODO: ask for another name
        }
        
    }
    
    /**
     * Read the contents of the specified user's key files, if found.
     * @param scanner The scanner used to read user input in the UserInterface class.
     */
    private void readFromUserFile(Scanner scanner) {
        String username = validateUsername(scanner);
        
        if (username != null) {
            fileHelper.readFromFile(username);
        } else {
            // TODO: ask for another name.
        }
    }
    
    /**
     * Ask for and validate username.
     * @param scanner The scanner used to read user input in the UserInterface class.
     * @return The given username if valid, null otherwise.
     */
    private String validateUsername(Scanner scanner) {
        System.out.println("Please give your username:");
        String username = scanner.nextLine();
        System.out.println("");
        
        if (!username.isEmpty() && !username.contains("_")) {
            return username;
        } else {
            System.out.println("Username cannot be empty or contain underscores (_).");
            return null;
        }
    }
}
