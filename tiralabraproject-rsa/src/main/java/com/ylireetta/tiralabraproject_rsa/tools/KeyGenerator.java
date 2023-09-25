package com.ylireetta.tiralabraproject_rsa.tools;

import com.ylireetta.tiralabraproject_rsa.PrivateKey;
import com.ylireetta.tiralabraproject_rsa.PublicKey;
import com.ylireetta.tiralabraproject_rsa.UserKey;
import java.math.BigInteger;
import java.util.ArrayList;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class KeyGenerator {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private ArrayList<UserKey> keys;
        
    public PublicKey getPublicKey() {
        return publicKey;
    }
    
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    
    public ArrayList<UserKey> getKeys() {
        return keys;
    }
    
    public void setPublicKey(PublicKey key) {
        this.publicKey = key;
        this.keys.add(key); // TODO: make sure that there is only one public key in the list.
    }
    
    public void setPrivateKey(PrivateKey key) {
        this.privateKey = key;
        this.keys.add(key); // TODO: make sure that there is only one private key in the list.
    }
    
    /**
     * Overwrite previous key list so that new user keys can be created.
     */
    public void resetKeyList() {
        this.keys = new ArrayList();
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .append("publicKey", publicKey)
            .append("privateKey", privateKey)
            .toString();
    }
    
    /**
     * Generate public and private keys.
     */
    public void generateKeys() {
        resetKeyList();
        
        BigInteger p = PrimeHelper.generatePrime();
        BigInteger q = PrimeHelper.generatePrime();
        
        // Make sure p and q are not equal.
        while (p.equals(q)) {
            q = PrimeHelper.generatePrime();
        }
        
        BigInteger n = p.multiply(q);
        
        /* Choose a number e:
        e < n
        n is relatively prime to (p - 1) * (q - 1)
        e and (p - 1) * (q - 1) have no common factor except 1 (this gets tested when the extended euclidean algorithm is run)
        φ(n) (Euler's totient function) as φ(n) = (p - 1) * (q - 1)
        So e needs to be smaller than φ(n) and bigger than 1
        e is coprime to φ(n)
        The most common choice for 'e' is 65537.
        */
        
        // Euler's totient function
        BigInteger euler = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger publicExponent = new BigInteger("65537");
        
        BigInteger[] extendedEuclideanResult = PrimeHelper.extendedEuclidean(publicExponent, euler);
        BigInteger greatestCommonDivisor = extendedEuclideanResult[0];
        
        // if gcd == 1, publicExponent (i.e., e) is coprime to euler and can be used.
        if (greatestCommonDivisor.equals(BigInteger.ONE)) {
            BigInteger privateExponent = extendedEuclideanResult[1];
            
            // Private exponent cannot be negative. Add phi(n), i.e. Euler's totient function, and make sure d > 0.
            // https://stackoverflow.com/a/21007972
            if (privateExponent.compareTo(BigInteger.ZERO) < 0) {
                privateExponent = privateExponent.add(euler);
            }
            
            // Now we have the private exponent of the private key (i.e., d).
            setPublicKey(new PublicKey(publicExponent, n));
            setPrivateKey(new PrivateKey(privateExponent, n));
        } else {
            // TODO: do something in case gcd was not 1.
        }
    }
}
