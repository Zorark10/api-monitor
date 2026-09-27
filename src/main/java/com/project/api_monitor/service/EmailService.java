package com.project.api_monitor.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.project.api_monitor.model.IncidentEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender javaMailSender;
	
	public void sendIncidentEmail(IncidentEvent event) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("harrybarry266@gmail.com");
		message.setSubject("API Monitor Incident - " + event.getStatus());
		message.setText("API Monitor Incident\n"
				+ "\n"
				+ "Monitor ID: " + event.getMonitorId() + "\n"
				+ "Incident ID: " + event.getIncidentId() + "\n"
				+ "Status: " + event.getStatus() + "\n"
				+ "Started At: " + event.getStartedAt() + "\n"
				+ "Resolved At: " + event.getResolvedAt());
		
		javaMailSender.send(message);
	}
}
