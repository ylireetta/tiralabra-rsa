package com.ylireetta.tiralabraproject_rsa;

import java.math.BigInteger;

public interface UserKey {
    public String getType();
    
    /**
     * Get the public or private exponent part of the key.
     * @return The public exponent e, or the private exponent d, depending on the key type.
     */
    public BigInteger getExponent();
    
    /**
     * Get the modulus part of the key.
     * @return The modulus n of the key.
     */
    public BigInteger getModulus();
}
