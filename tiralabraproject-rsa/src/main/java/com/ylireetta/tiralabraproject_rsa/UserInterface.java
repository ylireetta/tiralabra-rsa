package com.ylireetta.tiralabraproject_rsa;

import com.ylireetta.tiralabraproject_rsa.tools.DecryptionHelper;
import com.ylireetta.tiralabraproject_rsa.tools.EncryptionHelper;
import com.ylireetta.tiralabraproject_rsa.tools.FileHelper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Scanner;

public class UserInterface {
    private final FileHelper fileHelper = new FileHelper();
    private final EncryptionHelper encryptionHelper = new EncryptionHelper(fileHelper);
    private final DecryptionHelper decryptionHelper = new DecryptionHelper(fileHelper);
    
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
            System.out.println("    3: encrypt a message");
            System.out.println("    4: decrypt a message");
            System.out.println("    x: end program");
            
            String command = scanner.nextLine();
            System.out.println("");
            
            if (command.equals("1")) {
                writeUserKeys(scanner);
            } else if (command.equals("2")) {
                readFromUserFile(scanner);
            } else if (command.equals("3")) {
                handleMessage(scanner, true);
            } else if (command.equals("4")) {
                handleMessage(scanner, false);
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
        String username = validateUsername(scanner, true);
        
        if (fileHelper.usernameTaken(username)) {
            System.out.println("Username already taken.");
        } else if (username != null) {
            try {
                fileHelper.writeKeys(username);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    /**
     * Read the contents of the specified user's key files, if found.
     * @param scanner The scanner used to read user input in the UserInterface class
     */
    private void readFromUserFile(Scanner scanner) {
        String username = validateUsername(scanner, true);
        
        if (username != null) {
            try {
                fileHelper.readFromFile(username);
            } catch (FileNotFoundException e) {
                // This exception is thrown if both public and private key files are missing.
                System.out.println(e.getMessage());
            } catch (IOException e) {
                // This exception is thrown is something goes wrong when retrieving keys from the file contents.
                System.out.println(e.getMessage());
            }
        }
    }
    
    /**
     * Ask for and validate username.
     * @param scanner The scanner used to read user input in the UserInterface class.
     * @param ownName True if the user should provide their own username, false if the username belongs to the message recipient.
     * @return The given username if valid, null otherwise. 
     */
    private String validateUsername(Scanner scanner, boolean ownName) {
        if (ownName) {
            System.out.println("Please give your username:");
        } else {
            System.out.println("Please give the username of the recipient:");
        }
        String username = scanner.nextLine();
        System.out.println("");
        
        if (!username.isEmpty() && !username.contains("_")) {
            return username;
        } else {
            System.out.println("Username cannot be empty or contain underscores (_).");
            return null;
        }
    }
    
    /**
     * Encrypt a message for a specific recipient using their public key.
     * @param scanner The scanner used to read user input in the UserInterface class.
     */
    private void handleMessage(Scanner scanner, boolean encrypt) {
        // There is a bug in here somewhere, or perhaps it's in the key generation. Keys for reetta and iuiu work, but new keys give utter gibberish when decrypting message.
        
        // Give the boolean as the opposite. If we are encrypting a message, we need to ask for the recipient name, not the user's own name.
        String username = validateUsername(scanner, !encrypt);
        
        if (username != null) {
            String message = getMessage(scanner);
            if (message != null && encrypt) {
                try {
                    BigInteger encryptedMessage = encryptionHelper.encryptMessage(username, message);
                    System.out.println("Encrypted message " + message + ": result is\n\n" + encryptedMessage);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if (message != null && !encrypt) {
                try {
                    String decryptedMessage = decryptionHelper.decryptMessage(username, message);
                    System.out.println("Decryption result: " + decryptedMessage);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            // Something was wrong with the recipient username.
        }
    }
    
    /**
     * Ask for the message to encrypt or decrypt.
     * @param scanner The scanner used to read user input in the UserInterface class.
     * @return The message from the user if not empty, null otherwise.
     */
    private String getMessage(Scanner scanner) {
        System.out.println("Please write your message here:");
        
        String message = scanner.nextLine();
        System.out.println("");
        
        if (!message.isEmpty()) {
            return message;
        } else {
            System.out.println("Message cannot be empty.");
        }
        return null;
    }
}
