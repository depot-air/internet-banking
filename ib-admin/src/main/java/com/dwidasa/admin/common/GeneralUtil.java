package com.dwidasa.admin.common;

import org.apache.tapestry5.services.Session;

public class GeneralUtil {
	public static int getSessionSize(Session session) {
		int total = 0;
	
		try	{
			java.io.ByteArrayOutputStream baos;
			java.io.ObjectOutputStream oos;
		
			for (String name : session.getAttributeNames())
			{				
				baos = new java.io.ByteArrayOutputStream();
				oos = new java.io.ObjectOutputStream(baos);
				
				Object obj = session.getAttribute(name);
				oos.writeObject(obj);
				
				int size = baos.size();
				total += size;
				
				oos.close();
				baos.close();
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return total;
	}

	
	/**
	 * 
	 * @param camelCaseWord (i.e: userName)
	 * @return lower case with underscore (i.e: user_name)
	 */
	public static String getSqlName(String camelCaseWord) {
		if (camelCaseWord == null) return null;
		String result = camelCaseWord.trim();
		if (result.length() == 0) return "";
		result = result.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2");
		result = result.replaceAll("([a-z\\d])([A-Z])", "$1_$2");
		return result.toLowerCase();
	}
	
	
	public static String getRootCause(Throwable t) {
		Throwable cause = t;
		Throwable subCause = cause.getCause();
		while (subCause != null && !subCause.equals(cause)) {
			cause = subCause;
			subCause = cause.getCause();
		}
		return cause.getMessage();
	}
}
