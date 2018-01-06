package com.dwidasa.engine;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.dwidasa.engine.dao.ResponseCodeDao;
import com.dwidasa.engine.model.ResponseCode;
import com.dwidasa.engine.service.ServiceLocator;

/**
 * Defined business exception for internet banking application.
 *
 * @author dp
 */
@SuppressWarnings("serial")
@JsonIgnoreProperties({"cause", "localizedMessage", "stackTrace"})
public class BusinessException extends RuntimeException implements Serializable {
	private Logger logger = Logger.getLogger(BusinessException.class);
    private String errorCode;
    private String fullMessage;
    private Object[] arguments;
    private String responseCode;

    public BusinessException(String errorCode) {
        super();
        this.errorCode = errorCode;

        translateMessage();
    }

    public BusinessException(String errorCode, Object... arguments) {
        super();
        this.errorCode = errorCode;
        this.arguments = arguments;

        translateMessage();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getFullMessage() {
        return fullMessage;
    }

    public void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    private void translateMessage() {
    	if (errorCode.equals("IB-1009")) {
            ResponseCodeDao responseCodeDao = (ResponseCodeDao) ServiceLocator.getService("responseCodeDao");
            responseCode = "RC-" + arguments[0];
            if (String.valueOf(arguments[0]) != null && String.valueOf(arguments[0]).equals("50")) {
                fullMessage = "Timeout";
                logger.info("errorCode=" + errorCode + " responseCode=50 Timeout");
                return;
            } else {
	            ResponseCode rc = responseCodeDao.get(String.valueOf(arguments[0]));
	            if (rc != null) {
	                fullMessage = rc.getDescription();
	                if (rc.getResponseCode().equals("TP")) {
	                    SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
	    				String paidPeriod = sdf.format(new Date());
	    				fullMessage = "TAGIHAN BULAN "+ paidPeriod.toUpperCase() +" BELUM TERSEDIA";
	    				responseCode = "RC-" + "TQ";
	                } else if (rc.getResponseCode().equals("AR")) {
	            		fullMessage += " ("+ arguments[1] +"), Anda Telah melakukan transfer ke rekening " + arguments[2] + " sejumlah " + arguments[3];
	                }
	            	if (arguments.length == 2) {	
	            		fullMessage = fullMessage.replace("{0}", arguments[1].toString());
	            	} else if (arguments.length == 3) {	
	            		fullMessage = fullMessage.replace("{1}", arguments[1].toString());
	            	}  
                    logger.info("errorCode=" + errorCode + " responseCode=" + responseCode + " fullMessage=" + fullMessage);
	                return;
	            } else {
	            	if (arguments[1] != null) fullMessage = arguments[1].toString();
	            	else fullMessage = "Timeout";
	            	logger.info("errorCode=" + errorCode + " responseCode=" + responseCode + " fullMessage=" + fullMessage);
	            	return;
	            }
            }
        }

        ResourceBundle bundle;

        try {
            bundle = ResourceBundle.getBundle("errors");
        } catch (MissingResourceException e) {
            e.printStackTrace();
            return;
        }

        try {
            String message = bundle.getString(this.errorCode);
            if (this.arguments != null && this.arguments.length > 0) {
                this.fullMessage = MessageFormat.format(message, this.arguments);
            }
            else {
                this.fullMessage = message;
            }
        } catch (MissingResourceException e) {
            if (this.arguments != null && this.arguments.length > 0) {
                this.fullMessage = MessageFormat.format(this.errorCode, this.arguments);
                if (this.errorCode.startsWith("KAI-")) {
                	this.fullMessage = (String) this.arguments[0];
                }
            }
            else {
                this.fullMessage = this.errorCode;
            }
        }

        logger.info("errorCode=" + errorCode + " fullMessage=" + fullMessage);

    }

    @Override
    public String getMessage() {
        return this.errorCode + " (" + this.fullMessage.trim() +
                (super.getMessage() == null ? "" : " " + super.getMessage().trim()) + ")" ;
    }
}
