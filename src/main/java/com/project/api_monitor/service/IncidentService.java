package com.project.api_monitor.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.api_monitor.model.Incident;
import com.project.api_monitor.model.IncidentEvent;
import com.project.api_monitor.model.IncidentStatus;
import com.project.api_monitor.model.MonitorResult;
import com.project.api_monitor.repository.IncidentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentService {
	private final IncidentRepository incidentRepo;
	private final IncidentEventProducer incidentEventProducer;
	
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
					IncidentEvent event = IncidentEvent.builder().monitorId(incident.getMonitor().getId())
											.incidentId(incident.getId()).status(IncidentStatus.OPEN)
											.startedAt(incident.getStartedAt()).resolvedAt(null)
											.build();
					incidentEventProducer.sendIncidentEvent(event);
				}
				break;
				
			case UP:
				if(openIncident.isPresent()) {
					Incident incident = openIncident.get();
					incident.setStatus(IncidentStatus.RESOLVED);
					incident.setResolvedAt(result.getCheckedAt());
					incidentRepo.save(incident);
					
					IncidentEvent event = IncidentEvent.builder().monitorId(incident.getMonitor().getId())
							.incidentId(incident.getId()).status(IncidentStatus.RESOLVED)
							.startedAt(incident.getStartedAt()).resolvedAt(incident.getResolvedAt())
							.build();
					incidentEventProducer.sendIncidentEvent(event);
				}
				break;
		}
		
	}
	
}
