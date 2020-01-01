package org.redapps.netmon.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class EmailSender {

    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    private static String username;
    private static String password;
    private static String subject;
    private static String emailfrom;
    private static boolean auth;
    private static boolean starttls;
    private static String host;
    private static int port;

    public EmailSender(){
    }

    /*
     * Read email configuration from properties file.
     */
    @Value("${email.Username}")
    public void setUsername(String value) {
        username = value;
    }

    @Value("${email.Password}")
    public void setPassword(String value) {
        password = value;
    }

    @Value("${email.Subject}")
    public void setSubject(String value) {
        subject = value;
    }

    @Value("${email.EmailFrom}")
    public void setEmailFrom(String value) {
        emailfrom = value;
    }

    @Value("${email.auth}")
    public void setAuth(boolean value) {
        auth = value;
    }

    @Value("${email.starttls}")
    public void setStarttls(boolean value) {
        starttls = value;
    }

    @Value("${email.host}")
    public void setHost(String value) {
        host = value;
    }

    @Value("${email.port}")
    public void setPort(int value) { port = value;
    }

    /**
     * Sending an email.
     * @param msg the body of email
     * @param emailAddress email address
     */
    void sendMail(String msg, String emailAddress) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
	    props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailfrom));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailAddress));
            message.setSubject(subject);
            message.setText(msg);

            Transport.send(message);

            logger.info("An email has been sent to "+ emailAddress + " " + msg);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
