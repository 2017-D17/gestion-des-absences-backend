package dev.gda.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Profile("production")
public class EmailServiceImpl implements EmailService {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    public JavaMailSender emailSender;

	@Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);

        try {
            this.emailSender.send(message);
        }
        catch (MailException ex) {
        	LOG.warn("Error in sending mail for " + to, ex);
        }
        
    }
	



}
