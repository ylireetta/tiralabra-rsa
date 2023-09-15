package com.ylireetta.tiralabraproject_rsa;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        KeyGenerator generator = new KeyGenerator();
        generator.generateKeys();
        
        System.out.println("Public key: " + generator.getPublicKey());
        System.out.println("Private key: " + generator.getPrivateKey());
    }
}
