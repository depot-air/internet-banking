package com.dwidasa.engine.model.view;

/**
 * Object providing token credential for authentication.
 *
 * @author rk
 */
public class TokenView {
    /**
     * Generated challenge.
     */
    private String challenge;
    /**
     * Input token.
     */
    private String token;

    private String encryptedToken;

	public TokenView() {
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncryptedToken() {
        return encryptedToken;
    }

    public void setEncryptedToken(String encryptedToken) {
        this.encryptedToken = encryptedToken;
    }

}
