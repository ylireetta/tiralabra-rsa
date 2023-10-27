import com.ylireetta.tiralabraproject_rsa.PublicKey;
import com.ylireetta.tiralabraproject_rsa.UserKey;
import com.ylireetta.tiralabraproject_rsa.tools.FileHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;

public class FileHelperTest {
    @TempDir
    static Path tempDir;
    
    private FileHelper fileHelper;
    
    private final String username = "testuser";
    
    private final String publicFileName = "public/" + username + ".txt";
    private final String privateFileName = "private/" + username + ".txt";
    
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
        PublicKey key = new PublicKey(
                BigInteger.ONE,
                BigInteger.TEN
        );
        boolean result = fileHelper.writeToFile(username, key);
        
        File file1 = tempDir.resolve(publicFileName).toFile();
        
        assertTrue(file1.exists());
        assertTrue(result);
    }
    
    @Test
    public void writeKeysDoesNotCreateDuplicateFiles() throws IOException {
        PublicKey key = new PublicKey(
                BigInteger.ONE,
                BigInteger.TEN
        );
        boolean firstResult = fileHelper.writeToFile(username, key);
        boolean secondResult = fileHelper.writeToFile(username, key);
        
        assertTrue(firstResult);
        assertFalse(secondResult);
    }
    
    @Test
    public void writeToFileReturnsTrueIfSuccessful() throws IOException {
        UserKey key = new PublicKey(BigInteger.TEN, BigInteger.TWO);
        assertTrue(fileHelper.writeToFile("someUser", key));
    }
    
    @Test
    public void writeToFileReturnsFalseIfNotSuccessful() throws IOException {
        // writeToFile calls createFile which returns false if a file with the same name already exists. Create a file first so that createFile returns false later.
        String newFileName = "public/someUser.txt";
        UserKey key = new PublicKey(BigInteger.TEN, BigInteger.TWO);
        File publicFile = new File(tempDir.toString() + "/" + newFileName);
        publicFile.createNewFile();
        
        assertFalse(fileHelper.writeToFile("someUser", key));
    }
    
    @Test
    public void createFileCreatesFileWhenUsernameIsLowerCase() {
        String completeFileName = tempDir.toString() + "/" + publicFileName;
        assertTrue(fileHelper.createFile(completeFileName));
    }
    
    @Test
    public void createFileCreatesFileWhenUsernameContainsCapitalLetters() {
        String fileName = tempDir.toString() + "/" + "public/HereWeHaveCaPiTaL.txt";
        assertTrue(fileHelper.createFile(fileName));
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
        
        List<UserKey> result = fileHelper.readFromFile(username);
        assertEquals(2, result.size());
    }
    
    @Test
    public void readFileThrowsExceptionIfFileNotFound() {
        assertThrows(FileNotFoundException.class, () -> fileHelper.readFromFile(username));
    }
    
    @Test
    public void retrieveUserFileReturnsFileIfUserExists() throws IOException {
        File publicFile = new File(tempDir.toString() + "/" + publicFileName);
        publicFile.createNewFile();
        
        assertNotNull(fileHelper.retrieveUserFile(username, "public"));
    }
    
    @Test
    public void retrieveUserFileReturnsNullIfUserNotFound() throws IOException {
        // Create a file for another user.
        File publicFile = new File(tempDir.toString() + "/" + publicFileName);
        publicFile.createNewFile();
        
        assertNull(fileHelper.retrieveUserFile("nonexistent", "public"));
    }
    
    @Test
    public void usernameTakenChangesValue() throws IOException {
        assertFalse(fileHelper.usernameTaken(username));
        
        File publicFile = new File(tempDir.toString() + "/" + publicFileName);
        publicFile.createNewFile();
        
        assertTrue(fileHelper.usernameTaken(username));
        assertFalse(fileHelper.usernameTaken("anotheruser"));
    }
    
    @Test
    public void usernameTakenReturnsTrueWhenEitherKeyExists() throws IOException {
        // If keys of both publicity classes are not found, usernameTaken still returns true, because a new file cannot be created using the same username/filename.
        File privateFile = new File(tempDir.toString() + "/" + privateFileName);
        privateFile.createNewFile();
        assertTrue(fileHelper.usernameTaken(username));
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
    public void getKeyFromFileWithInvalidKeyTypeThrowsException() throws IOException {
        File publicFile = new File(tempDir.toString() + "/" + publicFileName);
        publicFile.createNewFile();
        
        // Write content to key file.
        String content = "123456789,987654321";
        Files.write(publicFile.toPath(), content.getBytes());
        
        assertThrows(IllegalArgumentException.class, () -> fileHelper.getKeyFromFile(publicFile, "invalid"));
    }
    
    @Test
    public void getKeyFromFileReturnsNullIfFileEmpty() throws IOException {
        File publicFile = new File(tempDir.toString() + "/" + publicFileName);
        publicFile.createNewFile();
        
        assertNull(fileHelper.getKeyFromFile(publicFile, "public"));
    }
    
    @Test
    public void binarySearchReturnsExpectedOrder() {
        File[] files = createTestFileArray();
                
        int smallerIndex = fileHelper.binarySearch(files, "reetta");
        int biggerIndex = fileHelper.binarySearch(files, "reetta6");
        
        assertTrue(smallerIndex < biggerIndex);        
    }
    
    @Test
    public void binarySearchReturnsExpectedIfFileNotFound() {
        File[] files = createTestFileArray();
        
        assertEquals(-1, fileHelper.binarySearch(files, "testnamenotfound"));
    }
    
    @Test
    public void getNameStartReturnsExpectedIfDotsFound() {
        String fileNameWithDot = "ihavedots.txt";
        String fileNameWithoutDot = "filenamewithoutdot";
        String resultWithDot = fileHelper.getNameStart(fileNameWithDot);
        
        assertEquals(-1, resultWithDot.indexOf("."));
        assertEquals(fileNameWithoutDot, fileHelper.getNameStart(fileNameWithoutDot));
    }
    
    /**
     * Create an array of files for binary search tests.
     * @return An array of files with suitable file names.
     */
    private File[] createTestFileArray() {
        String[] fileNames = new String[]{
            "ällömöllö.txt",
            "testfile605.txt",
            "first.txt",
            "testfile.txt",
            "testfile1.txt",
            "45file.txt",
            "tttttest.txt",
            "test.txt",
            "abcd.txt",
            "reetta6.txt",
            "reetta5.txt",
            "reetta.txt",
            "reettax.txt"
        };
        
        File[] files = new File[fileNames.length];
        
        for (int i = 0; i < files.length; i++) {
            files[i] = new File(tempDir.toString(), fileNames[i]);
        }
        
        return files;
    }
}
