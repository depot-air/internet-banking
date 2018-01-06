package com.dwidasa.interlink.medium.socket;

/**
 * @author prayugo
 */
public interface Connection {

    /** connection as client socket */
    public static final int Client = 0;

    /** connection as server socket */
    public static final int Server = 1;

    /**
     * open the connection with external application
     * @return always return true whenever operation succeed and vice versa
     */
    public boolean connect();

    /**
     * implemented method will be automatically called by Transport
     * whenever a connection to external application just connected
     */
    public void onConnect();

    /**
     * close the connection with external application
     */
    public void disconnect();

    /**
     * implemented method will be automatically called by Transport
     * whenever an exception that caused connection to be closed to the
     * external application occurred
     */
    public void onDisconnect();

    /**
     * send a message to external application that connected with our system.
     * @param Msg interface message contains of business process data
     * @return always return true whenever operation succeed and vice versa
     */
    public boolean transmit( String Msg );
    
}





