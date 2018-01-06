package com.dwidasa.interlink.utility;

import com.dwidasa.interlink.Digester;
import com.dwidasa.interlink.Listener;
import com.dwidasa.interlink.medium.Transporter;
import com.dwidasa.interlink.model.MInterlink;
import com.dwidasa.interlink.model.MSocket;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;

/**
 * @author prayugo
 */
public final class Setup 
{
	/** singleton instance of ServiceCaller object */
    private static final Setup instance = new Setup();
    
    /** keep the status of parsing configuration file */
    private boolean status = true;
    
	/** instance of InterParser used to retrieve all interlink configuration */
	private InterParser parser;
	
	/** module logger uses log4j component */
    private Logger logger = Logger.getLogger( Setup.class );
    
    /**
     * default [ private ] constructor for singleton object
     */
	private Setup()
	{
		parser = new InterParser();
		status = parser.parse();
	}
	
    /**
     * instantiate Setup object through static access method
     * @return return the single instance of Setup object
     */
    public static Setup getInstance()
    {
        return instance;
    }

    /**
     * prevent the Setup object for cloning. always throw
     * CloneNotSupportedException, it's mean that Setup can not be cloned
     * @return does not return anything, just simple throw exception
     * @throws CloneNotSupportedException
     */
    protected Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException( "Setup cannot be cloned." );
    }

    /**
     * get the status of process parsing configuration file
     * @return just return true is parsing task was success and vice versa
     */
    public boolean getStatus() { return status; }
    
	/**
	 * initialize the message transporter class
	 * @return an instance of which implements Transporter interface
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Transporter getTransporter( Listener argListener ) 
	{ 
		MInterlink o = getInterObject();
		String clazz = o.getTransporterName().trim();
		
		try 
		{
			Class cs = Class.forName( clazz );

			Constructor cr = cs.getConstructor( new Class[]{ Listener.class } );
        	Transporter t = ( Transporter ) cr.newInstance( new Object[] { argListener } );
			
        	// -- starting the transporter
        	t.startup();
			
			return t;
		} 
		catch( Exception e )
		{
			logger.error( "unable to instantiate message transporter : " + clazz, e );
		}
		
		return null;
	}
	
	/**
	 * initialize the message digester class
	 * @return an instance of which implements Digester interface
	 */
	public Digester getDigester() 
	{
		MInterlink o = getInterObject();
		String clazz = o.getDigesterName().trim();
		
		try 
		{
			Digester d = ( Digester ) Class.forName( clazz ).newInstance();
			d.startup();
			
			return d;
		} 
		catch( Exception e )
		{
			logger.error( "unable to instantiate message digester : " + clazz, e );
		}
		
		return null;
	}

	/**
	 * get the main configuration of interlink
	 * @return object contain of interlink configuration
	 */
	public MInterlink getInterObject() { return parser.getInterObject(); }
	
	/**
	 * get the socket configuration of interlink
	 * @return object contain of socket interlink configuration
	 */
	public MSocket getSocketObject() { return parser.getSocketObject(); }

	/**
	 * get the monitor period for monitoring time out calling
	 * @return the monitor period for monitoring time out calling
	 */
	public long getMonitorPeriod() { return getInterObject().getMonitorPeriod(); }

	/**
	 * get the timeout period for for each message calling
	 * @return the timeout period for for each message calling
	 */
	public long getTimeoutPeriod() { return getInterObject().getTimeoutPeriod(); }

	/**
	 * get the iso8583 packager file name
	 * @return the iso8583 packager file name
	 */
	public String getIsoPackager() {
        return getInterObject().getIsoPackager();
//        return "C:/iso-packager.xml";
    }

	/**
	 * get the first position of the message key in the message
	 * @return the first position of the message key in the message
	 */
	public int getStartKeyPosition() { return getInterObject().getMessageKeyPosition(); }
	
	/**
	 * get the message key length in the message
	 * @return the message key length in the message
	 */
	public int getMessageKeyLength() { return getInterObject().getMessageKeyLength(); }

	/**
	 * get the element name of the message key in the message
	 * @return the element name of the message key in the message
	 */
	public String getMessageKeyElement() { return getInterObject().getMessageKeyElement(); }
	
	/**
	 * get the echo implementation status.
	 * @return true if we need echo message to monitor our connection
	 */
	public boolean isEchoApply() { return getInterObject().isEchoApply(); }
	
	/**
	 * get the period of echo message to be sent to other application
	 * @return the period of echo message sent
	 */
	public long getEchoPeriod() { return getInterObject().getEchoPeriod(); }
	
	/**
	 * get the message type format implemented in this interfacing
	 * @return the message type format ex. iso8583 etc.
	 */
	public String getMessageType() { return getInterObject().getMessageType(); }
}
