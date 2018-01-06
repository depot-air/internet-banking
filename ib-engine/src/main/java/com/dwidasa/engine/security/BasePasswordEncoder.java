package com.dwidasa.engine.security;


import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BasePasswordEncoder implements PasswordEncoder {
    private final String algorithm;

    /**
     * Convenience constructor for specifying the algorithm
     * @param algorithm
     * @throws IllegalArgumentException if an unknown
     */
    public BasePasswordEncoder(String algorithm) throws IllegalArgumentException {
        this.algorithm = algorithm;
        //Validity Check
        getMessageDigest();
    }

    /**
     * Encodes the rawPass using a MessageDigest.
     * @param rawPass The plain text password
     * @return Hex string of password digest (or base64 encoded string if encodeHashAsBase64 is enabled.
     */
    public String encodePassword(String rawPass) {
        MessageDigest messageDigest = getMessageDigest();

        byte[] digest;

        try {
            digest = messageDigest.digest(rawPass.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported!");
        }

        digest = messageDigest.digest(digest);
        return Hex.encodeHexString(digest);
    }

    /**
     * Get a MessageDigest instance for the given algorithm.
     * @return MessageDigest instance
     * @throws IllegalArgumentException if NoSuchAlgorithmException is thrown
     */
    protected final MessageDigest getMessageDigest() throws IllegalArgumentException {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm [" + algorithm + "]");
        }
    }

    /**
     * Takes a previously encoded password and compares it with a rawpassword and encoding that value.
     * @param encPass previously encoded password
     * @param rawPass plain text password
     * @return true or false
     */
    public boolean isPasswordValid(String encPass, String rawPass) {
        String pass1 = "" + encPass;
        String pass2 = encodePassword(rawPass);

        return pass1.equals(pass2);
    }

    public String getAlgorithm() {
        return algorithm;
    }
}
