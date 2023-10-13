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
    
    /**
     * Set the private variable PublicKey and add the key to the list of UserKey objects.
     * @param key The key to set.
     */
    public void setPublicKey(PublicKey key) {
        this.publicKey = key;
        this.keys.add(key);
    }
    
    /**
     * Set the private variable PrivateKey and add the key to the list of UserKey objects.
     * @param key The key to set.
     */
    public void setPrivateKey(PrivateKey key) {
        this.privateKey = key;
        this.keys.add(key);
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
        
        PublicKey publicKey = createPublicKey(n);
        setPublicKey(publicKey);
        
        BigInteger publicExponent = publicKey.getExponent();
        PrivateKey privateKey = createPrivateKey(p, q, publicExponent, n);
        
        if (privateKey != null) {
            setPrivateKey(privateKey);
        }
    }
    
    /**
     * Generate a public key to use for encryption.
     * @param n The result of multiplying p by q.
     * @return A new PublicKey.
     */
    public PublicKey createPublicKey(BigInteger n) {
        /* Choose a number e:
        e and (p - 1) * (q - 1) have no common factor except 1 (this gets tested when the extended euclidean algorithm is run). i.e., e is coprime to φ(n)
        The most common choice for 'e' is 65537.
        */
        BigInteger publicExponent = new BigInteger("65537");
        return new PublicKey(publicExponent, n);
    }
    
    /**
     * Generate a private key to use for decryption.
     * @param p (Probable) prime number.
     * @param q (Probable) prime number.
     * @param publicExponent Public exponent from the public key.
     * @param n The result of multiplying p by q.
     * @return A new PrivateKey if the greatest common divisor of the public exponent and Euler's totient function is equal to one, null otherwise.
     */
    public PrivateKey createPrivateKey(BigInteger p, BigInteger q, BigInteger publicExponent, BigInteger n) {
        // φ(n) (Euler's totient function) as φ(n) = (p - 1) * (q - 1)
        BigInteger euler = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
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
            return new PrivateKey(privateExponent, n);
        } else {
            // gcd was not 1, return null.
            return null;
        }
    }
}
