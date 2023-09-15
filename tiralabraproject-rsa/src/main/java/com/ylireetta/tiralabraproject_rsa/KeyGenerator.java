package com.ylireetta.tiralabraproject_rsa;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *
 * @author ylireett
 */
public class KeyGenerator {
    private PublicKey publicKey;
    private PrivateKey privateKey;
        
    public PublicKey getPublicKey() {
        return publicKey;
    }
    
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    
    public void setPublicKey(PublicKey key) {
        this.publicKey = key;
    }
    
    public void setPrivateKey(PrivateKey key) {
        this.privateKey = key;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .append("publicKey", publicKey)
            .append("privateKey", privateKey)
            .toString();
    }
    
    /***
     * Generates public and private keys.
     */
    public void generateKeys() {
        long p = PrimeHelper.generatePrime();
        long q = PrimeHelper.generatePrime();
        
        // Make sure p and q are not equal.
        while (p == q) {
            q = PrimeHelper.generatePrime();
        }
        
        long n = p * q;
        
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
        long euler = (p - 1) * (q - 1);
        long publicExponent = 65537;
        
        long greatestCommonDivisor = PrimeHelper.extendedEuclidean(publicExponent, euler)[0];
        
        // if gcd == 1, publicExponent (i.e., e) is coprime to euler and can be used.
        if (greatestCommonDivisor == 1) {
            // There is a bug somewhere here, the private exponent may become negative in some cases.
            long privateExponent = PrimeHelper.extendedEuclidean(publicExponent, euler)[1];
            
            // Now we have the private exponent of the private key (i.e., d).
            setPublicKey(new PublicKey(publicExponent, n));
            setPrivateKey(new PrivateKey(privateExponent, n));
        } else {
            // TODO: do something in case gcd was not 1.
        }
    }
}
