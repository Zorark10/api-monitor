package com.project.api_monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MonitorKafkaProducer {
	
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
	
	public void sendMonitorCheck(Integer monitorId) {
		kafkaTemplate.send("monitor-checks", String.valueOf(monitorId));
		
	}

}
