package com.project.api_monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.project.api_monitor.model.Monitor;
import com.project.api_monitor.repository.MonitorRepo;

@Component
public class MonitorKafkaConsumer {
	
	@Autowired
	private MonitorRepo monitorRepo;
	
	@Autowired
	private HealthCheckService healthCheckService;
	
	@KafkaListener(topics = "monitor-checks",
					groupId = "monitor-check-group")
	public void receiveMessage(String message) {
		Integer monitorId = Integer.valueOf(message);

        Monitor monitor = monitorRepo.findById(monitorId)
        		.orElseThrow(() -> new RuntimeException("Monitor not found: " + monitorId));

        healthCheckService.monitorCheck(monitor);

	}
}
