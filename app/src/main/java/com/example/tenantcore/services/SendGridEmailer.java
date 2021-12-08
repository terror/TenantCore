package com.example.tenantcore.services;


import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import java.io.IOException;

public class SendGridEmailer {

  private static SendGrid sendGrid = new SendGrid("");
  private static Email from = new Email("");

  public static void SendEmail(String recipient, String subject, String message) throws IOException {
    Email to = new Email(recipient);
    Content content = new Content("text", message);

    Mail mail = new Mail(from, subject, to, content);

    Request request = new Request();
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");
    request.setBody(mail.build());

    Response response = sendGrid.api(request);
  }
}
