package com.ylireetta.tiralabraproject_rsa.tools;

import com.ylireetta.tiralabraproject_rsa.pojo.PrivateKey;
import com.ylireetta.tiralabraproject_rsa.pojo.PublicKey;
import com.ylireetta.tiralabraproject_rsa.interfaces.UserKey;
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

public class FileHelper {
    private final ArrayList<String> keyTypes = new ArrayList<>(Arrays.asList("public", "private"));
    private String baseDirectory;
    
    /**
     * Constructor for basic usage. Key files get generated in the resources directory. 
     */
    public FileHelper() {
        createDirectoryStructure();
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
     * Create the necessary directories to enable file creation in the later stages.
     */
    private void createDirectoryStructure() {
        String currentDir = System.getProperty("user.dir");
        String basePath = currentDir + File.separator + "resources/keyfiles/";
        
        for (String keyType : keyTypes) {
            String fullPath = basePath + File.separator + keyType;

            // Create directory structure for later use.
            File directory = new File(fullPath);
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    this.baseDirectory = fullPath + "/";
                }
            }
        }
        
        this.baseDirectory = basePath;
    }
    
    /**
     * Create a new user key file and write a user key into it.
     * @param username The user whose username will be used in the file names.
     * @param key The key whose value will be written in the file.
     * @return True if file creation and file write was successful, false otherwise.
     * @throws IOException 
     */
    public boolean writeToFile(String username, UserKey key) throws IOException {
        String keyType = key.getType();
        
        String filePath = getBaseDirectory() + keyType + "/";
        //String fileName = username + "_" + keyType + "_key.txt";
        String fileName = username + ".txt";
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
     * Collect user keys.
     * @param username The user whose keys should be retrieved.
     * @return A list with user key(s) if at least one was found.
     * @throws java.io.FileNotFoundException If neither public nor private key files can be found for the specified user.
     * @throws IOException 
     */
    public List<UserKey> collectKeys(String username) throws FileNotFoundException, IOException {
        int missingFiles = 0;
        List<UserKey> result = new ArrayList<>();
        
        for (String keyType : keyTypes) {
            File userFile = retrieveUserFile(username, keyType);
            
            if (userFile != null) {
                UserKey key = getKeyFromFile(userFile, keyType);
                result.add(key);
            } else {
                missingFiles++;
            }
        }
        
        // Throw exception if no files were found.
        if (missingFiles == 2) {
            throw new FileNotFoundException("Key files missing for user " + username + ".");
        }
        
        return result;
    }
    
    /**
     * Retrieve user key file of the specified publicity class.
     * @param username The user whose key file should be retrieved.
     * @param keyType The publicity class of the key.
     * @return The key file of the user if found, null otherwise.
     */
    public File retrieveUserFile(String username, String keyType) {
        File[] fileList = new File(getBaseDirectory() + "/" + keyType).listFiles();
        
        int userFileIndex = binarySearch(fileList, username);
        if (userFileIndex > -1) {
            return fileList[userFileIndex];
        }
        
        return null;
    }
    
    /**
     * Check if a key file for the given username already exists.
     * @param username The username to check.
     * @return True if a public or a private key file already exists with the given username, false otherwise.
     */
    public boolean usernameTaken(String username) {
        File[] publicList = new File(getBaseDirectory() + "/public").listFiles();
        File[] privateList = new File(getBaseDirectory() + "/private").listFiles();
                
        // It should never happen that one of the key files does not exist, but it's not impossible. Check both directories.       
        return binarySearch(publicList, username) > -1 || binarySearch(privateList, username) > -1;
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
     * Use binary search to look for the key file of a specific user in a list of files.
     * @param fileList The collection of files to search through.
     * @param username The username of the user whose file to look for.
     * @return The index of the file in the list if found, -1 otherwise.
     */
    public int binarySearch(File[] fileList, String username) {
        Arrays.sort(fileList);
        int low = 0, middle = 0;
        int high = fileList.length - 1;
                        
        while (low <= high) {
            middle = low + ((high - low) / 2);
            String userFileName = fileList[middle].getName();
            String nameStart = getNameStart(userFileName);
            
            // Found a file name that matches the username.
            if (nameStart.equals(username)) {
                return middle;
            } else {
                // Fiddle around with the indexes and continue the loop.
                if (nameStart.compareTo(username) < 0) {
                    // The file name we are looking for is located to the right of our mid value.
                    low = middle + 1;
                } else if (nameStart.compareTo(username) > 0) {
                    // The file name we are looking for is located to the left of our mid value.
                    high = middle - 1;
                }
            }
        }
        
        // The file was not found.
        return -1;
    }
    
    /**
     * Get the beginning of the file name if it contains dots.
     * @param fileName The complete file name to check.
     * @return The beginning of the file name up until the first dot. If no dots are found, return the original file name.
     */
    public String getNameStart(String fileName) {
        int dotIndex = fileName.indexOf(".");

        if (dotIndex != -1) {
            return fileName.substring(0, dotIndex);
        } else {
            return fileName;
        }
    }
}
