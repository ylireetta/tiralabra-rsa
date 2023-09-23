package com.ylireetta.tiralabraproject_rsa.tools;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PrimeHelper {
    public static final List<BigInteger> SMALLPRIMES = sieveOfEratosthenes();
    
    /**
     * Generate a prime number.
     * @return A prime number.
     */
    public static BigInteger generatePrime() {
        BigInteger primeCandidate = new BigInteger(1000, new Random());
        
        while (!isPrime(primeCandidate)) {
            primeCandidate = new BigInteger(1000, new Random());
        }
        return primeCandidate;
    }
    
    /**
     * Miller-Rabin primality test.
     * @param n The integer to test for primality.
     * @return Boolean indicating whether n is a likely prime or not.
     */
    public static boolean isPrime(BigInteger n) {
        // If n is less than two or even, it cannot be prime.
        // When comparing BigInteger x to y, the result is -1 if x < y.
        if (n.compareTo(BigInteger.TWO) == -1 || n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return false;
        }
        
        if (divisibleBySmallPrime(n)) {
            return false;
        }
        
        // Write n-1 as 2^r*d
        BigInteger[] resultArray = factorizePowerTwo(n);
        BigInteger d = resultArray[1];
        
        // Choice of k: a larger value provides more accuracy, but requires more calculation effort.
        BigInteger[] randomWitnesses = randomWitnesses(n, 40);
        
        for (int i = 0; i < randomWitnesses.length; i++) {
            // If this test passes for each a in random witnesses, n is likely prime.
            // If it fails, n is composite and of no longer interest to us.
            if (!millerRabinTest(randomWitnesses[i], d, n)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Write n - 1 as 2^r * d
     * @param n The prime candidate under test.
     * @return An array with values r and d.
     */
    private static BigInteger[] factorizePowerTwo(BigInteger n) {
        BigInteger r = BigInteger.ZERO;
        BigInteger d = n.subtract(BigInteger.ONE);
        
        // remainder returns the remainder when d is divided by 2, i.e. d % 2
        while (d.remainder(BigInteger.TWO).equals(BigInteger.ZERO)) {
            r.add(BigInteger.ONE);
            d = d.divide(BigInteger.TWO);
        }
        
        // At this point, 'r' is the largest power of 2 such that 2^r divides 'n - 1', and 'd' is the remainder.
        return new BigInteger[]{r, d};
    }
    
    /**
     * 
     * @param n Number to test.
     * @param k Number of random witnesses to generate.
     * @return An array with k random witnesses.
     */
    private static BigInteger[] randomWitnesses(BigInteger n, int k) {
        BigInteger[] witnesses = new BigInteger[k];
        
        for (int i = 0; i < k; i++) {
            BigInteger a = BigInteger.ZERO;
            
            // Calculate the greatest common divisor.
            while (!extendedEuclidean(a, n)[0].equals(BigInteger.ONE)) {
                BigInteger min = BigInteger.TWO;
                BigInteger max = n.subtract(BigInteger.ONE);
                BigInteger randRange = max.subtract(min).add(BigInteger.ONE); // ((n - 1) - 2) + 1
                
                a = new BigInteger(randRange.bitLength(), new Random())
                        .mod(randRange)
                        .add(min);
            }
            witnesses[i] = a;
        }
        return witnesses;
    }
    
    /**
     * Find x and y, i.e. Bézout's coefficients of a and b.
     * @param a The first input value.
     * @param b The second input value.
     * @return An array holding the greatest common divisor, and Bézout's coefficients (of input values a and b): x, and y.
     */
    public static BigInteger[] extendedEuclidean(BigInteger a, BigInteger b) {
        // Make sure that the recursion ends when b, i.e. the remainder, is zero.
        if (b.equals(BigInteger.ZERO)) {
            return new BigInteger[]{a, BigInteger.ONE, BigInteger.ZERO};
        }
        
        // Swap parameter order. a % b is the remainder when a is divided by b.
        BigInteger modResult = a.mod(b);
        BigInteger[] previousResult = extendedEuclidean(b, modResult);
        BigInteger gcd = previousResult[0];
        BigInteger x = previousResult[1];
        BigInteger y = previousResult[2];
        
        BigInteger newX = y;
        
        // (a / b) is the coefficient of a, since b goes into a this many times.
        // Multiply by y, since we are calculating ax + by.
        // Subtract from the previous x value.
        BigInteger division = a.divide(b);
        BigInteger multiplication = division.multiply(y);
        BigInteger newY = x.subtract(multiplication);
        
        BigInteger[] result = new BigInteger[]{gcd, newX, newY};
        return result;
    }
    
    /**
     * https://www.geeksforgeeks.org/primality-test-set-3-miller-rabin/
     * @param a Random witness against which the test is made.
     * @param d The remainder d calculated when (n - 1) was written as 2^r * d.
     * @param n The number under test.
     * @return Whether n is a likely prime or not.
     */
    private static boolean millerRabinTest(BigInteger a, BigInteger d, BigInteger n) {
        BigInteger result = modularExponentiation(a, d, n);
        
        // If either of the conditions is true, return and repeat with a different value of a.
        // NOTE: this is merely the first part of the test. We need to test for whether there exists a non-trivial square root for 1 (mod n).
        if (result.equals(BigInteger.ONE) || result.equals(n.subtract(BigInteger.ONE))) {
            return true;
        }
        
        return squaredPowerModCheck(d, n, result);
    }
    
    /**
     * Calculate a^d % n to be used in the Miller-Rabin test.
     * @param base The random witness against which the test is made.
     * @param exponent The remainder part which was calculated when (n - 1) was written as 2^r * d.
     * @param modulus The number by which the division is made. Basically n.
     * @return Result of a^d % n, which is the remainder when a is raised to the power of d and the result is divided by n.
     */
    private static BigInteger modularExponentiation(BigInteger base, BigInteger exponent, BigInteger modulus) {
        if (modulus.equals(BigInteger.ONE)) {
            return BigInteger.ZERO;
        }
        
        BigInteger result = BigInteger.ONE;
        BigInteger baseValue = base.mod(modulus); // a % n
        
        // x.compareTo(y) returns 1 when x > y.
        while (exponent.compareTo(BigInteger.ZERO) > 0) {
            if (exponent.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
                result = result.multiply(baseValue).mod(modulus); // multiply previous result by (a % n) and divide by n. The new result is the remainder.
            }
            baseValue = baseValue.multiply(baseValue).mod(modulus);
            exponent = exponent.divide(BigInteger.TWO);
        }
        return result;
    }
    
    /**
     * Keep squaring x and calculating modulus while d does not become (n - 1).
     * @param d Received when n - 1 was written as 2^r * d.
     * @param n The number under test, prime candidate.
     * @param x The result calculated in the first stage of Miller-Rabin test.
     * @return True if n is a likely prime, false if n is composite.
     */
    private static boolean squaredPowerModCheck(BigInteger d, BigInteger n, BigInteger x) {
        while (!d.equals(n.subtract(BigInteger.ONE))) {
            x = x.multiply(x).mod(n);
            d = d.multiply(BigInteger.TWO);
            
            if (x.equals(BigInteger.ONE)) {
                return false;
            }
            
            if (x.equals(n.subtract(BigInteger.ONE))) {
                return true;
            }
            
        }
        // composite
        return false;
    }
    
    /**
     * Sieve out prime candidates divisible by small primes before running Miller-Rabin test.
     * @param n The number under test for primality.
     * @return True if n is divisible by one of the first small primes, false otherwise.
     */
    private static boolean divisibleBySmallPrime(BigInteger n) {
        for (BigInteger small : SMALLPRIMES) {
            if (small.equals(n)) {
                break;
            }
            
            BigInteger modResult = n.mod(small);
            
            if (modResult.equals(BigInteger.ZERO)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Prepopulate the list of the first 200 small primes. The list is used for sieving out composites before running the Miller-Rabin test for prime candidates.
     * @return The list of the first 200 prime numbers.
     */
    private static List<BigInteger> sieveOfEratosthenes() {
        // The 200th prime is 1223.
        boolean[] prime = new boolean[1223 + 1];
        Arrays.fill(prime, true);
        
        // Start from number two since that is the first prime.
        // Mark multiples of each prime as composite.
        for (int number = 2; number * number < prime.length; number++) {
            if (prime[number]) {
                for (int i = number * 2; i < prime.length; i += number) {
                    prime[i] = false;
                }
            }
        }
        
        List<BigInteger> primeList = new ArrayList<>();
        for (int i = 2; i < prime.length; i++) {
            if (prime[i]) {
                primeList.add(BigInteger.valueOf(i));
            }
        }
        return primeList;
    }

}
