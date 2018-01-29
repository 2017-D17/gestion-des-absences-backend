package dev.gda.api.service;

public interface EmailService {
	void sendSimpleMessage(String to, String subject, String text);
}
