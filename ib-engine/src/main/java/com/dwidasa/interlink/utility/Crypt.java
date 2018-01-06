package com.dwidasa.interlink.utility;

/**
 * @author prayugo
 */
public class Crypt 
{
	public String encrypt( String argData )
	{
		int l = argData.length();
		char[] x = argData.toCharArray();
		int [] y = new int[ l ];
		
		for( int i = 0; i < l; i++ )
		{
			y[ i ] = getIndex( String.valueOf( x[ i ] ) );
		}
		
		for( int i = 0; i < l; i++ )
		{
			y[ i ] = y[ i ] + esmk[ i ];
			
			if( y[i] > 62 )
			{
				y[i] = y[i] - 62;
			}
		}
		
		StringBuffer sb = new StringBuffer();
		for( int i = 0; i < l; i++ )
		{
			sb.append( ct[ y[i] ] );
		}			
		
		return sb.toString();
	}
	
	public String decrypt( String argData )
	{
		int l = argData.length();
		char[] x = argData.toCharArray();
		int [] y = new int[ l ];
		
		for( int i = 0; i < l; i++ )
		{
			y[ i ] = getIndex( String.valueOf( x[ i ] ) );
		}
		
		for( int i = 0; i < l; i++ )
		{
			y[ i ] = y[ i ] - esmk[ i ];
			
			if( y[i] < 0 )
			{
				y[i] = y[i] + 62;
			}
		}
		
		StringBuffer sb = new StringBuffer();
		for( int i = 0; i < l; i++ )
		{
			sb.append( ct[ y[i] ] );
		}			
		
		return sb.toString();
	}
	
	private int getIndex( String argData )
	{
		for( int i = 0; i < 63; i++ )
		{
			if( argData.equals( ct[ i ] ) )
			{
				return i;
			}
		}
		
		return 0;
	}
	
	private String[] ct = 
	{ 
			"o","b","E","c","Y","M","K","d","T","q",
			"U","y","H","C","P","9","a","w","u","O",
			"p","8","Q","5","2","m","W","1","Z","g",
			"3","f","I","X","s","G","i","S","D","0",
			"t","l","k","N","6","F","z","4","r","h",
			"J","B","x","j","V"," ","e","R","7","v",
			"A","n","L" 
	};

	/*  e c  t  s  e  r  a  m  a  s  t  e  r  k  e  y */
	/* 57 4 41 35 57 49 17 26 17 35 41 57 49 43 57 12 */
	
	private int[] esmk = { 57,4,41,35,57,49,17,26,17,35,41,57,49,43,57,12 };

	public static void main( String[] args )
	{
		String a = "prayugorajatidur";
		String r = null;
		
		Crypt e = new Crypt();
		
		r = e.encrypt( a );
		System.out.println( "a : " + a );
		System.out.println( "e : " + r );
		
		r = e.decrypt( r );
		System.out.println( "d : " + r );
	}
}
