import com.ylireetta.tiralabraproject_rsa.tools.EncryptionHelper;
import com.ylireetta.tiralabraproject_rsa.tools.FileHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;

public class EncryptionHelperTest {
    private static final BigInteger PUBLICEXPONENT = new BigInteger("65537");
    
    private static final BigInteger PUBLICMODULUS = new BigInteger("258967070095777853336882419931336124616180286093545828505707750403505288533025"
            + "7997167223842681608095657358607144778157143310374321136136720547318340317801088423141361042011418928683794584016"
            + "0798397773988955141756514753895858500680336287190564356693430456488480966286078836901487002875982277715380697647"
            + "4067066798148744855644156861629500418803440281049213226735778993409203236770780472882355604321792790669191792746"
            + "3141428834657189465705608003793111723824109915903438347458267722282518755691382471852027582848179571023252962939"
            + "9197322741114813662604469233976494216449910674746975557532170918219537599171");
    
    @TempDir
    static Path tempDir;
    
    private static FileHelper fileHelper;

    private static final String PUBLICFILENAME = "public/testuser.txt";
    
    private static EncryptionHelper encryptionHelper;

    @BeforeAll
    public static void setUpClass() throws IOException {
        fileHelper = new FileHelper(tempDir.toString() + "/");
        encryptionHelper = new EncryptionHelper(fileHelper);
        
        // Create subdirectory under tempdir.
        Path publicSubDir = tempDir.resolve("public");
        Files.createDirectories(publicSubDir);
    }
    
    @AfterEach
    public void tearDown() throws IOException {
        // Delete the created files after each test.
        Files.deleteIfExists(tempDir.resolve(PUBLICFILENAME));
    }

    @Test
    public void encryptSimpleString() throws IOException {
        // Create a working public key and use the values to encrypt (i.e. use modPow, not the tool).
        // Assert that the expected value equals the tool result.
        String message = "Hello world";
        BigInteger expected = createExpectedValue(message);
        createTestFile();
        
        assertEquals(expected, encryptionHelper.encryptMessage("testuser", message));
    }
    
    @Test
    public void encryptStringWithUnusualCharacters() throws IOException {
        String message = "123456789?=^~`'\"ÄÖäöÅåÜü¤;";
        BigInteger expected = createExpectedValue(message);
        createTestFile();
        
        assertEquals(expected, encryptionHelper.encryptMessage("testuser", message));        
    }
    
    @Test
    public void encryptStringWithEmptyMessage() throws IOException {
        String message = "";
        BigInteger expected = createExpectedValue(message);
        createTestFile();
        
        assertEquals(expected, encryptionHelper.encryptMessage("testuser", message));
    }
    
    @Test
    public void encryptMessageThrowsExceptionIfFileNotFound() {
        assertThrows(FileNotFoundException.class, () -> encryptionHelper.encryptMessage("testuser", "throws error"));
    }
    
    /**
     * Helper method to create an encrypted message to use for assertions.
     * @param message The message to encrypt.
     * @return The BigIntegre representation of the encrypted message.
     */
    private BigInteger createExpectedValue(String message) {
        byte[] messageBytes = message.getBytes();
        BigInteger numericForm = new BigInteger(1, messageBytes);
        
        BigInteger encryptedValue = numericForm.modPow(PUBLICEXPONENT, PUBLICMODULUS);
        return encryptedValue;
    }
    
    /**
     * Create a file with a working public key to use in tests.
     * @throws IOException 
     */
    private void createTestFile() throws IOException {
        // Create a public key file.
        File publicFile = new File(tempDir.toString() + "/" + PUBLICFILENAME);
        publicFile.createNewFile();
        
        // Write the file content.
        String key = PUBLICEXPONENT + "," + PUBLICMODULUS;
        Files.write(publicFile.toPath(), key.getBytes());
    }
}
