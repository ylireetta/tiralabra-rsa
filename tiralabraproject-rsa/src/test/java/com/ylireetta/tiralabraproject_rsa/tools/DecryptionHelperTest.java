package com.ylireetta.tiralabraproject_rsa.tools;

import com.ylireetta.tiralabraproject_rsa.tools.DecryptionHelper;
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

public class DecryptionHelperTest {
    private static final BigInteger PRIVATE_EXPONENT = new BigInteger("1136836078345900611944719352644237652807956853519586414713705537194080771334"
            + "5365606985524200672881717512581767178123438822808408871185231769862268436294480570354727399358937473881659638094"
            + "8384297405123466475338867346608722378055842237969768609570482322082666829027885086002968671296022401106225720229"
            + "9448992530966675830159898318296410714751134300274096524290201264738176942992282818055936422746082034482403762571"
            + "6186813747832426366060080016939050154194798423989045367752417153541898445503266030911987680890635988540433251760"
            + "31012287648293392098338697523276538375958779451316622321036809602960706797473");
    
    private static final BigInteger PRIVATEMODULUS = new BigInteger("258967070095777853336882419931336124616180286093545828505707750403505288533025"
            + "7997167223842681608095657358607144778157143310374321136136720547318340317801088423141361042011418928683794584016"
            + "0798397773988955141756514753895858500680336287190564356693430456488480966286078836901487002875982277715380697647"
            + "4067066798148744855644156861629500418803440281049213226735778993409203236770780472882355604321792790669191792746"
            + "3141428834657189465705608003793111723824109915903438347458267722282518755691382471852027582848179571023252962939"
            + "9197322741114813662604469233976494216449910674746975557532170918219537599171");
    
    @TempDir
    static Path tempDir;
    
    private static FileHelper fileHelper;

    private static final String PRIVATEFILENAME = "private/testuser.txt";
    
    private static DecryptionHelper decryptionHelper;

    @BeforeAll
    public static void setUpClass() throws IOException {
        fileHelper = new FileHelper(tempDir.toString() + "/");
        decryptionHelper = new DecryptionHelper(fileHelper);
        
        // Create subdirectory under tempdir.
        Path privateSubDir = tempDir.resolve("private");
        Files.createDirectories(privateSubDir);
    }
    
    @AfterEach
    public void tearDown() throws IOException {
        // Delete the created files after each test.
        Files.deleteIfExists(tempDir.resolve(PRIVATEFILENAME));
    }
    
    @Test
    public void decryptionWorks() throws IOException {
        // Create the encrypted message first by using modPow.
        // Create expected result by using modPow.
        BigInteger encrypted = createEncryptedMessage();
        String expected = createExpectedValue(encrypted);
        createTestFile();
        
        assertEquals(expected, decryptionHelper.decryptMessage("testuser", encrypted.toString()));
    }
    
    @Test
    public void decryptMessageThrowsExceptionIfFileNotFound() {
        assertThrows(FileNotFoundException.class, () -> decryptionHelper.decryptMessage("testuser", "notfound"));
    }
    
    /**
     * Create a file with a working private key to use in tests.
     * @throws IOException 
     */
    private void createTestFile() throws IOException {
        // Create a private key file.
        File privateFile = new File(tempDir.toString() + "/" + PRIVATEFILENAME);
        privateFile.createNewFile();
        
        // Write the file content.
        String key = PRIVATE_EXPONENT + "," + PRIVATEMODULUS;
        Files.write(privateFile.toPath(), key.getBytes());
    }
    
    /**
     * Create an encrypted message to decrypt in the tests.
     * @return An encrypted message as BigInteger.
     */
    private BigInteger createEncryptedMessage() {
        String message = "Hello from test! 123456789?~^`'_äöÅÄÄäöÖ";
        byte[] messageBytes = message.getBytes();
        BigInteger numericForm = new BigInteger(1, messageBytes);
        
        // Use some arbitrary public exponent to encrypt. The modulus part is the same for both keys.
        BigInteger testPublicExponent = new BigInteger("65537");
        BigInteger encryptedValue = numericForm.modPow(testPublicExponent, PRIVATEMODULUS);
        return encryptedValue;
    }
    
    /**
     * Decrypt the test message using built-in modPow and the private key.
     * @param encryptedMessage The message to decrypt.
     * @return The decrypted message.
     */
    private String createExpectedValue(BigInteger encryptedMessage) {
        BigInteger decryptedNumeric = encryptedMessage.modPow(PRIVATE_EXPONENT, PRIVATEMODULUS);
        byte[] decryptedBytes = decryptedNumeric.toByteArray();
        String decryptedMessage = new String(decryptedBytes);
        
        return decryptedMessage;
    }
    
}
