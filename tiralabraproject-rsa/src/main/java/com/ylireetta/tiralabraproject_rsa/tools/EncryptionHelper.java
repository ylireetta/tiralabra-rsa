package com.ylireetta.tiralabraproject_rsa.tools;

import com.ylireetta.tiralabraproject_rsa.UserKey;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

public class EncryptionHelper {
    private FileHelper fileHelper;
    
    public EncryptionHelper(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }
    
    public BigInteger encryptMessage(String recipient, String message) throws IOException {
        // Okay: first check if a public key exists for the recipient.
        // If not, generate keys on the fly and use the public key to encrypt.
        // If a key already exists, retrieve it from the file.
        
        File publicKeyFile = fileHelper.retrieveUserFile(recipient, "public");
        
        if (publicKeyFile == null) {
            // Generate key(s?) for the recipient. Retrieve the public key file and use the contents.
            fileHelper.writeKeys(recipient);
            publicKeyFile = fileHelper.retrieveUserFile(recipient, "public");
        }
        
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
        
    }
}
