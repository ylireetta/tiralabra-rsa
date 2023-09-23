import com.ylireetta.tiralabraproject_rsa.tools.FileHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
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
        this.fileHelper = new FileHelper(tempDir.toString());
        
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
        
        // This method is void at the moment and just prints the contents of the files, so more helpful assertions need to be written in the coming weeks.        
        assertDoesNotThrow(() -> fileHelper.readFromFile("testuser"));
    }
    
    @Test
    public void readFileIsUnsuccessful() {
        assertThrows(FileNotFoundException.class, () -> fileHelper.readFromFile("testuser"));
    }
}
