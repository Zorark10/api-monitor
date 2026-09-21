package com.project.api_monitor.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.api_monitor.model.Monitor;
import com.project.api_monitor.model.MonitorResult;
import com.project.api_monitor.model.MonitorStatistics;
import com.project.api_monitor.model.MonitorStatus;
import com.project.api_monitor.repository.MonitorResultRepo;

@Service
public class MonitorResultService {
	@Autowired
	private MonitorResultRepo repo;
	
	public MonitorResult saveResult(MonitorResult result) {
		return repo.save(result);
	}
	
	public List<MonitorResult> getResultsByMonitor(Monitor monitor){
		return repo.findByMonitor(monitor);
	}
	
	public MonitorStatistics getStatistics(Monitor monitor){
		List<MonitorResult> results = getResultsByMonitor(monitor);
		int totalChecks = results.size();
		int successfulChecks = 0, failedChecks = 0;
		for(MonitorResult result : results) {
			if(result.getStatus() == MonitorStatus.UP) {
				successfulChecks++;
			} else if(result.getStatus() == MonitorStatus.DOWN) {
				failedChecks++;
			}
		}
		double uptimePerc = 0;
		if(totalChecks != 0) {
			uptimePerc = ((double) successfulChecks / totalChecks) * 100;
		} else {
			uptimePerc = 0;
		}		long totalResponseTime = 0;
		int count = 0;
		Long min = null, max = null;
		
		for(MonitorResult result : results) {
			if(result.getResponseTime() == null)
				continue;
			
			totalResponseTime += result.getResponseTime();
			if(min == null || result.getResponseTime() < min) {
				min = result.getResponseTime();
			}
			if(max == null || result.getResponseTime() > max) {
				max = result.getResponseTime();
			}
			count++;
		}
		double averageResponseTime = 0;
		if(count > 0)
			averageResponseTime = (double) totalResponseTime / count;
		else 
			averageResponseTime = 0;
		
		return MonitorStatistics.builder().monitorId(monitor.getId())
								.totalChecks(totalChecks).successfulChecks(successfulChecks)
								.failedChecks(failedChecks).uptimePercentage(uptimePerc)
								.averageResponetime(averageResponseTime).minResponseTime(min)
								.maxResponseTime(max).build();
		
	}
	
}
