import com.ylireetta.tiralabraproject_rsa.tools.PrimeHelper;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

public class PrimeHelperTest {
    private static long[] primes;
    private static long[] composites;
        
    @BeforeAll
    public static void setUpClass() {
        primes = new long[]{59107,38177,59779,87179,7333,20393,97231,63629,56333,94603,769,12613,9151,66931,58943};
        composites = new long[primes.length];
        
        int index = 0;
        for (long prime : primes) {
            composites[index] = prime * 2;
            index++;
        }
        
        // Add a few odd composites.
        composites[0] = 121;
        composites[1] = 187;
        composites[2] = 667;
        composites[3] = 493;
        
        // Test a few Carmichael numbers.
        composites[4] = 561;
        composites[5] = 1105;
        composites[6] = 1729;
    }

    @Test
    public void primeNumbersAreDetected() {        
        for (long prime : primes) {
            Assertions.assertTrue(PrimeHelper.isPrime(BigInteger.valueOf(prime)));
        }
    }
    
    @Test
    public void compositeNumbersAreDetected() {
        for (long composite : composites) {
            Assertions.assertFalse(PrimeHelper.isPrime(BigInteger.valueOf(composite)));
        }
    }
    
    @Test
    public void extendedEuclideanTest() {
        BigInteger a = new BigInteger("17");
        BigInteger b = new BigInteger("8");
        
        BigInteger[] result = PrimeHelper.extendedEuclidean(a, b);
        BigInteger gcd = result[0];
        BigInteger x = result[1];
        BigInteger y = result[2];
        
        Assertions.assertEquals(BigInteger.ONE, gcd);
        Assertions.assertEquals(BigInteger.ONE, x);
        Assertions.assertEquals(new BigInteger("-2"), y);
    }
    
    @Test
    public void millerRabinWithBigComposite() {
        // Test by creating two LARGE primes (google) as BigIntegers. Multiply them --> not prime anymore.
        // MillerRabin should return false. Write a test for the Miller-Rabin specifically!!!
        BigInteger a = new BigInteger("4125636888562548868221559797461449");
        BigInteger b = new BigInteger("23768741896345550770650537601358309");
        BigInteger c = a.multiply(b);
        
        Assertions.assertFalse(PrimeHelper.isPrime(c));
    }
    
    @Test
    public void millerRabinWithBigPrime() {
        // Test by a LARGE prime (google) as BigInteger.
        // MillerRabin should return true. Write a test for the Miller-Rabin specifically!!!
        BigInteger a = new BigInteger("4125636888562548868221559797461449");
        
        Assertions.assertTrue(PrimeHelper.isPrime(a));
    }
}
