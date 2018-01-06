package com.dwidasa.engine.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.model.AeroCustomer;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.PeriodicProcess;
import com.dwidasa.engine.model.view.AccountStatementView;
import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.util.DateUtils;

/**
 * Convenience class to send mail message using our domain object.
 *
 * @author rk
 */
@Service("messageMailer")
public class MessageMailer {
    @Autowired
    private MailEngine mailEngine;

    @Autowired
    private CustomerDao customerDao;

    public MessageMailer() {
    }

    /**
     * Send mail message using template and data provided.
     * @param templateName template file name
     * @param view data to fill the template
     */
    public void sendTransactionMessage(String templateName, BaseView view) {
        Customer c = customerDao.get(view.getCustomerId());

        if (c.getCustomerEmail() == null || c.getCustomerEmail() == "") {
            return;
        }

        String to = c.getCustomerName() + "<" + c.getCustomerEmail() + ">";
        String subject = "Jurnal transaksi elektronik banking BPRKS";

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("view", view);

        NumberTool nf = new NumberTool();
        model.put("nf", nf);

        DateUtils du = new DateUtils();
        model.put("du",du);

        Locale locale = new Locale("in");
        model.put("locale", locale);

        try {
            mailEngine.sendHTMLMessage(new String[] {to}, null, subject, templateName, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String sendTransactionMessageAero(String templateName, BaseView view, AeroCustomer aeroCustomer) {
    	//untuk mendapatkan list alamat email
    	AeroTicketingView aeroTicketingView = (AeroTicketingView) view;
        String to = aeroCustomer.getCustomerName() + "<" + aeroCustomer.getCustomerEmail() + ">";
        String[] strs = null;
        String[] recipients = null;
        if (aeroTicketingView.getCardData1() != null && !aeroTicketingView.getCardData1().equals("") && aeroTicketingView.getCardData1().contains("@")) {
        	strs = aeroTicketingView.getCardData1().split(",");
        	recipients = new String[strs.length + 1];
            recipients[0] = to;
            for (int i = 0; i < strs.length; i++) {
            	recipients[i + 1] = strs[i];
            }
        } else {
        	recipients = new String[1];
        	recipients[0] = to;
        }
        		
        
        String subject = "Jurnal transaksi elektronik banking BPRKS";

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("view", view);

        NumberTool nf = new NumberTool();
        model.put("nf", nf);

        DateUtils du = new DateUtils();
        model.put("du",du);

        Locale locale = new Locale("in");
        model.put("locale", locale);

        
        String fileName = aeroTicketingView.getCardData2();
        try {
            mailEngine.sendHTMLMessageWithAttachment(recipients, null, subject, templateName, model, fileName);
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail " + e.getMessage();
        }
    }

    /**
     * Send mail message containing an attachment of account statement.
     * @param customerId customer id
     * @param accountStatements list of account statement
     * @throws MessagingException
     */
    public void sendAccountMessage(Long customerId, List<AccountStatementView> accountStatements)
            throws MessagingException {
        String attachment = "\"Tanggal Transaksi (dd/mm/yyyy hh:mm:ss)\", \"Jumlah \", \"Jenis Transaksi\", \"Keterangan \" \n";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Customer customer = customerDao.get(customerId);
        String email = customer.getCustomerName() + "<" + customer.getCustomerEmail() + ">";

        for (AccountStatementView mas : accountStatements) {
            attachment = attachment + "\"" + sdf.format(mas.getTransactionDate()) + " \",";
            attachment = attachment + "\"" + mas.getAmount().longValue() + " \",";
            attachment = attachment + "\""
                    + (mas.getTransactionName() == null ? "" : mas.getTransactionName())
                    + " \",";
            attachment = attachment + "\""
                    + (mas.getDescription() == null ? "" : mas.getDescription().replace("\"","\"\"")) + " \"\n";
        }

        mailEngine.sendMessage(new String[] {email}, null, attachment.getBytes(),
                "Terima kasih telah menggunakan layanan Elektronik Banking BPRKS \n" +
                "\n Mutasi rekening terlampir dalam attachment \n",
                "Mutasi transaksi elektronik banking BPRKS", "mutasi.csv");
    }

	public void sendQueuedTransactionEmail(String email, PeriodicProcess process) {
		String to = "Bank Maspion Operation" + "<" + email + ">";
		String status = getStrProcessStatus(process.getStatus());
        String subject = "BMI-Batch Transaksi Terjadwal " + status;
        String strDate = DateUtils.getYYYYMMDD(process.getProcessDate());
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("strDate", strDate);
        model.put("strStart", getStrDateTime(process.getStartTime()));
        model.put("strFinish", getStrDateTime(process.getFinishTime()));
        model.put("strDurasi", getStrDuration(process.getStartTime(), process.getFinishTime()));
        model.put("strStatus", status);

        try {
            mailEngine.sendHTMLMessage(new String[] {to}, null, subject, "TransferView.vm", model);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public String getStrDuration(Date startTime, Date endTime) {
    	if (endTime == null || startTime == null) {
    		return "-";
    	}
    	long diff = endTime.getTime() - startTime.getTime();
    	long diffMinutes = diff / (60 * 1000); 
    	return String.valueOf(diffMinutes) + " menit";
    }

	private String getStrProcessStatus(String status) {
		if ("S".equals(status)) {
			return "Sukses";
		} else {
			return "Gagal"; 
		}
	}

	public String getStrDateTime(Date dateTime) {
    	if (dateTime == null) return "-";
    	return DateUtils.getYYYYMMDD(dateTime);
    }
	
}