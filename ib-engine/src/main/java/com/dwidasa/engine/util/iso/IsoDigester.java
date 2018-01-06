package com.dwidasa.engine.util.iso;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.service.IsoBitmapService;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.util.ReferenceGenerator;
import com.dwidasa.interlink.dao.InterlinkDao;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.GenericPackager;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * utility for transforming internal internet banking data
 * T_Transaction to ISO 8583 message and vice versa.
 * Since this is a singleton class, it must be registered into
 * SpringFramework through
 *
 * <bean id="isoDigester" class="com.dwidasa.ib.utility.IsoDigester" factory-method="getInstance" init-method="load" />
 *
 * usages
 *  1. Create reference through ServiceLocator.getService( "isoDigester" );
 *  2. Call digestRequest( TTransaction tt ) to create ISO 8583 message
 *  3. Call TTransaction digestResponse( String m ) to extract ISO 8583 message
 *
 * @author prayugo
 * </p>
 */
public final class IsoDigester {

	/** template message */
	private ISOMsg oMsg;

    /** singleton instance of IsoDigester object */
    private static final IsoDigester instance = new IsoDigester();

    /** bitmap information used for creating iso message */
    private Map< String, IsoBitmap > bitmap = new HashMap< String, IsoBitmap >();

    private static Logger logger = Logger.getLogger( IsoDigester.class );

    /**
     * default [ private ] constructor for singleton object
     */
	private IsoDigester()
	{
		try {
			load();
		}
		catch( Exception e ) {
            e.printStackTrace();
			logger.error( "error when instantiates IsoDigester. " + e.getMessage(), e );
		}
	}

    /**
     * instantiate IsoDigester object through static access method
     * @return return the single instance of IsoDigester object
     */
    public static IsoDigester getInstance()
    {
        return instance;
    }

    /**
     * prevent the IsoDigester object for cloning. always throw
     * CloneNotSupportedException, it's mean that IsoDigester can not be cloned
     * @return does not return anything, just simple throw exception
     * @throws CloneNotSupportedException
     */
    protected Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException( "IsoDigester cannot be cloned." );
    }

    public void load() throws Exception
	{
        InterlinkDao interlinkDao = (InterlinkDao) ServiceLocator.getService("interlinkDao");
//        String packager = "/opt/iso-packager.xml";
        String packager = interlinkDao.getInterlink().getIsoPackager();

    	// -- init iso packager
    	initPackager( packager );

        IsoBitmapService isoBitmapService = (IsoBitmapService) ServiceLocator.getService("isoBitmapService");

    	// -- load bitmap and put into internal container : bitmap
    	List< com.dwidasa.engine.model.IsoBitmap > bmp = isoBitmapService.getAllWithTransactionType();

    	for( com.dwidasa.engine.model.IsoBitmap o : bmp ) {
    		IsoBitmap b = new IsoBitmap();

    		b.setTxType( o.getTransactionType().getTransactionType() );
    		b.setMti( o.getMti() );
    		b.setBitmap( o.getBitmap() );
    		b.initCustom( o.getCustom() );

    		bitmap.put( b.getTxType(), b );
    	}
	}

    /**
     * init iso packager for iso message utility class
     * @param packager packager file name
     * @throws Exception
     */
    private void initPackager( String packager ) throws Exception
    {
    	try {
    		ISOPackager p = new GenericPackager( packager );

    		oMsg = new ISOMsg();
    		oMsg.setPackager( p );
    	}
    	catch( Exception e ) {
            e.printStackTrace();
    		throw new Exception( "unable to set iso packager for packager : " + packager, e );
    	}
    }

	/**
	 * generate 12 characters length retrieval reference number
	 * @return generated retrieval reference number
	 */
	public synchronized String reference()
	{
        return ReferenceGenerator.generate();
	}

	/**
     * create iso8583 message to be sent to core application
     * @param tt internal transaction data
     * @return iso8583 message based on internal data
     * @throws Exception
     */
	public String digestRequest( Transaction tt ) throws Exception
	{
		IsoBitmap b = bitmap.get( tt.getTransactionType() );
		ISOMsg Msg = ( ISOMsg ) oMsg.clone();
        tt.setReferenceNumber( reference() );
		Msg = IsoHelper.populate( Msg, tt, b );
		
		return new String( Msg.pack() );
	}

	public String digestRequestJson( Transaction tt ) throws Exception
	{
		IsoBitmap b = bitmap.get( tt.getTransactionType() );
		b.setMti("JSRQ");
		b.setBitmap("2,3,4,7,11,12,13,18,29,37,39,41,56,102,48");
		if (tt.getFreeData4() == null) tt.setFreeData4("");
		ISOMsg Msg = ( ISOMsg ) oMsg.clone();
        tt.setReferenceNumber( reference() );
		Msg = IsoHelper.populate( Msg, tt, b );
		String res = b.getMti();
		//2,3,4,7,11,12,13,18,29,37,39,41,56,102,48
		//19+6+12+7+6+14+9+12+59 
		
		//bit 2 cardNumber, 19 char, left align
		//bit 3 tt.getTransactionType() + tt.getFromAccountType() + tt.getToAccountType();
     	//bit 4 amount 12 digit, right align
		//bit 7 MMddhhmmss
		//bit 11 stan 6 digit
		
     	//bit 12 hhmmss
        //bit 13 MMdd
     	//bit 18 merchantType 4 digit
		//bit 29 fee, "C00000000", 9 digit
		//bit 37 referenceNumber, 000000913929, 12 digit
		
		//bit 39 responseCode 2 digit
		//bit 41 terminal Id 8 digit, left align
		//bit 56 feature code, 30 digit, left align
		//bit 102 accountNumber, 19 char, left align
		
		res += StringUtils.rightPad(Msg.getString("2").trim(), 19, " ");
		res += StringUtils.rightPad(Msg.getString("3"), 6, " ");
		res += StringUtils.leftPad(Msg.getString("4"), 12, "0");
		res += StringUtils.leftPad(Msg.getString("7"), 10, " ");
		res += StringUtils.leftPad(Msg.getString("11"), 6, "0");
		res += StringUtils.rightPad(Msg.getString("12"), 6, " ");
		res += StringUtils.rightPad(Msg.getString("13"), 4, " ");
		res += StringUtils.rightPad(Msg.getString("18"), 4, " ");
		res += StringUtils.rightPad(Msg.getString("29"), 9, " ");
		res += StringUtils.leftPad(Msg.getString("37"), 12, "0");
		res += StringUtils.leftPad(Msg.getString("39"), 2, " ");
		res += StringUtils.rightPad(Msg.getString("41"), 8, " ");
		res += StringUtils.rightPad(Msg.getString("56").trim(), 30, " ");
		res += StringUtils.rightPad(Msg.getString("102").trim(), 19, " ");
		res += Msg.getString("48");
		
		return res;
	}

	
    /**
     * create iso8583 message to be sent to core application
     * @param tt internal transaction data
     * @param isoMsg original iso msg
     * @return iso8583 message based on internal data
     * @throws Exception
     */
    public String digestRequest( Transaction tt, String isoMsg ) throws Exception
    {
        IsoBitmap b = bitmap.get( tt.getTransactionType() );
        ISOMsg Msg = ( ISOMsg ) oMsg.clone();

        ISOMsg orginalMsg = (ISOMsg) oMsg.clone();
        orginalMsg.unpack(isoMsg.getBytes());

        Msg = IsoHelper.populate( Msg, orginalMsg, tt, b );
        return new String( Msg.pack() );
    }

	/**
	 * extract received iso8583 message and update internal transaction
	 * data with information received from core application
	 * @param tt internal transaction data
	 * @param m received iso8583 message
	 * @return updated internal transaction data
	 * @throws Exception
	 */
	public Transaction digestResponse( Transaction tt, String m ) throws Exception
	{
		String mti = m.substring(0, 4);
		if (mti.equals("JSRQ") || mti.equals("JSRS") ) {
			//JSRS6278790011900001   9e1000000000000000032419064304160819064303246014C0000000000000091557500I1516288VOLT.FLY.AVA,0002,0001        1574087868              {}
//			System.out.println("m=" + m);
			DateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
			DateFormat yearFormat = new SimpleDateFormat("yyyy");
			Date today = new Date();
			
			String MM = m.substring(41, 43);
			String dd = m.substring(43, 45);
			String hhmmsss = m.substring(45, 51);
			String yyyy = yearFormat.format(today);
			Date transactionDate = sdf.parse(dd + MM + yyyy + hhmmsss);
			
			tt.setMti(mti);
			tt.setCardNumber(m.substring(4, 23).trim());
			tt.setTransactionType(m.substring(23, 25));
			tt.setTransactionAmount(BigDecimal.valueOf(Long.parseLong(m.substring(29, 41))));
			tt.setTransactionDate(transactionDate);
			tt.setStan(BigDecimal.valueOf(Long.parseLong(m.substring(51, 57))));
			tt.setMerchantType(m.substring(67, 71));
			tt.setFeeIndicator(m.substring(71,72));
			tt.setFee(BigDecimal.valueOf(Long.parseLong(m.substring(72, 80))));
			tt.setReferenceNumber(m.substring(80, 92));
			tt.setResponseCode(m.substring(92, 94));
			tt.setTerminalId(m.substring(94, 102));
			//feature name
			tt.setFromAccountNumber(m.substring(132, 151).trim());
			tt.setFreeData1(m.substring(151).trim());
			return tt;
		}
		ISOMsg Msg = ( ISOMsg ) oMsg.clone();		
		Msg.unpack( m.getBytes() );

		IsoHelper.populate( tt, Msg );

		return tt;
	}
	
}

