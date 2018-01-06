package com.dwidasa.engine.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.util.Map;

/**
 * Class for sending e-mail messages based on Velocity templates.
 */
public class MailEngine {
    private final Log log = LogFactory.getLog(MailEngine.class);
    private MailSender mailSender;
    private VelocityEngine velocityEngine;
    private TaskExecutor taskExecutor;
    private String defaultFrom;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setDefaultFrom(String defaultFrom) {
        this.defaultFrom = defaultFrom;
    }

    /**
     * Send a simple message based on a Velocity template.
     * @param msg the message to populate
     * @param templateName the Velocity template to use (relative to classpath)
     * @param model a map containing key/value pairs
     */
    @SuppressWarnings("rawtypes")
	public void sendMessage(SimpleMailMessage msg, String templateName, Map model) {
        String result = null;

        try {
            result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, model);
        } catch (VelocityException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        msg.setText(result);
        send(msg);
    }

    /**
     * Convenience method for sending messages with attachments.
     * @param recipients array of e-mail addresses
     * @param sender e-mail address of sender
     * @param attachment attachment byte array
     * @param bodyText text in e-mail
     * @param subject subject of e-mail
     * @param attachmentName name for attachment
     * @throws MessagingException thrown when can't communicate with SMTP server
     */
    public void sendMessage(String[] recipients, String sender, byte[] attachment, String bodyText,
                            String subject, String attachmentName) throws MessagingException {
        MimeMessage message = ((JavaMailSenderImpl) mailSender).createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipients);

        // use the default sending if no sender specified
        if (sender == null) {
            helper.setFrom(defaultFrom);
        } else {
           helper.setFrom(sender);
        }

        helper.setText(bodyText);
        helper.setSubject(subject);

        helper.addAttachment(attachmentName, new ByteArrayResource(attachment));
        taskExecutor.execute(new MailTask(message));
    }

    @SuppressWarnings("rawtypes")
	public void sendHTMLMessage(String[] recipients, String sender, String subject, String templateName, Map model)
            throws Exception {

        MimeMessage message = ((JavaMailSenderImpl) mailSender).createMimeMessage();
        message.setSubject(subject);

        if (sender == null) {
            message.setFrom(new InternetAddress(defaultFrom));
        }
        else {
           message.setFrom(new InternetAddress(sender));
        }

        for (String recipient : recipients) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        }

        String bodyText = null;
        try {
            bodyText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, model);
        } catch (VelocityException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        message.setContent(bodyText, "text/html");
        taskExecutor.execute(new MailTask(message));
    }
    @SuppressWarnings("rawtypes")
	public void sendHTMLMessageWithAttachment(String[] recipients, String sender, String subject, String templateName, Map model, String filename)
            throws Exception {

        MimeMessage message = ((JavaMailSenderImpl) mailSender).createMimeMessage();
        message.setSubject(subject);

        if (sender == null) {
            message.setFrom(new InternetAddress(defaultFrom));
        }
        else {
           message.setFrom(new InternetAddress(sender));
        }

        for (String recipient : recipients) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        }

        String bodyText = null;
        try {
            bodyText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, model);
        } catch (VelocityException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        BodyPart messageBodyPart1 = new MimeBodyPart();  
        messageBodyPart1.setContent(bodyText, "text/html"); 
        
        MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
        
        DataSource source = new FileDataSource(filename);  
        messageBodyPart2.setDataHandler(new DataHandler(source));  
        messageBodyPart2.setFileName(filename);  
        
        Multipart multipart = new MimeMultipart();  
        multipart.addBodyPart(messageBodyPart1);  
        multipart.addBodyPart(messageBodyPart2);  
      
        message.setContent(multipart);
        taskExecutor.execute(new MailTask(message));
    }
    /**
     * Send a simple message with pre-populated values.
     * @param msg the message to send
     */
    public void send(SimpleMailMessage msg) {
        taskExecutor.execute(new MailTask(msg));
    }

    /**
     * Class to setup mail task to be sent asynchronously
     */
    private class MailTask implements Runnable {
        private SimpleMailMessage simpleMailMessage = null;
        private MimeMessage mimeMessage = null;

        private MailTask(SimpleMailMessage simpleMailMessage) {
            this.simpleMailMessage = simpleMailMessage;
        }

        private MailTask(MimeMessage mimeMessage) {
            this.mimeMessage = mimeMessage;
        }

        public void run() {
            if (mimeMessage == null) {
                mailSender.send(simpleMailMessage);
            }
            else {
                ((JavaMailSenderImpl) mailSender).send(mimeMessage);
            }
        }
    }
}
