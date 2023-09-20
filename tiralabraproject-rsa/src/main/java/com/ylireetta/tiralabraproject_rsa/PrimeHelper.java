package com.ylireetta.tiralabraproject_rsa;

import java.util.concurrent.ThreadLocalRandom;

public class PrimeHelper {
    private static long[] smallPrimes = new long[]{3, 5, 7, 11, 13, 17, 19, 23, 27, 29};
    
    /**
     * Generate a prime number.
     * @return A prime number.
     */
    public static long generatePrime() {
        long primeCandidate = ThreadLocalRandom.current().nextLong(100000L, 10000000000L);
        //System.out.println("Prime candidate " + primeCandidate);
        
        while (!isPrime(primeCandidate)) {
            primeCandidate = ThreadLocalRandom.current().nextLong(100000L, 10000000000L);
            //System.out.println("Did not find prime or a suitable prime, new candidate " + primeCandidate);
        }
        
        return primeCandidate;
    }
    
    /**
     * Miller-Rabin primality test.
     * @param n The integer to test for primality.
     * @return Boolean indicating whether n is a likely prime or not.
     */
    public static boolean isPrime(long n) {
        // If n is less than two or even, it cannot be prime.
        if (n < 2 || n % 2 == 0) return false;
        
        if (divisibleBySmallPrime(n)) return false;
        
        // Write n-1 as 2^r*d
        long[] resultArray = factorizePowerTwo(n);
        long d = resultArray[1];
        
        // Choice of k: a larger value provides more accuracy, but requires more calculation effort.
        long[] randomWitnesses = randomWitnesses(n, 5);
        
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
    private static long[] factorizePowerTwo(long n) {
        long r = 0;
        long d = n - 1;
        
        while (d % 2 == 0) {
            r++;
            d = d / 2;
        }
        
        // At this point, 'r' is the largest power of 2 such that 2^r divides 'n - 1', and 'd' is the remainder.
        return new long[]{r, d};
    }
    
    /**
     * 
     * @param n Number to test.
     * @param k Number of random witnesses to generate.
     * @return An array with k random witnesses.
     */
    private static long[] randomWitnesses(long n, int k) {
        long[] witnesses = new long[k];
        
        for (int i = 0; i < k; i++) {
            long a = 0;
            
            // Calculate the greatest common divisor.
            while (extendedEuclidean(a, n)[0] != 1) {
                a = ThreadLocalRandom.current().nextLong(n - 2) + 2;
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
    public static long[] extendedEuclidean(long a, long b) {
        // Make sure that the recursion ends when b, i.e. the remainder, is zero.
        if (b == 0) {
            return new long[]{a, 1, 0};
        }
        
        // Swap parameter order. a % b is the remainder when a is divided by b.
        long[] previousResult = extendedEuclidean(b, a % b);
        long gcd = previousResult[0];
        long x = previousResult[1];
        long y = previousResult[2];
        
        long newX = y;
        
        // (a / b) is the coefficient of a, since b goes into a this many times.
        // Multiply by y, since we are calculating ax + by.
        // Subtract from the previous x value.
        long newY = x - (a / b) * y;
        
        long[] result = new long[]{gcd, newX, newY};
        return result;
    }
    
    /**
     * https://www.geeksforgeeks.org/primality-test-set-3-miller-rabin/
     * @param a Random witness against which the test is made.
     * @param d The remainder d calculated when (n - 1) was written as 2^r * d.
     * @param n The number under test.
     * @return Whether n is a likely prime or not.
     */
    private static boolean millerRabinTest(long a, long d, long n) {
        long result = modularExponentiation(a, d, n);
        
        // If either of the conditions is true, return and repeat with a different value of a.
        // NOTE: this is merely the first part of the test. We need to test for whether there exists a non-trivial square root for 1 (mod n).
        if (result == 1 || result == (n - 1)) return true;
        
        return squaredPowerModCheck(d, n, result);
    }
    
    /**
     * Calculate a^d % n to be used in the Miller-Rabin test.
     * @param base The random witness against which the test is made.
     * @param exponent The remainder part which was calculated when (n - 1) was written as 2^r * d.
     * @param modulus The number by which the division is made. Basically n.
     * @return Result of a^d % n, which is the remainder when a is raised to the power of d and the result is divided by n.
     */
    private static long modularExponentiation(long base, long exponent, long modulus) {
        if (modulus == 1) return 0;
        
        long result = 1;
        long baseValue = base % modulus; // a % n
        
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * baseValue) % modulus; // multiply previous result by (a % n) and divide by n. The new result is the remainder.
            }
            baseValue = (baseValue * baseValue) % modulus;
            exponent = exponent / 2;
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
    private static boolean squaredPowerModCheck(long d, long n, long x) {
        while (d != n - 1) {
            x = (x * x) % n;
            d = d * 2;
            
            if (x == 1) return false;
            if (x == n - 1) return true;
            
        }
        // composite
        return false;
    }
    
    /**
     * Sieve out prime candidates divisible by small primes before running Miller-Rabin test.
     * @param n The number under test for primality.
     * @return True if n is divisible by one of the first small primes, false otherwise.
     */
    private static boolean divisibleBySmallPrime(long n) {
        for (long small : smallPrimes) {
            if (n % small == 0) {
                return true;
            }
        }
        
        return false;
    }
}
