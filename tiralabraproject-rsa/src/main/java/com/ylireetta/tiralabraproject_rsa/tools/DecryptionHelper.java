package com.ylireetta.tiralabraproject_rsa.tools;

import com.ylireetta.tiralabraproject_rsa.interfaces.UserKey;
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
     * Decrypt the given string using the private key of the specified user.
     * @param username The user whose private key should be used for decryption.
     * @param message The message to decrypt.
     * @return The decrypted message.
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
            // The private key was not found, there's nothing we can do now.
            throw new FileNotFoundException("No private key file for user " + username + " was found. Cannot decrypt message.");
        }
    }
    
}
