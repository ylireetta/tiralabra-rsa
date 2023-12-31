package com.ylireetta.tiralabraproject_rsa.interfaces;

import java.math.BigInteger;

public interface UserKey {
    /**
     * Get the type of the user key.
     * @return "public" or "private", depending on the type of the key.
     */
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
