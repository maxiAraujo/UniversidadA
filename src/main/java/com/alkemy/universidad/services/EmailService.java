package com.alkemy.universidad.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
	
	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String mailFrom;

	@Value("${spring.mail.password}")
	private String mailPassword;
	/*
	 * supportEmail (name, lastName, email, text)
	 * supportAdmin (name, lastName, email, text, userEmail)
	 */
	
	public void sendEmail(String html, String subject, String email, String name, String lastName,
			String text, String userEmail, String code, String dni) throws MessagingException, IOException{
		
//		System.out.println(datos.get("name"));
//		System.out.println(datos.get("lastName"));
//		System.out.println("email remitente " + datos.get("email"));
//		System.out.println(datos.get("text"));
//		System.out.println(datos.get("userEmail"));
//		System.out.println(datos.get("html"));
//		System.out.println(datos.get("subject"));
//		System.out.println("----------------------------------------");
		
		
		Properties prop = new Properties();
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        Context context = new Context();
        String registerLink = "http://localhost:8080/register/completed?confirmation="+dni;
        String recoverPasswordLink = "http://localhost:8080/changepassword?email="+email;
        context.setVariable("name", name);
        context.setVariable("lastName", lastName);
        context.setVariable("email", email);
        context.setVariable("text", text);
        context.setVariable("userEmail", userEmail);
        context.setVariable("code", code);
        context.setVariable("registerLink", registerLink);
        context.setVariable("recoverPasswordLink", recoverPasswordLink);
        
        String process = templateEngine.process("/emails/" + html + ".html", context);
        new Thread(() -> {
        	try {
        		MimeMessage message = mailSender.createMimeMessage();

                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, "UTF-8");
                mimeMessageHelper.setFrom(mailFrom, "Universidad Alkemy");
                mimeMessageHelper.setTo(email);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(process, true);

                MimeBodyPart mensaje = new MimeBodyPart();
                mensaje.setContent(process, "text/html");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mensaje);
                message.setContent(multipart);

                mailSender.send(message);
        	}catch(Exception e) {
        		e.printStackTrace();
        	}
        }).start();
	}
}
