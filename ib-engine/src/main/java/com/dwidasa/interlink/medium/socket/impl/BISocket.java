package com.dwidasa.interlink.medium.socket.impl;

import com.dwidasa.interlink.medium.socket.SocketHandler;
import com.dwidasa.interlink.utility.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * implementation with binary message length 2 characters
 * message length include header length (include)
 * message length : ( message length + data )
 *  
 * @author prayugo
 */
public class BISocket implements SocketHandler {

	private Socket socket;
	
    private DataInputStream dis;
    private DataOutputStream dos;
    
    /**
     * start receiving any messages in a communication buffer
     * @return return any messages received including blank message
     * @throws Exception throw exception whenever exception occurred
     */
    @Override
    public String receive() throws Exception {
    
    	byte[] b = new byte[2];
    	byte[] t = new byte[2];
		int ctr = dis.read( b );
		
		if( ctr > 0 ) {
		
			// -- twist message length
			t[0] = b[1];
			t[1] = b[0];
			
			int l = Util.bcd2str( t );
			byte[] d = new byte[ l-2 ];

			dis.read( d );
			return ( new String( d ) );
		}
		else {
		
			throw new Exception( "Connection was disconnected." );
		}
    }

    /**
     * transmit a message to the external application
     * @param argMsg a message to be sent to the external application
     * @return always return true when no exception fired
     * @throws Exception any exception will be thrown
     */
    @Override
    public boolean transmit( String argMsg ) throws Exception {
    
    	String ml = Util.str2bcd( argMsg.length() + 2 );
    	byte[] mb = ml.getBytes( Util.Encoding );
    	byte[] tw = new byte[2];
    	
    	// -- twist message length bytes
    	tw[0] = mb[1];
    	tw[1] = mb[0];
    	
    	dos.write( tw );
    	dos.write( argMsg.getBytes( Util.Encoding ) );
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
    	
    	dis = new DataInputStream( s.getInputStream() );
    	dos = new DataOutputStream( s.getOutputStream() );
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
