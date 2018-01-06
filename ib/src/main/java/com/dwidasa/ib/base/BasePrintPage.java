package com.dwidasa.ib.base;


public class BasePrintPage {	
		
	public String getDashIfEmpty(String str) {
		if (str !=  null && str.trim().length() > 0) {
			return str;
		}
		return "-";
    }
}
