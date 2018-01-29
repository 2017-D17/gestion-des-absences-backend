package dev.gda.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("default")
public class EmailServiceDev implements EmailService {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Override
    public void sendSimpleMessage(String to, String subject, String text) {
		
		LOG.info("Sending mail to " + to + "\nwith subject "+ subject + "\nand body "+ text);   
    }

}
