package com.ylireetta.tiralabraproject_rsa;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PrivateKey {
    private long d;
    private long n;
    
    public PrivateKey(long d, long n) {
        this.d = d;
        this.n = n;
    }
    
    public long getPrivateExponent() {
        return d;
    }
    
    public long getModulus() {
        return n;
    }
    
    public void setPrivateExponent(long d) {
        this.d = d;
    }
    
    public void setModulus(long n) {
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
