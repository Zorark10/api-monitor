package com.project.api_monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.project.api_monitor.model.Monitor;
import com.project.api_monitor.repository.MonitorRepo;

@Component
public class MonitorScheduler {
	@Autowired
	private MonitorRepo monitorRepo;
	
	@Autowired
	private RedisSchedulerService redisSchedulerService;
	
	@Autowired
	private HealthCheckService healthCheckService;
	
	@Autowired
	private MonitorKafkaProducer monitorKafkaProducer;
	
	@Scheduled(fixedRate = 1000)
	public void scheduler() {
		List<Monitor> allMonitors = monitorRepo.findAll();
		
		for(Monitor monitor1 : allMonitors) {
			if(monitor1.isEnabled()) {
				Long nextCheckTime = redisSchedulerService.getNextCheck(monitor1.getId());
				if(nextCheckTime == null) {
					nextCheckTime = System.currentTimeMillis() + (monitor1.getInterval() * 1000L);
					redisSchedulerService.setNextCheck(monitor1.getId(), nextCheckTime);
					continue;
				}
				
				long currentTime = System.currentTimeMillis();
				if(currentTime >= nextCheckTime) {
					System.out.println("Sending Kafka message for monitor " + monitor1);
					monitorKafkaProducer.sendMonitorCheck(monitor1.getId());
					long nextCheck = System.currentTimeMillis() + (monitor1.getInterval() * 1000L);
					redisSchedulerService.setNextCheck(monitor1.getId(), nextCheck);
				}
			}
		}
				
	}
	
	
}
