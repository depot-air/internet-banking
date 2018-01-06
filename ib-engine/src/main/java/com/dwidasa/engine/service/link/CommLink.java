package com.dwidasa.engine.service.link;

/**
 * Interface to define communication link to other system. An implementation
 * example of this interface is messaging system between our application to MX.
 * Source objects to be passed are sent through constructor.
 *
 * @author rk
 */
public interface CommLink {
    /**
     * Send message to other system.
     */
    public void sendMessage();

    /**
     * Reply message from other system.
     */
    public void replyMessage();
    
    
    /*
     * Send message 
     */
    public void sendMessage(boolean waitResponse);
    
}
