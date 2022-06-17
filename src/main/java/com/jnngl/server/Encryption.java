package com.jnngl.server;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class Encryption {

    /* ============== RSA ============== */

    public KeyPair rsa;

    public void generateRSA() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        rsa = generator.generateKeyPair();
    }

    public byte[] decryptRSA(byte[] encrypted)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
                    IllegalBlockSizeException, BadPaddingException {
        Cipher decrypt = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        decrypt.init(Cipher.DECRYPT_MODE, rsa.getPrivate());
        return decrypt.doFinal(encrypted);
    }

    /* ============== AES ============== */

    public SecretKey aes;

    private Cipher decryptCipher;
    private Cipher encryptCipher;

    public void initAES(byte[] secret) {
        aes = new SecretKeySpec(secret, "AES");
    }

    public byte[] encryptAES(byte[] data)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        if(encryptCipher == null) {
            encryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, aes, new IvParameterSpec(aes.getEncoded()));
        }
        return encryptCipher.doFinal(data);
    }

    public byte[] decryptAES(byte[] encrypted)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        if(decryptCipher == null) {
            decryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
            decryptCipher.init(Cipher.DECRYPT_MODE, aes, new IvParameterSpec(aes.getEncoded()));
        }
        return decryptCipher.doFinal(encrypted);
    }

}
