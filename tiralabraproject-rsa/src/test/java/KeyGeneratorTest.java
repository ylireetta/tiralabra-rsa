import com.ylireetta.tiralabraproject_rsa.tools.KeyGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
