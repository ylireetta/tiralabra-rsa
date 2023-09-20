package com.ylireetta.tiralabraproject_rsa;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PublicKey implements UserKey {
    private long e;
    private long n;
    private final String type = "public";
    
    public PublicKey(long e, long n) {
        this.e = e;
        this.n = n;
    }
    
    public long getPublicExponent() {
        return e;
    }
    
    public long getModulus() {
        return n;
    }
    
    public String getType() {
        return type;
    }
    
    public void setPublicExponent(long e) {
        this.e = e;
    }
    
    public void setModulus(long n) {
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
