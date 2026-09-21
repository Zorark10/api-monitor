package com.project.api_monitor.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.api_monitor.model.Monitor;
import com.project.api_monitor.repository.MonitorRepo;

@Service
public class MonitorService {
	
	@Autowired
	private MonitorRepo monitorRepo;
	
	public Monitor addMonitor(Monitor monitor) {
		monitor.setCreatedAt(LocalDateTime.now());
		return monitorRepo.save(monitor);
	}
	
	public List<Monitor> getAllMonitors(){
		return monitorRepo.findAll();
	}
	
	public Monitor getMonitorById(Integer id) throws Exception {
		Monitor monitor = monitorRepo.findById(id).orElse(null);
		if(monitor == null)
			throw new Exception();
		return monitor;
	}
	
	public Monitor updateMonitor(Integer id, Monitor monitor) throws Exception {
		Monitor monitor2 = monitorRepo.findById(id).orElse(null);
		if(monitor2 == null)
			throw new Exception();
		monitor2.setUrl(monitor.getUrl());
		monitor2.setName(monitor.getName());
		monitor2.setHttpMethod(monitor.getHttpMethod());
		monitor2.setInterval(monitor.getInterval());
		monitor2.setTimeout(monitor.getTimeout());
		monitor2.setEnabled(monitor.isEnabled());
		
		return monitorRepo.save(monitor2);
	}
	public void deleteMonitor(Integer id) throws Exception {
		Monitor monitor = monitorRepo.findById(id).orElse(null);
		if(monitor == null)
			throw new Exception();
		monitorRepo.delete(monitor);
	}
	
}
