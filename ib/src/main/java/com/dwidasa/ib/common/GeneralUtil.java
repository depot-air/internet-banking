package com.dwidasa.ib.common;

import org.apache.tapestry5.services.Session;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class GeneralUtil {
	public static final DateTimeFormatter fmtDateTime = DateTimeFormat.forPattern("dd-MMM-yyyy HH:mm");
	public static final DateTimeFormatter fmtDateTimess = DateTimeFormat.forPattern("dd-MMM-yyyy HH:mm:ss");
	public static final DateTimeFormatter fmtTimess = DateTimeFormat.forPattern("HH:mm:ss");
	public static final DateTimeFormatter fmtDate = DateTimeFormat.forPattern("dd-MMM-yyyy");
	public static final DateTimeFormatter fmtYearMonthDate = DateTimeFormat.forPattern("yyyyMMdd");
	public static final DateTimeFormatter fmtYearMonthDateAnsi = DateTimeFormat.forPattern("yyyy-MM-dd");
	public static final DateTimeFormatter fmtDateTimeEncode = DateTimeFormat.forPattern("yyyyMMddHHmm");
	public static final DateTimeFormatter fmtInputDateTime = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
	public static final DateTimeFormatter fmtMonthYear = DateTimeFormat.forPattern("MM/YY");
	public static final DateTimeFormatter fmtYmdhms = DateTimeFormat.forPattern("yyyyMMddHHmmss");
	
	public static int getSessionSize(Session session) {
		int total = 0;
	
			java.io.ByteArrayOutputStream baos;
			java.io.ObjectOutputStream oos;
		
			for (String name : session.getAttributeNames())
			{
				try {
					baos = new java.io.ByteArrayOutputStream();
					oos = new java.io.ObjectOutputStream(baos);
					
					Object obj = session.getAttribute(name);
					oos.writeObject(obj);
					
					int size = baos.size();
					total += size;
					
					oos.close();
					baos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
	
		return total;
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
