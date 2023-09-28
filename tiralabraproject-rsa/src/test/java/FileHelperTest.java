import com.ylireetta.tiralabraproject_rsa.UserKey;
import com.ylireetta.tiralabraproject_rsa.tools.FileHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;

public class FileHelperTest {
    @TempDir
    static Path tempDir;
    
    private FileHelper fileHelper;
    
    private final String publicFileName = "public/testuser_public_key.txt";
    private final String privateFileName = "private/testuser_private_key.txt";
    
    @BeforeEach
    public void setup() throws IOException {
        this.fileHelper = new FileHelper(tempDir.toString() + "/");
        
        // Create subdirectories under tempdir.
        Path publicSubDir = tempDir.resolve("public");
        Files.createDirectories(publicSubDir);
        
        Path privateSubDir = tempDir.resolve("private");
        Files.createDirectories(privateSubDir);
    }
    
    @AfterEach
    public void cleanup() throws IOException {
        // Delete the created files after each test.
        Files.deleteIfExists(tempDir.resolve(publicFileName));
        Files.deleteIfExists(tempDir.resolve(privateFileName));
    }
    
    @Test
    public void fileGetsCreated() throws IOException {
        boolean result = fileHelper.writeKeys("testuser");
        
        File file1 = tempDir.resolve(publicFileName).toFile();
        File file2 = tempDir.resolve(privateFileName).toFile();
        
        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertTrue(result);
    }
    
    @Test
    public void fileAlreadyExists() throws IOException {
        boolean firstResult = fileHelper.writeKeys("testuser");
        boolean secondResult = fileHelper.writeKeys("testuser");
        
        assertTrue(firstResult);
        assertFalse(secondResult);
    }
    
    @Test
    public void readFileIsSuccessful() throws IOException {
        File publicFile = new File(tempDir.toString() + "/" + publicFileName);
        publicFile.createNewFile();
        File privateFile = new File(tempDir.toString() + "/" + privateFileName);
        privateFile.createNewFile();
        
        // Write content to key files so that we can do something with the keys.
        String content = "123456789,987654321";
        Files.write(publicFile.toPath(), content.getBytes());
        Files.write(privateFile.toPath(), content.getBytes());
        
        // This method is void at the moment and just prints the contents of the files, so more helpful assertions need to be written in the coming weeks.        
        assertDoesNotThrow(() -> fileHelper.readFromFile("testuser"));
    }
    
    @Test
    public void readFileIsUnsuccessful() {
        assertThrows(FileNotFoundException.class, () -> fileHelper.readFromFile("testuser"));
    }
    
    @Test
    public void usernameTakenChangesValue() throws IOException {
        assertFalse(fileHelper.usernameTaken("testuser"));
        
        File publicFile = new File(tempDir.toString() + "/" + publicFileName);
        publicFile.createNewFile();
        
        assertTrue(fileHelper.usernameTaken("testuser"));
        assertFalse(fileHelper.usernameTaken("anotheruser"));
    }
    
    @Test
    public void retrieveUserFileIfUserExists() throws IOException {
        File publicFile = new File(tempDir.toString() + "/" + publicFileName);
        publicFile.createNewFile();
        
        assertNotNull(fileHelper.retrieveUserFile("testuser", "public"));
    }
    
    @Test
    public void retrieveUserFileIfUserNotFound() throws IOException {
        // Create a file for another user.
        File publicFile = new File(tempDir.toString() + "/" + publicFileName);
        publicFile.createNewFile();
        
        assertNull(fileHelper.retrieveUserFile("nonexistent", "public"));
    }
    
    @Test
    public void getKeyFromFileReturnsKey() throws IOException {
        File publicFile = new File(tempDir.toString() + "/" + publicFileName);
        publicFile.createNewFile();
        
        // Write content to key file.
        String content = "123456789,987654321";
        Files.write(publicFile.toPath(), content.getBytes());
        
        // File contents.
        BigInteger expectedExponent = new BigInteger("123456789");
        BigInteger expectedModulus = new BigInteger("987654321");
        UserKey key = fileHelper.getKeyFromFile(publicFile, "public");
                
        assertEquals(expectedExponent, key.getExponent());
        assertEquals(expectedModulus, key.getModulus());
    }
    
    @Test
    public void getKeyFromFileWithMalformedDataThrowsException() throws IOException {
        File publicFile = new File(tempDir.toString() + "/" + publicFileName);
        publicFile.createNewFile();
        
        // Write content to key file.
        String content = "123456789,987654321,malformed";
        Files.write(publicFile.toPath(), content.getBytes());
        
        assertThrows(IllegalArgumentException.class, () -> fileHelper.getKeyFromFile(publicFile, "public"));
    }
    
    @Test
    public void getKeyFromFileWithInvalidKeyTypeReturnsNull() throws IOException {
        File publicFile = new File(tempDir.toString() + "/" + publicFileName);
        publicFile.createNewFile();
        
        // Write content to key file.
        String content = "123456789,987654321";
        Files.write(publicFile.toPath(), content.getBytes());
        
        assertNull(fileHelper.getKeyFromFile(publicFile, "invalid"));
    }
}
