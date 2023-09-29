package com.ylireetta.tiralabraproject_rsa.tools;

import com.ylireetta.tiralabraproject_rsa.UserKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

public class EncryptionHelper {
    private final FileHelper fileHelper;
    
    public EncryptionHelper(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }
    
    /**
     * Encrypt the given string using the public key of the specified recipient user.
     * @param recipient The user whose public key should be used for encryption.
     * @param message The message that should be encrypted.
     * @return The encrypted message as BigInteger.
     * @throws IOException 
     */
    public BigInteger encryptMessage(String recipient, String message) throws IOException {
        File publicKeyFile = fileHelper.retrieveUserFile(recipient, "public");
        
        // Public key file should not be null because we generated missing keys on the UI side.
        if (publicKeyFile != null) {
            try {
                UserKey publicKey = fileHelper.getKeyFromFile(publicKeyFile, "public");
                byte[] messageBytes = message.getBytes();
                BigInteger messageNumeric = new BigInteger(1, messageBytes);

                BigInteger e = publicKey.getExponent();
                BigInteger n = publicKey.getModulus();

                BigInteger encryptedMessage = PrimeHelper.modularExponentiation(messageNumeric, e, n);
                return encryptedMessage;

            } catch (IOException e) {
                throw e;
            }
        } else {
            // The public key was not found. We should never end up here since files were created in the UI if they were missing to begin with, but just in case.
            throw new FileNotFoundException("No public key file for user " + recipient + " was found. Cannot encrypt message.");
        }
        
    }
}
