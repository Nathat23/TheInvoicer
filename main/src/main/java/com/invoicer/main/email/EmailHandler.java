package com.invoicer.main.email;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class EmailHandler {

    private final EmailConfig emailConfig;
    private Properties properties;

    public EmailHandler(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    public void init() {
        properties = new Properties();
        properties.put("mail.smtp.auth", emailConfig.isSmtpAuth());
        properties.put("mail.smtp.starttls.enable", emailConfig.isSmtpStartTtsEnable());
        properties.put("mail.smtp.host", emailConfig.getHost());
        properties.put("mail.smtp.port", emailConfig.getPort());
        properties.put("mail.smtp.ssl.trust", emailConfig.getSslTrust());
    }

    public CompletableFuture<Void> sendEmail(String email, String title, String html) {
        return CompletableFuture.supplyAsync(() -> {
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailConfig.getUsername(), emailConfig.getPassword());
                }
            });
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailConfig.getUsername()));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                message.setSubject(title);
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setContent(html, "text/html; charset=utf-8");
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);
                message.setContent(multipart);
                session.setDebug(true);
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();;
            }
            return null;
        });
    }
}
