package com.example.kadai_002.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.kadai_002.entity.Users;

@Component
public class ResetEventPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;
	
	public ResetEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	public void publishResetEvent(Users users, String requestUrl) {
		applicationEventPublisher.publishEvent(new ResetEvent(this, users, requestUrl));
	}

}