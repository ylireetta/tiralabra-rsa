package com.ylireetta.tiralabraproject_rsa;

import com.ylireetta.tiralabraproject_rsa.tools.DecryptionHelper;
import com.ylireetta.tiralabraproject_rsa.tools.EncryptionHelper;
import com.ylireetta.tiralabraproject_rsa.tools.FileHelper;
import com.ylireetta.tiralabraproject_rsa.tools.KeyGenerator;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
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
     * @param scanner The scanner used to read user input in the UserInterface class.
     */
    private void writeUserKeys(Scanner scanner) {
        String username = validateUsername(scanner, true);
        
        if (username != null) {
            if (fileHelper.usernameTaken(username)) {
                System.out.println("Username already taken.");
            } else {
                List<UserKey> keys = generateKeys();
                writeKeysToFile(username, keys, true);
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
                List<UserKey> userKeys = fileHelper.collectKeys(username);
                for (UserKey key : userKeys) {
                    System.out.println("Found " + key.getType() + " key, and it looks like this:\nexponent: " + key.getExponent() + "\nmodulus: " + key.getModulus() + "\n");
                }
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
     * @return The given username in lowercase if valid, null otherwise. 
     */
    private String validateUsername(Scanner scanner, boolean ownName) {
        if (ownName) {
            System.out.println("Please give your username:");
        } else {
            System.out.println("Please give the username of the recipient:");
        }
        String username = scanner.nextLine();
        System.out.println("");
        
        if (!username.isEmpty() && !username.contains(".")) {
            return username.toLowerCase();
        } else {
            System.out.println("Username cannot be empty or contain dots (.).");
            return null;
        }
    }
    
    /**
     * Encrypt or decrypt a message.
     * @param scanner The scanner used to read user input in the UserInterface class.
     * @param encrypt True if the user wants to encrypt a message, false if they want to decrypt.
     */
    private void handleMessage(Scanner scanner, boolean encrypt) {
        // Give the boolean as the opposite. If we are encrypting a message, we need to ask for the recipient name, not the user's own name.
        String username = validateUsername(scanner, !encrypt);
        
        if (username != null && keyFileExists(username, encrypt)) {
            String message = getMessage(scanner, encrypt);
            if (message != null && encrypt) {
                handleEncryption(message, username);
            } else if (message != null && !encrypt) {
                handleDecryption(message, username);
            }
        }
    }
    
    /**
     * Ask for the message to encrypt or decrypt.
     * @param scanner The scanner used to read user input in the UserInterface class.
     * @param encrypt Whether the message should be encrypted or decrypted.
     * @return The message from the user if not empty and of correct format, null otherwise. 
     */
    private String getMessage(Scanner scanner, boolean encrypt) {
        System.out.println("Please write your message here:");
        
        String message = scanner.nextLine();
        System.out.println("");
        
        // Special case when the message should be decrypted in the next stage.
        if (!encrypt && !message.matches("[0-9]+")) {
            System.out.println("Message to decrypt can only contain numbers.");
            return null;
        }
        
        // Other cases.
        if (!message.isEmpty()) {
            return message;
        } else {
            System.out.println("Message cannot be empty.");
        }
        return null;
    }
    
    /**
     * Check whether key files exist for the given user.
     * If not and the user wants to encrypt, create files on the fly. If no files exist and the user wants to decrypt, inform them and return.
     * @param username The user whose key files need to be checked.
     * @param encrypt Whether the situation calls for encrypting or decrypting a message.
     * @return True if files were found to start with or successfully created in the process. False if the user wants to decrypt a message and no key files are found.
     */
    private boolean keyFileExists(String username, boolean encrypt) {
        if (encrypt && !fileHelper.usernameTaken(username)) {
            // Create key files if none exist for the current user. This should only be done when encrypting.
            List<UserKey> keys = generateKeys();
            writeKeysToFile(username, keys, false);
            return true;
        }
        
        // If no files are found when decryption should be done, there's nothing we can do.
        if (!encrypt && !fileHelper.usernameTaken(username)) {
            System.out.println("No key files found for user " + username + ". Cannot decrypt message.");
            return false;
        }
        
        return true;
    }
    
    /**
     * Encrypt a message for a specific recipient using their public key.
     * @param message The message to encrypt.
     * @param username The user whose public key should be used.
     */
    private void handleEncryption(String message, String username) {
        try {
            BigInteger encryptedMessage = encryptionHelper.encryptMessage(username, message);
            System.out.println("Encrypted message " + message + ": result is\n" + encryptedMessage);
            copyMessageToClipboard(encryptedMessage.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Decrypt a message using the private key of the user.
     * @param message The message to decrypt.
     * @param username The user whose private key should be used.
     */
    private void handleDecryption(String message, String username) {
        try {
            String decryptedMessage = decryptionHelper.decryptMessage(username, message);
            System.out.println("Decryption result:\n" + decryptedMessage);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Copy the message to clipboard so that the user doesn't have to do that themselves.
     * @param message The message to copy.
     */
    private void copyMessageToClipboard(String message) {
        StringSelection selection = new StringSelection(message);
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        clip.setContents(selection, selection);
        
        System.out.println("\nThe message has been copied to your clipboard for convenience.");
    }
    
    /**
     * Generate new public and private keys.
     * @return A list that contains the new UserKeys.
     */
    private List<UserKey> generateKeys() {
        System.out.println("Generating user keys...");
        return KeyGenerator.generateKeys();
    }
    
    /**
     * Write the generated UserKeys to new files.
     * @param username The username to use when creating the file name.
     * @param keys The list with the UserKeys that should be written to files.
     */
    private void writeKeysToFile(String username, List<UserKey> keys, boolean printSuccess) {
        boolean failed = false;
        
        for (UserKey key : keys) {
            try {
                if (!fileHelper.writeToFile(username, key)) {
                    System.out.println("Could not write user keys to file.");
                    failed = true;
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        
        if (!failed && printSuccess) {
            System.out.println("Wrote user keys to file successfully!");
            System.out.println("The key files can be found under the subdirectories of " + fileHelper.getBaseDirectory() + ".\n");
        }
    }
}
