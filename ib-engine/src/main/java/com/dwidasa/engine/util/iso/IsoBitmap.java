package com.dwidasa.engine.util.iso;

import java.util.HashMap;
import java.util.Map;

/**
 * container for iso bitmap and default field definition
 *
 * @author prayugo
 */
public class IsoBitmap {

	private String mti;
	private String txType;
	private String bitmap;
	private Map< String, String > custom;

	public String getMti() {
		return mti;
	}

	public void setMti(String mti) {
		this.mti = mti;
	}

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}

	public String getBitmap() {
		return bitmap;
	}

	public void setBitmap(String bitmap) {
		this.bitmap = bitmap.trim();
	}

	public Map<String, String> getCustom() {
		return custom;
	}

	public void setCustom(Map<String, String> custom) {
		this.custom = custom;
	}

	/**
	 * first time initialization of custom data
     *  - format data in custom field must follow this format
     *  - bit(#):=value{#}bit(#):=value ... {#}bit(#):=value
	 * @param d custom data to be extracted
	 */
	public void initCustom( String d ) {
        custom = new HashMap<String, String>();

		if( d != null && ( d.trim().length() > 3 ) )
		{
	    	String[] a = d.split( "\\{#}" );
	    	for( String b : a )
	    	{
	    		String[] c = b.split( "\\:=" );
    			custom.put( c[0].trim(), c[1].trim() );
	    	}
		}
	}

	/**
	 * get the field info from the inner container
	 * @param k key to point the data
	 * @return data keep in keyed
	 */
	public String getCustomItem( String k ) {
		String dat = custom.get( k );
		return ( dat == null ) ? "" : dat;
	}

	/**
	 * set the field into inner container
	 * @param k key to point the data
	 * @param v data value
	 */
	public void setCustomItem( String k, String v ) {
		custom.put( k, v );
	}
}
