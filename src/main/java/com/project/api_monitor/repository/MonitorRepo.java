package com.project.api_monitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.api_monitor.model.Monitor;

public interface MonitorRepo extends JpaRepository<Monitor, Integer>{

}
