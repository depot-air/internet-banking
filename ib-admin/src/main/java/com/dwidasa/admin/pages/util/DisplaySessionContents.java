package com.dwidasa.admin.pages.util;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

import com.dwidasa.admin.common.GeneralUtil;

/**
 * This page is based on the portion of Tapestry's default ExceptionReport page that handles the session.
 */

public class DisplaySessionContents {

    @Property
    private String attributeName;

    @Inject
    private Request request;

    public boolean getHasSession() {
        return request.getSession(false) != null;
    }

    public Session getSession() {
        return request.getSession(false);
    }
    
    public int getAttributeSize() {
    	try {
	    	java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
		
			Object obj = getSession().getAttribute(attributeName);
			oos.writeObject(obj);		
			int size = baos.size();
			oos.close();
			baos.close();
			return size;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return -1;
    	}

    }
    
    public int getTotalSessionSize() {
    	return GeneralUtil.getSessionSize(getSession());
    }

    public Object getAttributeValue() {
        return getSession().getAttribute(attributeName);
    }

}

