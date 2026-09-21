package com.project.api_monitor.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.api_monitor.model.Incident;
import com.project.api_monitor.model.IncidentStatus;
import com.project.api_monitor.model.MonitorResult;
import com.project.api_monitor.repository.IncidentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentService {
	private final IncidentRepository incidentRepo;
	
	public void processResult(MonitorResult result) {
		
		Optional<Incident> openIncident = incidentRepo
				.findByMonitorAndStatus(result.getMonitor(), IncidentStatus.OPEN);
		switch (result.getStatus()) {
			case DOWN:
				if(openIncident.isEmpty()) {
					Incident incident = Incident.builder().monitor(result.getMonitor())
										.status(IncidentStatus.OPEN).startedAt(result.getCheckedAt())
										.resolvedAt(null).build();
					incidentRepo.save(incident);
				}
				break;
				
			case UP:
				if(openIncident.isPresent()) {
					Incident incident = openIncident.get();
					incident.setStatus(IncidentStatus.RESOLVED);
					incident.setResolvedAt(result.getCheckedAt());
					incidentRepo.save(incident);
				}
				break;
		}
		
	}
	
}
