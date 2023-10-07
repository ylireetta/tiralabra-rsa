package com.ylireetta.tiralabraproject_rsa.tools;

import com.ylireetta.tiralabraproject_rsa.PrivateKey;
import com.ylireetta.tiralabraproject_rsa.PublicKey;
import com.ylireetta.tiralabraproject_rsa.UserKey;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class FileHelper {
    private final KeyGenerator generator = new KeyGenerator();
    private final ArrayList<String> keyTypes = new ArrayList<>(Arrays.asList("public", "private"));
    private String baseDirectory;
    
    /**
     * Constructor for basic usage. Key files get generated in the resources directory. 
     */
    public FileHelper() {
        this.baseDirectory = "resources/keyfiles/";
    }
    
    /**
     * Constructor for test usage. Key files get generated in the supplied directory (i.e. some tempDir).
     * @param baseDirectory The base directory where the key files should be generated.
     */
    public FileHelper(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }
    
    public String getBaseDirectory() {
        return baseDirectory;
    }
    
    /**
     * Create public and private keys for the user, and write the keys to their respective files.
     * @param username The user whose username will be used in the file names.
     * @return True if both the public and private key files were created successfully, false otherwise.
     * @throws java.io.IOException 
     */
    public boolean writeKeys(String username) throws IOException {
        generator.generateKeys();

        String lowerCase = username.toLowerCase();
        int successfulWrites = 0;
        
        for (UserKey key : generator.getKeys()) {
            if (writeToFile(key.getType(), lowerCase, key)) {
                successfulWrites++;
            }
        }
        
        // Return true if both file writes were successful.
        return successfulWrites == 2;
    }
    
    /**
     * 
     * @param keyType The key type to use as part of the file names.
     * @param username The user whose username will be used in the file names.
     * @param key The key whose value will be written in the file.
     * @return True if file creation and file write was successful, false otherwise.
     * @throws IOException 
     */
    public boolean writeToFile(String keyType, String username, UserKey key) throws IOException {
        String filePath = getBaseDirectory() + keyType + "/";
        String fileName = username + "_" + keyType + "_key.txt";
        String completeName = filePath + fileName;
        
        if (createFile(completeName)) {
            try {
                FileWriter writer = new FileWriter(completeName);
                writer.write(key.toString());
                writer.close();
                return true;
            } catch (IOException e) {
                throw e;
            }
        } else {
            return false;
        }
    }
    
    /**
     * Create a file of the specified name.
     * @param fileName The name of the file to create.
     * @return True if creation was successful, false if unsuccessful or if the file already exists.
     */
    public boolean createFile(String fileName) {
        try {
            File file = new File(fileName);
            
            if (!file.createNewFile()) {
                // File already exists, won't create a new one.
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    
    /**
     * Read the contents of both the public and private user key files.
     * @param username The user whose files should be read.
     * @throws java.io.FileNotFoundException If neither public nor private key files can be found for the specified user.
     * @throws IOException 
     */
    public void readFromFile(String username) throws FileNotFoundException, IOException {
        String lowerCase = username.toLowerCase();
        int missingFiles = 0;
        
        for (String keyType : keyTypes) {
            File userFile = retrieveUserFile(lowerCase, keyType);
            
            if (userFile != null) {
                UserKey key = getKeyFromFile(userFile, keyType);
                System.out.println("Found " + key.getType() + " key, and it looks like this:\nexponent: " + key.getExponent() + "\nmodulus: " + key.getModulus());
            } else {
                missingFiles++;
            }
        }
        
        // Throw exception if no files were found.
        if (missingFiles == 2) {
            throw new FileNotFoundException("Key files missing for user " + username + ".");
        }
    }
    
    /**
     * Retrieve user key file of the specified publicity class.
     * @param username The user whose key file should be retrieved.
     * @param keyType The publicity class of the key.
     * @return The key file of the user if found, null otherwise.
     */
    public File retrieveUserFile(String username, String keyType) {
        File[] fileList = new File(getBaseDirectory() + "/" + keyType).listFiles();
        // Match the first part of the file name, up until the first underscore.
        String regex = "^" + Pattern.quote(username) + "_.*$";
        Pattern pattern = Pattern.compile(regex);
        
        for (File file : fileList) {
            if (pattern.matcher(file.getName()).matches()) {
                return file;
            }
        }
        
        return null;
    }
    
    /**
     * Check if a key file for the given username already exists.
     * @param username The username to check.
     * @return True if a public or a private key file already exists with the given username, false otherwise.
     */
    public boolean usernameTaken(String username) {
        String regex = "^" + Pattern.quote(username) + "_.*$";
        Pattern pattern = Pattern.compile(regex);
        
        String[] publicList = new File(getBaseDirectory() + "public").list();
        String[] privateList = new File(getBaseDirectory() + "private").list();
                
        List<String> allFileNames = new ArrayList<>(Arrays.asList(publicList));
        allFileNames.addAll(Arrays.asList(privateList));
        allFileNames.sort(String::compareToIgnoreCase);

        for (String name : allFileNames) {
            if (pattern.matcher(name).matches()) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Read the specified type of user key from the file contents.
     * @param userFile The file which needs to be read.
     * @param keyType Specifies if the key returned should be PublicKey or PrivateKey.
     * @return UserKey of the specified publicity class if the file content is as expected, null otherwise.
     * @throws IOException If something goes wrong when reading the file. 
     */
    public UserKey getKeyFromFile(File userFile, String keyType) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] elements = line.split(",");
                
                if (elements.length == 2) {
                    BigInteger exponent = new BigInteger(elements[0]);
                    BigInteger n = new BigInteger(elements[1]);
                    
                    switch (keyType) {
                        case "public":
                            return new PublicKey(exponent, n);
                        case "private":
                            return new PrivateKey(exponent, n);
                        default:
                            throw new IllegalArgumentException("Invalid key type.");
                    }
                } else {
                    throw new IllegalArgumentException("Expected two elements in the file, found " + elements.length + ".");
                }
            }
        } catch (IOException e) {
            throw e;
        }
        
        return null;
    }
    
    /**
     * 
     * @param fileList
     * @param username
     * @param keyType
     * @return 
     */
    public int binarySearch(File[] fileList, String username, String keyType) {
        // Match the first part of the file name, up until the first underscore.
        String regex = "^" + Pattern.quote(username) + "_.*$";
        Pattern pattern = Pattern.compile(regex);
        
        int low = 0, middle = 0;
        int high = fileList.length - 1;
                
        while (low <= high) {
            middle = low + ((high - low) / 2);
            String userFileName = fileList[middle].getName();
            
            // Found a file name that matches the username.
            if (pattern.matcher(userFileName).matches()) {
                return middle;
            } else {
                // Fiddle around with the indexes and continue the loop.
                if (userFileName.compareTo(username) < 0) {
                    // The file name we are looking for is located to the right of our mid value.
                    low = middle + 1;
                } else if (userFileName.compareTo(username) > 0) {
                    // The file name we are looking for is located to the left of our mid value.
                    high = middle - 1;
                }
            }
        }
        
        // The file was not found.
        return -1;
    }
}
