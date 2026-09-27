package com.project.api_monitor.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.project.api_monitor.model.IncidentEvent;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class IncidentEventConsumer {
	private final ObjectMapper objectMapper;
	private final EmailService emailService;
	
	@KafkaListener(topics = "incident-events", 
			groupId = "incident-notification-group")
	public void receiveIncidentEvent(String json) {
		IncidentEvent event = objectMapper.readValue(json, IncidentEvent.class);
		emailService.sendIncidentEmail(event);
	}
	
}
