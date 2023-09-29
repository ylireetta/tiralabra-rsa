package com.ylireetta.tiralabraproject_rsa.tools;

import com.ylireetta.tiralabraproject_rsa.UserKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

public class DecryptionHelper {
    private final FileHelper fileHelper;
    
    public DecryptionHelper(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }
    
    /**
     * 
     * @param username
     * @param message
     * @return
     * @throws IOException 
     */
    public String decryptMessage(String username, String message) throws IOException {        
        File privateKeyFile = fileHelper.retrieveUserFile(username, "private");
        
        if (privateKeyFile != null) {
            // The message is still a String. Convert to BigInteger.
            BigInteger encryptedMessage = new BigInteger(message);
            
            try {
                UserKey privateKey = fileHelper.getKeyFromFile(privateKeyFile, "private");
                BigInteger d = privateKey.getExponent();
                BigInteger n = privateKey.getModulus();
                
                BigInteger decryptedMessageNumeric = PrimeHelper.modularExponentiation(encryptedMessage, d, n);
                
                byte[] decryptedBytes = decryptedMessageNumeric.toByteArray();
                String decryptedMessage = new String(decryptedBytes);
                return decryptedMessage;
            } catch (IOException e) {
                throw e;
            }
        } else {
            // The private key was not found, there's nothing we can do now. We have handled this situation in the UI already, but just in case.
            throw new FileNotFoundException("No private key file for user " + username + " was found. Cannot decrypt message.");
        }
    }
    
}
