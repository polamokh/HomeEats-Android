package com.example.homeeats.EmailSender;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil{

    public static void sendEmail(final String to, final String subject, final String body) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final String from = "homeeats.ris.2020@gmail.com";
                final String password = "HomeEats123";

                // Assuming you are sending email from through gmails smtp
                String host = "smtp.gmail.com";

                // Get system properties
                Properties properties = System.getProperties();

                // Setup mail server
                properties.put("mail.smtp.host", host);
                properties.put("mail.smtp.port", "465");
                properties.put("mail.smtp.ssl.enable", "true");
                properties.put("mail.smtp.auth", "true");

                // Get the Session object.// and pass username and password
                Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }

                });

                // Used to debug SMTP issues
                session.setDebug(true);

                try {
                    // Create a default MimeMessage object.
                    MimeMessage message = new MimeMessage(session);

                    // Set From: header field of the header.
                    message.setFrom(new InternetAddress(from));

                    // Set To: header field of the header.
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                    // Set Subject: header field
                    message.setSubject(subject);

                    // Now set the actual message
                    message.setText(body);

                    Log.e("Mail", "sending");
                    // Send message
                    Transport transport = session.getTransport();
                    transport.send(message, from, password);
                    Log.e("Mail", "sent");
                } catch (
                        MessagingException mex) {
                    Log.e("Exception", mex.toString());
                }
                return null;
            }
        }.execute();
    }
}