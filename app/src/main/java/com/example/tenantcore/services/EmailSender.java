package com.example.tenantcore.services;

import android.content.Context;
import android.os.AsyncTask;

import com.example.tenantcore.BuildConfig;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender extends AsyncTask {

  private Context context;
  private Session session;
  private String email;
  private String subject;
  private String message;

  public EmailSender(Context context, String email, String subject, String message) {
    this.context = context;
    this.email = email;
    this.subject = subject;
    this.message = message;
  }

  @Override
  protected Object doInBackground(Object[] objects) {
    // Set up the mail properties
    Properties properties = new Properties();
    properties.put("mail.smtp.host", "smtp.gmail.com");
    properties.put("mail.smtp.socketFactory.port", "465");
    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.port", "465");

    // Create the session
    session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(BuildConfig.JAVAMAIL_API_EMAIL, BuildConfig.JAVAMAIL_API_PASSWORD);
      }
    });

    // Create and send the email
    MimeMessage mimeMessage = new MimeMessage(session);
    try {
      mimeMessage.setFrom(new InternetAddress(BuildConfig.JAVAMAIL_API_EMAIL));
      mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
      mimeMessage.setSubject(subject);
      mimeMessage.setText(message);
      Transport.send(mimeMessage);
    } catch (MessagingException e) {
      e.printStackTrace();
    }

    return null;
  }
}
