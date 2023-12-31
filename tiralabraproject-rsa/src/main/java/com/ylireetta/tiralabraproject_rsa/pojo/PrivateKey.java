package com.ylireetta.tiralabraproject_rsa.pojo;

import com.ylireetta.tiralabraproject_rsa.interfaces.UserKey;
import java.math.BigInteger;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PrivateKey implements UserKey {
    private BigInteger d;
    private BigInteger n;
    private final String type = "private";
    
    public PrivateKey(BigInteger d, BigInteger n) {
        this.d = d;
        this.n = n;
    }
    
    @Override
    public BigInteger getExponent() {
        return d;
    }
    
    @Override
    public BigInteger getModulus() {
        return n;
    }
    
    @Override
    public String getType() {
        return type;
    }
    
    public void setExponent(BigInteger d) {
        this.d = d;
    }
    
    public void setModulus(BigInteger n) {
        this.n = n;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .append("d", d)
            .append("n", n)
            .toString();
    }
    
}
