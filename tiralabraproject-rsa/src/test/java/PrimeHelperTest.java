import com.ylireetta.tiralabraproject_rsa.PrimeHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

/**
 *
 * @author ylireett
 */
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
    }

    @Test
    public void primeNumbersAreDetected() {        
        for (long prime : primes) {
            Assertions.assertTrue(PrimeHelper.isPrime(prime));
        }
    }
    
    @Test
    public void compositeNumbersAreDetected() {
        for (long composite : composites) {
            Assertions.assertFalse(PrimeHelper.isPrime(composite));
        }
    }
    
    @Test
    public void extendedEuclideanTest() {
        long a = 17;
        long b = 8;
        
        long[] result = PrimeHelper.extendedEuclidean(a, b);
        long gcd = result[0];
        long x = result[1];
        long y = result[2];
        
        Assertions.assertEquals(1, gcd);
        Assertions.assertEquals(1, x);
        Assertions.assertEquals(-2, y);
    }
}
