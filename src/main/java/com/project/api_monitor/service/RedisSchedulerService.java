package com.project.api_monitor.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisSchedulerService {
	
	private final StringRedisTemplate redisTemplate;

	public RedisSchedulerService(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	public void setNextCheck(Integer monitorId, long nextCheckTime) {
		
		String key = "monitor:" + monitorId + ":nextCheck";
		redisTemplate.opsForValue().set(key, String.valueOf(nextCheckTime));
	}
	
	public Long getNextCheck(Integer monitorId) {
		String key = "monitor:" + monitorId + ":nextCheck";
		String value = redisTemplate.opsForValue().get(key);
		
		if(value == null)
			return null;
		
		return Long.valueOf(value);
	}
	
	
}
