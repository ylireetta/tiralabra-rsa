import com.ylireetta.tiralabraproject_rsa.tools.PrimeHelper;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

public class PrimeHelperTest {
    private static BigInteger[] primes;
    private static BigInteger[] composites;
        
    @BeforeAll
    public static void setUpClass() {
        primes = new BigInteger[]{
            new BigInteger("170141183460469231731687303715884105727"),
            new BigInteger("43143988327398957279342419750374600193"),
            new BigInteger("6161791591356884791277"),
            new BigInteger("19134702400093278081449423917"),
            new BigInteger("8683317618811886495518194401279999999")
        };
        composites = new BigInteger[]{
            new BigInteger("81917945374322723989"),
            new BigInteger("129472543259751776239"),
            new BigInteger("252318994941050036377"),
            new BigInteger("448858548276846497573"),
            new BigInteger("661670974840309909299")
        };
    }
    
    @Test
    public void generatePrimeGeneratesProbablePrime() {
        BigInteger probablePrime = PrimeHelper.generatePrime();
        
        Assertions.assertTrue(probablePrime.isProbablePrime(95));
    }

    @Test
    public void primeNumbersAreDetected() {        
        for (BigInteger prime : primes) {
            Assertions.assertTrue(PrimeHelper.isPrime(prime));
        }
    }
    
    @Test
    public void compositeNumbersAreDetected() {
        for (BigInteger composite : composites) {
            Assertions.assertFalse(PrimeHelper.isPrime(composite));
        }
    }
        
    @Test
    public void extendedEuclideanWithBigIntegers() {
        BigInteger a = new BigInteger("1234567890123456789");
        BigInteger b = new BigInteger("987654321987654321");
        
        BigInteger expectedGcd = a.gcd(b);
        // Expected values calculated using https://www.extendedeuclideanalgorithm.com/calculator.php?mode=1&a=1234567890123456789&b=987654321987654321#num
        BigInteger expectedX = new BigInteger("27628855136539747");
        BigInteger expectedY = new BigInteger("-34536068574882294");
        
        BigInteger[] result = PrimeHelper.extendedEuclidean(a, b);
        Assertions.assertEquals(expectedGcd, result[0]);
        Assertions.assertEquals(expectedX, result[1]);
        Assertions.assertEquals(expectedY, result[2]);
    }
    
    @Test
    public void millerRabinWithBigComposite() {
        // Test by creating two LARGE primes (google) as BigIntegers. Multiply them --> not prime anymore.
        // MillerRabin should return false.
        BigInteger a = new BigInteger("4125636888562548868221559797461449"); // Pell prime from Wikipedia
        BigInteger b = new BigInteger("23768741896345550770650537601358309"); // Primorial prime from Wikipedia
        BigInteger c = a.multiply(b);
        
        BigInteger d = new BigInteger("24515299590571338876728061562132160879215807179201986447978115832435");
        BigInteger witness = new BigInteger("58268770760342183424263560258622945581626744519917810239267918872632");
        
        Assertions.assertFalse(PrimeHelper.millerRabinTest(witness, d, c));
    }
    
    @Test
    public void millerRabinWithBigPrime() {
        BigInteger a = new BigInteger("4125636888562548868221559797461449"); // Pell prime from Wikipedia
        
        BigInteger d = new BigInteger("515704611070318608527694974682681");
        BigInteger witness = new BigInteger("2681381924148598885035133075237174");
        
        Assertions.assertTrue(PrimeHelper.millerRabinTest(witness, d, a));
    }
    
    @Test
    public void randomWitnessesGeneration() {
        BigInteger n = new BigInteger("123456789123456789123456789");
        BigInteger[] witnesses = PrimeHelper.randomWitnesses(n, 10);
        
        for (BigInteger witness: witnesses) {
            BigInteger gcd = n.gcd(witness);
            Assertions.assertEquals(BigInteger.ONE, gcd);
        }
    }
    
    @Test
    public void testFactorizePowerTwo() {
        BigInteger n = new BigInteger("65537");
        BigInteger[] expectedResult = {
            BigInteger.valueOf(16),
            BigInteger.ONE
        };
        
        BigInteger[] result = PrimeHelper.factorizePowerTwo(n);
        Assertions.assertEquals(expectedResult[0], result[0]);
        Assertions.assertEquals(expectedResult[1], result[1]);
    }
    
    @Test
    public void testModularExponentiation() {
        BigInteger a = new BigInteger("123456789123456789123456789");
        BigInteger d = new BigInteger("9876543212345678987654321");
        BigInteger n = new BigInteger("192837465192837465192837465");
        BigInteger expected = a.modPow(d, n);
        
        BigInteger result = PrimeHelper.modularExponentiation(a, d, n);
        Assertions.assertEquals(expected, result);
    }
    
    @Test
    public void sieveOfEratosthenesGeneration() {
        List<BigInteger> sieve = PrimeHelper.sieveOfEratosthenes();
        
        for (BigInteger number : sieve) {
            Assertions.assertTrue(number.isProbablePrime(95));
        }
    }
        
    @Test
    public void numberIsDivisibleBySmallPrimes() {
        BigInteger composite = new BigInteger("129472543259751776239");
        
        Assertions.assertTrue(PrimeHelper.divisibleBySmallPrime(composite));
    }
    
    @Test
    public void numberNotDivisibleBySmallPrimes() {
        BigInteger prime = new BigInteger("4125636888562548868221559797461449"); // Pell prime from Wikipedia. Not divisible by small primes.
        
        Assertions.assertFalse(PrimeHelper.divisibleBySmallPrime(prime));
    }
    
    @Test
    public void squaredPowerModCheckTest() {
        // TODO
    }
}
