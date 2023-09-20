package com.ylireetta.tiralabraproject_rsa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FileHelper {
    private final KeyGenerator generator = new KeyGenerator();
    private final ArrayList<String> keyTypes = new ArrayList<>(Arrays.asList("public", "private"));
    
    /**
     * Create public and private keys for the user, and write the keys to their respective files.
     * @param username The user whose username will be used in the file names.
     */
    public void writeKeys(String username) {
        generator.generateKeys();

        String lowerCase = username.toLowerCase();
        for (UserKey key : generator.getKeys()) {
            writeToFile(key.getType(), lowerCase, key);
        }
    }
    
    /**
     * 
     * @param keyType The key type to use as part of the file names.
     * @param username The user whose username will be used in the file names.
     * @param key The key whose value will be written in the file.
     */
    private void writeToFile(String keyType, String username, UserKey key) {
        String filePath = "resources/keyfiles/" + keyType + "/";
        String fileName = username + "_" + keyType + "_key.txt";
        String completeName = filePath + fileName;
        
        if (createFile(completeName)) {
            try {
                FileWriter writer = new FileWriter(completeName);
                writer.write(key.toString());
                writer.close();
                System.out.println("Wrote " + keyType + " key to a file.");
            } catch (IOException e) {
                System.out.println("Failed to write " + keyType + " key to file.");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Create a file of the specified name.
     * @param fileName The name of the file to create.
     * @return True if creation was successful, false if unsuccessful or if the file already exists.
     */
    private boolean createFile(String fileName) {
        try {
            File file = new File(fileName);
            
            if (!file.createNewFile()) {
                System.out.println("File already exists.");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * Read the contents of both the public and private user key files.
     * @param username The user whose files should be read.
     */
    public void readFromFile(String username) {
        String lowerCase = username.toLowerCase();
        
        for (String keyType : keyTypes) {
            File userFile = retrieveUserFile(lowerCase, keyType);
        
            if (userFile != null) {
                try {
                    Scanner scanner = new Scanner(userFile);
                    while (scanner.hasNextLine()) {
                        System.out.println("Line from " + username + " key file: " + scanner.nextLine());
                    }   
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Could not find the " + keyType + " key file for user " + username + ".");
            }
        }
    }
    
    /**
     * Retrieve user key file of the specified publicity class.
     * @param username The user whose key file should be retrieved.
     * @param keyType The publicity class of the key.
     * @return The key file of the user if found, null otherwise.
     */
    private File retrieveUserFile(String username, String keyType) {
        File[] publicList = new File("resources/keyfiles/" + keyType).listFiles();
        for (File file : publicList) {
            // TODO: check the whole username part until the first underscore in the file name.
            if (file.getName().startsWith(username)) {
                return file;
            }
        }
        
        return null;
    }
    
}
