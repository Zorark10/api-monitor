package com.project.api_monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.project.api_monitor.model.MonitorResultEvent;

import tools.jackson.databind.ObjectMapper;

@Service
public class MonitorResultProducer {
	
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
		
	private final ObjectMapper objectMapper;
	
	public MonitorResultProducer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	public void sendResult(MonitorResultEvent event) {
		String message = objectMapper.writeValueAsString(event);
		kafkaTemplate.send("monitor-results", message);
	}
}
