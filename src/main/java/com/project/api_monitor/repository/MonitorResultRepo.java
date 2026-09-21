package com.project.api_monitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.api_monitor.model.Monitor;
import com.project.api_monitor.model.MonitorResult;

public interface MonitorResultRepo extends JpaRepository<MonitorResult, Integer>{
	public List<MonitorResult> findByMonitor(Monitor monitor);
}
