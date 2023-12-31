package com.ylireetta.tiralabraproject_rsa.tools;

import com.ylireetta.tiralabraproject_rsa.pojo.PrivateKey;
import com.ylireetta.tiralabraproject_rsa.pojo.PublicKey;
import com.ylireetta.tiralabraproject_rsa.interfaces.UserKey;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class KeyGenerator {
    /**
     * Generate public and private keys.
     * @return A list with public and private user keys.
     */
    public static List<UserKey> generateKeys() {
        ArrayList<UserKey> result = new ArrayList<>();
        BigInteger p = PrimeHelper.generatePrime();
        BigInteger q = PrimeHelper.generatePrime();
        
        // Make sure p and q are not equal.
        while (p.equals(q)) {
            q = PrimeHelper.generatePrime();
        }
        
        BigInteger n = p.multiply(q);
        
        PublicKey publicKey = createPublicKey(n);
        result.add(publicKey);
        
        BigInteger publicExponent = publicKey.getExponent();
        PrivateKey privateKey = createPrivateKey(p, q, publicExponent, n);
        
        if (privateKey != null) {
            result.add(privateKey);
        }
        
        return result;
    }
    
    /**
     * Generate a public key to use for encryption.
     * @param n The result of multiplying p by q.
     * @return A new PublicKey.
     */
    public static PublicKey createPublicKey(BigInteger n) {
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
    public static PrivateKey createPrivateKey(BigInteger p, BigInteger q, BigInteger publicExponent, BigInteger n) {
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
