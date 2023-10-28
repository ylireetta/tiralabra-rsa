package com.ylireetta.tiralabraproject_rsa.pojo;

import com.ylireetta.tiralabraproject_rsa.interfaces.UserKey;
import java.math.BigInteger;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PublicKey implements UserKey {
    private BigInteger e;
    private BigInteger n;
    private final String type = "public";
    
    public PublicKey(BigInteger e, BigInteger n) {
        this.e = e;
        this.n = n;
    }
    
    @Override
    public BigInteger getExponent() {
        return e;
    }
    
    @Override
    public BigInteger getModulus() {
        return n;
    }
    
    @Override
    public String getType() {
        return type;
    }
    
    public void setExponent(BigInteger e) {
        this.e = e;
    }
    
    public void setModulus(BigInteger n) {
        this.n = n;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .append("e", e)
            .append("n", n)
            .toString();
    }
}
