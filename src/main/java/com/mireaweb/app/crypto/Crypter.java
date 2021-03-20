package com.mireaweb.app.crypto;
/**
* Convenient codec for encryption and decryption
 */
public class Crypter {
    private static String secret = "secret";

    static public String encode(String originalString) {
        return AES.encrypt(originalString, secret);
    }

    static public String decode(String encryptedString) {
        return AES.decrypt(encryptedString, secret);
    }

    public static void main(String[] args) {
        System.out.println(encode("admadm"));
    }
}
