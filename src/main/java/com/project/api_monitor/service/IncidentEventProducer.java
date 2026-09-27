package com.project.api_monitor.service;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.project.api_monitor.model.IncidentEvent;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class IncidentEventProducer {
	
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;
	
	public void sendIncidentEvent(IncidentEvent event) {
		String json = objectMapper.writeValueAsString(event);
		kafkaTemplate.send("incident-events", json);			
	}
}
