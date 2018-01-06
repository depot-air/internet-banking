package com.dwidasa.interlink.medium.socket.impl;

import com.dwidasa.interlink.medium.socket.SocketHandler;
import com.dwidasa.interlink.utility.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * implementation with binary message length 2 characters
 * message length does not include header length (exclude)
 * message length : ( data )
 *  
 * @author prayugo
 */
public class BESocket implements SocketHandler { 

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
		int ctr = dis.read( b );
		
		if( ctr > 0 ) {
		
			int l = Util.bcd2str( b ); 

			byte[] d = new byte[ l ];

			dis.read( d );
			
			return ( new String( d ) );
		}
		else {
		
			throw new Exception( "Connection was disconnected." );
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
    
    	String ml = Util.str2bcd( Msg.length() );

    	byte[] dl = ml.getBytes( Util.Encoding );
    	byte[] dm = Msg.getBytes( Util.Encoding );
    	
    	byte[] dt = new byte[ dl.length + dm.length ];
    	
    	// -- src, pos, dst, pos, len
    	System.arraycopy( dl, 0, dt, 0, dl.length ); 
    	System.arraycopy( dm, 0, dt, dl.length, dm.length ); 
    	
    	dos.write( dt );
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
