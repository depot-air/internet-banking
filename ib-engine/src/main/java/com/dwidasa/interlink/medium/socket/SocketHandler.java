package com.dwidasa.interlink.medium.socket;

import java.net.Socket;

/**
 * @author prayugo
 */
public interface SocketHandler {

    /**
     * start receiving any messages in a communication buffer
     * @return return any messages received including blank message
     * @throws Exception throw exception whenever exception occurred
     */
    public String receive() throws Exception;

    /**
     * transmit a message to the external application
     * @param Msg a message to be sent to the external application
     * @return always return true when no exception fired
     * @throws Exception any exception will be thrown
     */
    public boolean transmit( String Msg ) throws Exception;
    
    /**
     * set the connected socket into implementation class
     * @param s instance of connected socket
     * @throws Exception
     */
    public void setSocket( Socket s ) throws Exception;

    /**
     * close the connection
     */
    public void shutdown();    

}
