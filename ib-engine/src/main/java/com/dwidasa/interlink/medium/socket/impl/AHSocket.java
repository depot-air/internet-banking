package com.dwidasa.interlink.medium.socket.impl;

import com.dwidasa.interlink.medium.socket.SocketHandler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;

/**
 * socket implementation with ascii header length 4 characters
 *  
 * @author prayugo
 */
public class AHSocket implements SocketHandler {

	private Socket socket;
	
    private BufferedInputStream dis;
    private BufferedOutputStream dos;

    /**
     * start receiving any messages in a communication buffer
     * @return return any messages received including blank message
     * @throws Exception throw exception whenever exception occurred
     */
    @Override
    public String receive() throws Exception {

		byte[] b = new byte[ 6 ];
		int ctr = dis.read( b );
		
		if( ctr > 0 ) {
		
			int l = Integer.parseInt( new String( b ) );
			byte[] d = new byte[ l ];
			
			int totalRead = 0;
			int readCount = dis.read( d );
			
			totalRead += readCount;
			StringBuffer sb = new StringBuffer( new String( d, 0, readCount ) );
			
			while( totalRead < l ) {
				
				byte[] temp = new byte[ l - totalRead ];
				
				readCount = dis.read( temp );
				totalRead += readCount;
				
				sb.append( new String( temp, 0, readCount ) );
			}
			
			return sb.toString();
		}
		else {
		
			throw new Exception( "connection was disconnected." );
		}
    }

    /**
     * transmit a message to the external application
     * @param Msg a message to be sent to the external application
     * @return always return true when no exception fired
     * @throws Exception any exception will be thrown
     */
    @Override
    public boolean transmit( String Msg ) throws Exception {
    
		// -- write a single message.
		String aMsg = "000000" + Msg.length();
		       aMsg = aMsg.substring( aMsg.length() - 6 ) + Msg;
		       
    	dos.write( aMsg.getBytes() );
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
    	
    	dis = new BufferedInputStream( s.getInputStream() ); 
    	dos = new BufferedOutputStream( s.getOutputStream() );
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

