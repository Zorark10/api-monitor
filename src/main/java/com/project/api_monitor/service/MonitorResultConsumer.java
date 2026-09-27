package com.project.api_monitor.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.project.api_monitor.model.Monitor;
import com.project.api_monitor.model.MonitorResult;
import com.project.api_monitor.model.MonitorResultEvent;
import com.project.api_monitor.model.MonitorStatusResponse;
import com.project.api_monitor.repository.MonitorRepo;

import ch.qos.logback.core.spi.ConfigurationEvent.EventType;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class MonitorResultConsumer {
	
	private final ObjectMapper objectMapper;
	private final MonitorRepo monitorRepo;
	private final MonitorResultService monitorResultService;
	private final IncidentService incidentService;
	private final RedisSchedulerService redisSchedulerService;
	
	@KafkaListener(topics = "monitor-results", groupId = "monitor-result-group")
	public void receiveMessage(String message) {

		MonitorResultEvent event = objectMapper.readValue(message, MonitorResultEvent.class);
		
		MonitorStatusResponse response = MonitorStatusResponse.builder().monitorId(event.getMonitorId())
										.status(event.getStatus()).responseTime(event.getResponseTime())
										.checkedAt(event.getCheckedAt()).build();
		
		redisSchedulerService.setCurrentStatus(event.getMonitorId(), response);
		
		Monitor monitor = monitorRepo.findById(event.getMonitorId())
        		.orElseThrow(() -> new RuntimeException("Monitor not found: " + event.getMonitorId()));;
        
        MonitorResult result = MonitorResult.builder().monitor(monitor).status(event.getStatus())
        						.statusCode(event.getStatusCode()).responseTime(event.getResponseTime())
        						.checkedAt(event.getCheckedAt()).build();
        
		monitorResultService.saveResult(result);
		incidentService.processResult(result);
        }
}
