package com.dwidasa.interlink.digester;

import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.interlink.Digester;
import com.dwidasa.interlink.dao.InterlinkDao;
import com.dwidasa.interlink.model.MInterlink;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.GenericPackager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author prayugo
 */
public class IsoDigester implements Digester {

	private ISOMsg template;

    /**
     * default constructor for class IsoDigester
     */
    public IsoDigester() { 
    
    }
    
	/**
	 * determine the key or unique identifier of specified message
	 * @param Msg interface message contains of business process data
	 * @return the unique identifier of specified message
	 */
	public String getKey( String Msg ) {
	
    	try {
    	
        	ISOMsg m = ( ISOMsg ) template.clone();

        	m.unpack( Msg.getBytes() );
/*
			m.dump( System.out, "dump [unpack] message : " );
*/
        	String B03 = m.getString(  3 );
        	String B07 = m.getString(  7 );
        	String B11 = m.getString( 11 );
        	String B37 = m.getString( 37 );
        	String B41 = m.getString( 41 );
        	       B41 = ( B41 == null ) ? "" : B41;
        	
        	StringBuffer o = new StringBuffer();
        	o.append( B03 ).append( B07 ).append( B11 ).append( B37 ).append( B41 );
        	
        	return o.toString();
    	}
    	catch( Exception e ) {
    	
            e.printStackTrace();
    	}
    	
		return null; 
	}
	
	/**
	 * prepare configuration and start the service
	 * @return just return true if starting process was success and vice versa
	 */
	public boolean startup() {
	
    	try {
    	
	    	InterlinkDao dao = ( InterlinkDao ) ServiceLocator.getService( "interlinkDao" );
	    	MInterlink model = dao.getInterlink();

    		ISOPackager p = new GenericPackager( model.getIsoPackager() );
//            ISOPackager p = new GenericPackager( "/opt/iso-packager.xml" );

    		template = new ISOMsg();
    		template.setPackager( p );
    		
    		return true;
    	}
    	catch( Exception e ) {
    	
    		e.printStackTrace();
    	}
    	
		return false; 
	}


	/**
	 * create a new echo message to be sent.
	 * echo message used to monitor network connection
	 * @return new generate echo message
	 */
	public String crtEcho() {
	
        SimpleDateFormat df = new SimpleDateFormat( "MMddhhmmss" );

		try {
		
			ISOMsg Msg = ( ISOMsg ) template.clone();
			Calendar c = Calendar.getInstance();
			
			String td = df.format( new Date() );
			String st = td + c.get( Calendar.DAY_OF_YEAR );
			
			Msg.setMTI( "0800" );
			Msg.set(  7, td );
			Msg.set( 11, st.substring( st.length()-6 ) );
			Msg.set( 70, "301" );
			
			return new String( Msg.pack() );
		}
		catch( Exception e ) { 
		
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * create response for request echo message
	 * @param Msg incoming message received
	 * @return response message for echo request
	 */
	public String crtEchoReply( String Msg ) {
	
		try {
		
        	ISOMsg m = ( ISOMsg ) template.clone();

        	m.unpack( Msg.getBytes() );
/*
			m.dump( System.out, "dump [unpack] message : " );
*/
			m.setResponseMTI();
			m.set( 39, "00" );

			return new String( m.pack() );
		}
		catch( Exception e ) {
		
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * check whether this is an mti of network management
	 * @param argMsg internal message representation
	 * @return true if mti is an mti of network management
	 */
	public boolean isNetwork( String argMsg ) {
		
		String msgc = argMsg.substring( 0, 2 );
		
		return "08".equals( msgc );
	}
	
	/**
	 * check if Mti indicates a request message
	 * @param argMsg internal message representation
	 * @return true if Mti indicates a request message
	 */
	public boolean isRequest( String argMsg ) {
		
		String msgf = argMsg.substring( 2, 3 );
		
		return ( msgf.equals( "0" ) || msgf.equals( "2" ) );
	}

}
