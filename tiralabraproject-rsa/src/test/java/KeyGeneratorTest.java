import com.ylireetta.tiralabraproject_rsa.KeyGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ylireett
 */
public class KeyGeneratorTest {
    KeyGenerator generator = new KeyGenerator();
    
    @Test
    public void keysGetGenerated() {
        Assertions.assertNull(generator.getPublicKey());
        Assertions.assertNull(generator.getPrivateKey());
        
        generator.generateKeys();
        
        Assertions.assertNotNull(generator.getPublicKey());
        Assertions.assertNotNull(generator.getPrivateKey());
    }
}
