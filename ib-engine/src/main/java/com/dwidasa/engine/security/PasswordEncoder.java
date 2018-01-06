package com.dwidasa.engine.security;

/**
 * Interface for performing authentication operations on a password.
 * Adapted from spring security (removing base64 and salt)
 */
public interface PasswordEncoder {
    /**
     * Encodes the specified raw password with an implementation specific algorithm.
     * @param rawPass raw password
     * @return encoded password
     */
    String encodePassword(String rawPass);

    /**
     * Validates a specified raw password against an encoded password.
     * @param encPass a pre-encoded password
     * @param rawPass a raw password to encode and compare against the pre-encoded password
     * @return true if the password valid else otherwise
     */
    boolean isPasswordValid(String encPass, String rawPass);
}