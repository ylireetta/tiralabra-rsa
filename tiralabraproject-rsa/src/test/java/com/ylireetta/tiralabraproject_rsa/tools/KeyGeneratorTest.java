package com.ylireetta.tiralabraproject_rsa.tools;

import com.ylireetta.tiralabraproject_rsa.interfaces.UserKey;
import com.ylireetta.tiralabraproject_rsa.tools.KeyGenerator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class KeyGeneratorTest {    
    @Test
    public void keysGetGenerated() {        
        List<UserKey> keys = KeyGenerator.generateKeys();
        
        for (UserKey key : keys) {
            Assertions.assertNotNull(key);
        }   
    }
}
