package com.project.api_monitor.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.project.api_monitor.model.Monitor;
import com.project.api_monitor.model.MonitorResult;
import com.project.api_monitor.model.MonitorResultEvent;
import com.project.api_monitor.model.MonitorStatus;

@Service
public class HealthCheckService {
	
	@Autowired
	MonitorResultProducer monitorResultProducer;
	
	@Async
	public void monitorCheck(Monitor monitor) {

		long startTime = System.nanoTime();
		HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(monitor.getTimeout()))
										.build();
		HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(monitor.getUrl()))
										.timeout(Duration.ofMillis(monitor.getTimeout()))
										.method(monitor.getHttpMethod().name(), HttpRequest.BodyPublishers.noBody());
		HttpRequest request = requestBuilder.build();
		try {
			
		HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.discarding());
		
		long endTime = System.nanoTime();
		long responseTime = (endTime - startTime) / 1000000; //converting to ms
		
		int statusCode = response.statusCode();
		MonitorStatus status;
		if(statusCode >= 200 && statusCode <=399) {
			status = MonitorStatus.UP;
		} else {
			status = MonitorStatus.DOWN;
		}
		MonitorResultEvent result = MonitorResultEvent.builder().monitorId(monitor.getId()).checkedAt(LocalDateTime.now()).status(status)
								.statusCode(statusCode).responseTime(responseTime).build();
		
		monitorResultProducer.sendResult(result);

		} catch(Exception e) {
			long endTime = System.nanoTime();
			long responseTime = (endTime - startTime) / 1000000;
			MonitorResultEvent result = MonitorResultEvent.builder().monitorId(monitor.getId()).checkedAt(LocalDateTime.now()).status(MonitorStatus.DOWN)
					.statusCode(null).responseTime(responseTime).build();

			monitorResultProducer.sendResult(result);
		}
	}
}