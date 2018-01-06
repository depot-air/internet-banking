package com.dwidasa.interlink.utility;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author prayugo
 */
public final class Util {

    // -- character encoding used
    public static final String Encoding = "ISO8859_1";

    /* ---------------------------------------------------------------------- */
    /* Convert hexadecimal into byte array.                                   */
    /* ---------------------------------------------------------------------- */
    public static byte[] hex2Byte( String h ) {
    
        if( ( h.length() % 2 ) != 0 ) {
        
            h = "0" + h;
        }

        int l    = ( int ) h.length() / 2;

        byte[] r = new byte[ l ];

        for( int i = 0, j = 0, k = h.length(); i < k; i += 2, j++ ) {
        
            r[ j ] = Short.valueOf( h.substring( i, i+2 ),16 ).byteValue();
        }

        return r;
    }

    /* ---------------------------------------------------------------------- */
    /* Convert hexadecimal into ascii string                                  */
    /* ---------------------------------------------------------------------- */
    public static String hex2String( String h ) {
    
        return ( new String( hex2Byte( h ) ) );
    }

    /* ---------------------------------------------------------------------- */
    /* Convert byte array into hexadecimal string.                            */
    /* ---------------------------------------------------------------------- */
    public static String byte2Hex( byte[] b ) {
    
        StringBuffer sbuf = new StringBuffer();

        for( int i = 0, n = b.length; i < n; i++ ) {
        
            byte hiByte = ( byte ) ( ( b[ i ] & 0xF0 ) >> 4 );
            byte loByte = ( byte )   ( b[ i ] & 0x0F );

            sbuf.append( Character.forDigit( hiByte, 16 ) );
            sbuf.append( Character.forDigit( loByte, 16 ) );
        }

        return sbuf.toString();
    }

    /* ---------------------------------------------------------------------- */
    /* Convert ascii string into hexadecimal string.                          */
    /* ---------------------------------------------------------------------- */
    public static String String2Hex( String s ) {
    
    	return ( byte2Hex( s.getBytes(  ) ) );
    }

    /* ---------------------------------------------------------------------- */
    /* Convert ascii int into binary string representation                    */
    /* ---------------------------------------------------------------------- */
    public static String str2bcd( int argInt ) {
    
        try {
        
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            o.write( argInt >> 8 );
            o.write( argInt );

            return o.toString( Util.Encoding );
        }
        catch( UnsupportedEncodingException e ) {
        
            e.printStackTrace();
        }
        
        return "0";
    }
    
    /* ---------------------------------------------------------------------- */
    /* Convert binary int into ascii int representation                       */
    /* ---------------------------------------------------------------------- */
    public static int bcd2str( String argInt ) {
    
        try {
        
            byte[] b = argInt.getBytes( Util.Encoding );

            return ( ( ( ( int ) b[ 0 ] ) & 0xFF ) << 8 ) | ( ( ( int ) b[ 1 ] ) & 0xFF );
        }
        catch( UnsupportedEncodingException e ) {
        
            e.printStackTrace();
        }
        
        return 0;
    }

    /* ---------------------------------------------------------------------- */
    /* Convert binary int into ascii int representation                       */
    /* ---------------------------------------------------------------------- */
    public static int bcd2str( byte[] argInt ) {
    
        try {
        
            return ( ( ( ( int ) argInt[ 0 ] ) & 0xFF ) << 8 ) | ( ( ( int ) argInt[ 1 ] ) & 0xFF );
        }
        catch( Exception e ) {
        
            e.printStackTrace();
        }
        
        return 0;
    }

    /* ---------------------------------------------------------------------- */
    /* pad the string with specified pad character and length in total        */
    /* ---------------------------------------------------------------------- */
    public static String pad( String m, String pos, String with, int total ) {
    
    	if( m.length() < total ) {
    		
    		StringBuffer s = new StringBuffer( m );
    		
    		while( s.length() < total ) {
    		
    			if( "L".equals( pos ) ) {
    				s.insert( 0, with );
    			}
    			else if( "R".equals( pos ) ) {
    				s.append( with );
    			}
    		}
    		return s.toString();
    	}
    	else {
    		if( "L".equals( pos ) ) {
    			
    	    	return m.substring( m.length() - total );
    		}
    		else if( "R".equals( pos ) ) {
    	    	
    			return m.substring( 0, total );
    		}
    	}
    	
    	return m.substring( 0, total );
    }

    public static String stan() {
		
		 try {
		        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");

		        int myInt = sr.nextInt();
				 
		        int randInt = Math.abs( myInt / 10000 );
		        
		        String stan = "000000" + String.valueOf( randInt );
		        
		        return stan.substring( stan.length() - 6 );
		    }
    		catch( NoSuchAlgorithmException e ) {

    		} 
		 
		 return "987654";
	}
    
    public static String reference() {
    	
    	String tm = String.valueOf( System.currentTimeMillis() );
    	
    	return ( tm.substring( tm.length() - 6 ) + Util.stan() );
    }
    
}
