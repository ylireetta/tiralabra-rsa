package com.ylireetta.tiralabraproject_rsa;

/**
 *
 * @author ylireett
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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
        e and (p - 1) * (q - 1) have no common factor except 1 (I need an algorithm for this?)
        φ(n) (Euler's totient function) as φ(n) = (p - 1) * (q - 1)
        So e needs to be smaller than φ(n) and bigger than 1
        e is coprime to φ(n)
        */
        
        /*
        The most common choice for 'e' is 65537, often represented as 0x10001 in hexadecimal. This choice is based on its efficiency in encryption and its security properties. It's a prime number and has a simple binary representation, making encryption and exponentiation operations faster.
        */
        
        // Euler's totient function
        long euler = (p - 1) * (q - 1);
        long publicExponent = 65537;
        
        long greatestCommonDivisor = PrimeHelper.extendedEuclidean(publicExponent, euler)[0];
        long privateExponent = 0;
        
        // publicExponent (i.e., e) is coprime to euler and can be used.
        if (greatestCommonDivisor == 1) {
            // There is a bug somewhere, the private exponent may become negative in some cases.
            privateExponent = PrimeHelper.extendedEuclidean(publicExponent, euler)[1];
                        
            // Now we have the private exponent of the private key (i.e., d).
        }
        
        // the public key is now (e, n)
        long[] publicKey = new long[]{publicExponent, n};
        // the private key is now (d, n)
        long[] privateKey = new long[]{privateExponent, n};
        
        System.out.println("Public key is " + publicKey[0] + "," + publicKey[1]);
        System.out.println("Private key is " + privateKey[0] + "," + privateKey[1]);
        
        
    }
}
