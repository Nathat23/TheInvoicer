package com.invoicer.main.email;

import com.invoicer.main.TheInvoicer;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class EmailHandler {

    private EmailConfig emailConfig;
    private Properties properties;
    private Yaml yaml;
    private final String fileName;

    public EmailHandler(String fileName) {
        this.fileName = fileName;
    }

    public void init() throws IOException {
        yaml = new Yaml(new Constructor(EmailConfig.class));
        File file = new File(fileName);
        InputStream inputStream1 = file.exists() ? file.toURL().openStream() : TheInvoicer.class.getResourceAsStream("/" + fileName);
        emailConfig = yaml.load(inputStream1);
        properties = new Properties();
        properties.put("mail.smtp.auth", emailConfig.isSmtpAuth());
        properties.put("mail.smtp.starttls.enable", emailConfig.isSmtpStartTtsEnable());
        properties.put("mail.smtp.host", emailConfig.getHost());
        properties.put("mail.smtp.port", emailConfig.getPort());
        properties.put("mail.smtp.ssl.trust", emailConfig.getSslTrust());
        saveFile();
    }

    public void saveFile() throws IOException {
        FileWriter writer = new FileWriter(fileName);
        yaml.dump(emailConfig, writer);
    }

    /**
     * Send Email to the specified email address. This is done on a different thread to prevent hanging.
     * @param email the email address
     * @param title the title of the email
     * @param html html code for the email
     * @return CompletableFuture for the email
     */
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
