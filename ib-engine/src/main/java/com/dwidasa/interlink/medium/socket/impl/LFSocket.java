package com.dwidasa.interlink.medium.socket.impl;

import com.dwidasa.interlink.medium.socket.SocketHandler;

import java.io.*;
import java.net.Socket;

/**
 * socket implementation with \n delimiter
 * 
 * @author prayugo
 */
public class LFSocket implements SocketHandler {

	private Socket socket;
	
	private BufferedReader dis;
    private BufferedWriter dos;

    /**
     * start receiving any messages in a communication buffer
     * @return return any messages received including blank message
     * @throws Exception throw exception whenever exception occurred
     */
    @Override
    public String receive() throws Exception {
    
    	String data = dis.readLine();
    	
    	if( data == null ) {
    	
    		throw new Exception( "Connection was disconnected." );
    	}
    	
    	return data;
    }

    /**
     * transmit a message to the external application
     * @param Msg a message to be sent to the external application
     * @return always return true when no exception fired
     * @throws Exception any exception will be thrown
     */
    @Override
    public boolean transmit( String Msg ) throws Exception {
    
    	dos.write( Msg + "\n" );       	// -- Msg = ( argMsg + "\n" );
    	dos.flush();

    	return true;
    }

    /**
     * set the connected socket into implementation class
     * @param s instance of connected socket
     * @throws Exception
     */
    @Override
    public void setSocket( Socket s ) throws Exception {
    	
    	socket = s;

		dis = new BufferedReader( 
				new InputStreamReader( socket.getInputStream() ) );

    	dos = new BufferedWriter( 
    			new OutputStreamWriter( socket.getOutputStream() ) );
    }


    /**
     * close the connection
     */
    @Override
    public void shutdown() {
    	
    	try {
    		
    		socket.close();
    	}
    	catch( Exception e ) {

    	}

    	socket = null; dis = null;  dos = null;
    }
    
}
