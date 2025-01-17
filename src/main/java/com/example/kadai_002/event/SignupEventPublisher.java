package com.example.kadai_002.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.kadai_002.entity.Users;

@Component
public class SignupEventPublisher {
	   private final ApplicationEventPublisher applicationEventPublisher;
	     
	     public SignupEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
	         this.applicationEventPublisher = applicationEventPublisher;                
	     }
	     
	     public void publishSignupEvent(Users users, String requestUrl) {
	         applicationEventPublisher.publishEvent(new SignupEvent(this, users, requestUrl));
	     }
	}
