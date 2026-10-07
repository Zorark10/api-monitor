package com.project.api_monitor.exception;

public class MonitorNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MonitorNotFoundException(Integer id) {
        super("Monitor not found with id: " + id);
    }
}
